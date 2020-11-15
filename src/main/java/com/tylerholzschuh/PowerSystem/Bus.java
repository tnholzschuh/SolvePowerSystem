package com.tylerholzschuh.PowerSystem;

public class Bus {
	public final static int VDelta = 0;
	public final static int PV = 1;
	public final static int PQ = 2;

	// private int index;
	private String name;
	private int type;
	// private boolean solved=false;
	private double power, reactivePower, voltage, angle;
	
	public Bus(String name, int busType, double p, double q, double V, double delta) {
		// this.index = index;
		this.name = name;
		type = busType;
		
		power = p;
		reactivePower = q;
		voltage = V;
		angle = delta;
	}
	/*
	public int getIndex() {
		return index;
	}
	*/
	
	public String getName() {
		return name;
	}
	
	public double getP() {
		return power;
	}
	public void setP(double p) {
		power = p;		
	}
	
	public double getQ() {
		return reactivePower;
	}
	public void setQ(double q) {
		reactivePower = q;		
	}
	
	public double getV() {
		return voltage;
	}
	public void setV(double v) {
		voltage = v;		
	}
	
	public double getDelta() {
		return angle;
	}
	public void setDelta(double delta) {
		angle = delta;		
	}
	
	/*
	public boolean isSolved() {
		return solved;
	}
	public void setSolved() {
		solved = true;
	}
	*/
	
	public int getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
    public boolean equals(Object o) { 
		if (this == o) { 
            return true; 
		}
           
        if (o == null || o.getClass()!= this.getClass()) 
            return false; 
          
        Bus other = (Bus) o; 
           
        return (name == other.getName()); 
    }
}
