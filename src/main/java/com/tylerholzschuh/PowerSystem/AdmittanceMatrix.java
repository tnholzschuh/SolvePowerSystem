package com.tylerholzschuh.PowerSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.complex.Complex;

public class AdmittanceMatrix {
	Map<Bus, HashMap<Bus, Complex>> lines;
	
	public AdmittanceMatrix() {
		lines = new HashMap<Bus, HashMap<Bus, Complex>>();
	}
	
	public void addLine(Bus bus1, Bus bus2, Complex admittance) {
		if (lines.get(bus1) == null) {
			lines.put(bus1, new HashMap<Bus, Complex>());
			lines.get(bus1).put(bus2, admittance);
		} else if (lines.get(bus1).get(bus2) == null) {
			lines.get(bus1).put(bus2, admittance);
		} else {
			lines.get(bus1).put(bus2, lines.get(bus1).get(bus2).add(admittance));
		}
		
		if (lines.get(bus2) == null) {
			lines.put(bus2, new HashMap<Bus, Complex>());
			lines.get(bus2).put(bus1, admittance);
		} else if (lines.get(bus2).get(bus1) == null) {
			lines.get(bus2).put(bus1, admittance);
		} else {
			lines.get(bus2).put(bus1, lines.get(bus2).get(bus1).add(admittance));
		}
	}
	
	public Complex getComplexElement(Bus b1, Bus b2) {
		if (lines.get(b1) == null) {
			return new Complex(0);
		}
	
		if (b1.equals(b2)) {
			Complex y = new Complex(0);
			
			for (Entry<Bus, Complex> c : lines.get(b1).entrySet()) {
				y = y.add(c.getValue());
			}
			return y;
		}
		
		if (lines.get(b1).get(b2) == null) {
			return new Complex(0);
		} 
		
		return lines.get(b1).get(b2).negate();
	}
}
