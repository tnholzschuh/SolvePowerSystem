package com.tylerholzschuh.PowerSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.complex.Complex;

public class PowerSystem implements NewtonRaphsonCapable {
	List<Bus> allBuses, VDeltaBuses, PVBuses, PQBuses;
	Map<String, Bus> busLookupByName;
	AdmittanceMatrix admittanceMatrix;
	
	boolean solved;
	double desiredErrorRatio;
	

	public PowerSystem(double desiredErrorRatio) {
		this.desiredErrorRatio = desiredErrorRatio;
		setUp();
	}
	
	public PowerSystem() {
		this.desiredErrorRatio = .001;
		setUp();
	}
	
	private void setUp() {
		allBuses = new ArrayList<Bus>();
		VDeltaBuses = new ArrayList<Bus>();
		PVBuses = new ArrayList<Bus>();
		PQBuses = new ArrayList<Bus>();
		admittanceMatrix = new AdmittanceMatrix();
		busLookupByName = new HashMap<String, Bus>();
		busLookupByName.put("Ground", new Bus("Ground", Bus.VDelta, 0, 0, 0, 0));
		solved = false;
	}
	
	public void addBus(Bus newBus) {
		if (busLookupByName.containsKey(newBus.getName())) {
			return;
		}
		
		busLookupByName.put(newBus.getName(), newBus);
		allBuses.add(newBus);
		
		switch (newBus.getType()) {
			case Bus.VDelta:
				VDeltaBuses.add(newBus);
				break;
			case Bus.PV:
				PVBuses.add(newBus);
				break;
			case Bus.PQ:
				PQBuses.add(newBus);
		}
	}
	
	public void addLine(String busName1, String busName2, Complex impedance) {
		admittanceMatrix.addLine(busLookupByName.get(busName1), busLookupByName.get(busName2), (new Complex(1)).divide(impedance));
	}
	
	public void addLine(Bus bus1, Bus bus2, Complex impedance) {
		admittanceMatrix.addLine(bus1, bus2, (new Complex(1)).divide(impedance));
	}
	
	public List<Bus> getBuses() {
		return allBuses;
	}
	
	
	public int getNumberOfBuses() {
		return allBuses.size();
	}
	
	@Override
	public int getNumberOfInputs() {
		return PVBuses.size() + 2 * PQBuses.size();
	}

	@Override
	public int getNumberOfOutputs() {
		return PVBuses.size() + 2 * PQBuses.size();
	}

	@Override
	public double[] getInputErrorVector() {
		double[] inputErrorVector = new double[getNumberOfInputs()];
		
		for (int i = 0; i < PVBuses.size(); i++) {
			inputErrorVector[i] = PVBuses.get(i).getP() - getCalculatedPAtBus(PVBuses.get(i));
		}
		
		for (int i = 0; i < PQBuses.size(); i++) {
			inputErrorVector[i*2 + PVBuses.size()] = PQBuses.get(i).getP() - getCalculatedPAtBus(PQBuses.get(i));
			inputErrorVector[i*2 + PVBuses.size() + 1] = PQBuses.get(i).getQ() - getCalculatedQAtBus(PQBuses.get(i));
		}
		
		// TODO Auto-generated method stub
		return inputErrorVector;
	}
	
	public double getCalculatedPAtBus(Bus thisBus) {
		double power = 0;
		Complex y;
		
		for (Bus otherBus : allBuses) {
			y = admittanceMatrix.getComplexElement(thisBus, otherBus);
						
			power += thisBus.getV() * otherBus.getV() * 
					(y.getReal()*Math.cos(thisBus.getDelta()-otherBus.getDelta()) +
					 y.getImaginary()*Math.sin(thisBus.getDelta()-otherBus.getDelta()));			
		}
			
		return power;
	}
	
	public double getCalculatedQAtBus(Bus thisBus) {
		double reactivePower = 0;
		Complex y;
		
		for (Bus otherBus : allBuses) {
			y = admittanceMatrix.getComplexElement(thisBus, otherBus);
			reactivePower += thisBus.getV() * otherBus.getV() * 
					(y.getReal()*Math.sin(thisBus.getDelta()-otherBus.getDelta()) -
					 y.getImaginary()*Math.cos(thisBus.getDelta()-otherBus.getDelta()));	
		}
			
		return reactivePower;
		
	}

	@Override
	public double jacobianEntry(int row, int col) {
		final int dPdV = 0;
		final int dPdDelta = 1;
		final int dQdV = 2;
		final int dQdDelta = 3;
		
		int entryType = 0;
		int PQJacobianIndex;
		
		Bus bus1;
		Bus bus2;
		
		Complex y;
		
		if (row < PVBuses.size()) {
			bus1 = PVBuses.get(row);
		} else {
			PQJacobianIndex = (row - PVBuses.size());
			bus1 = PQBuses.get(PQJacobianIndex/2);
			
			if (PQJacobianIndex % 2 == 1) {
				entryType += 2;
			}
		}
		
		if (col < PVBuses.size()) {
			bus2 = PVBuses.get(col);
			entryType += 1;
		} else {
			PQJacobianIndex = (col - PVBuses.size());
			bus2 = PQBuses.get(PQJacobianIndex/2);
			
			if (PQJacobianIndex % 2 == 1) {
				entryType += 1;
			}
		}
		
		y = admittanceMatrix.getComplexElement(bus1, bus2);
		
		switch (entryType) {
			case dPdV:
				return bus1.getV() * 
					   (y.getReal()*Math.cos(bus1.getDelta()-bus2.getDelta()) +
						y.getImaginary()*Math.sin(bus1.getDelta()-bus2.getDelta()));
			case dPdDelta:
				return bus1.getV() * bus2.getV() *
					   (- y.getReal()*Math.sin(bus1.getDelta()-bus2.getDelta()) -
						y.getImaginary()*Math.cos(bus1.getDelta()-bus2.getDelta()));
			case dQdV:
				return bus1.getV() *
					   (y.getReal()*Math.sin(bus1.getDelta()-bus2.getDelta()) -
						y.getImaginary()*Math.cos(bus1.getDelta()-bus2.getDelta()));
			case dQdDelta:
				return bus1.getV() * bus2.getV() * 
			           (-y.getReal()*Math.cos(bus1.getDelta()-bus2.getDelta()) +
			            y.getImaginary()*Math.sin(bus1.getDelta()-bus2.getDelta()));
		}
		
		return 0;
	}


	@Override
	public void updateOutputs(double[] outputError) {
		for (int i = 0; i < PVBuses.size(); i++) {
			PVBuses.get(i).setDelta(outputError[i] + PVBuses.get(i).getDelta());
		}
		
		for (int i = 0; i < PQBuses.size(); i++) {
			PQBuses.get(i).setV(outputError[PVBuses.size()+i*2] + PQBuses.get(i).getV());
			PQBuses.get(i).setDelta(outputError[PVBuses.size()+i*2+1] + PQBuses.get(i).getDelta());
		}
		
	}

	@Override
	public double getMaxErrorRatio() {
		double errorRatio = 0;
		double error;
		
		for (Bus bus : PVBuses) {
			error = bus.getP() - getCalculatedPAtBus(bus); 
			errorRatio = Math.max(errorRatio, Math.abs(error/bus.getP()));
		}
		
		for (Bus bus : PQBuses) {
			error = bus.getP() - getCalculatedPAtBus(bus); 
			errorRatio = Math.max(errorRatio, Math.abs(error/bus.getP()));
						
			error = bus.getQ() - getCalculatedQAtBus(bus);
			errorRatio = Math.max(errorRatio, Math.abs(error/bus.getQ()));
		}
				
		return errorRatio;
	}

	@Override
	public double getDesiredMaxErrorRatio() {
		return desiredErrorRatio;
	}
	
	@Override
	public void finish() {
		for (Bus VDeltaBus : VDeltaBuses) {
			VDeltaBus.setP(getCalculatedPAtBus(VDeltaBus));
			VDeltaBus.setQ(getCalculatedQAtBus(VDeltaBus));
		}
		
		for (Bus PVBus : PVBuses) {
			PVBus.setQ(getCalculatedQAtBus(PVBus));
		}	
	}
}
