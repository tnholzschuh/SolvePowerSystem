package com.tylerholzschuh.PowerSystem;

public interface NewtonRaphsonCapable {
	int getNumberOfInputs();
	
	int getNumberOfOutputs();
	
	double[] getInputErrorVector();
	
	double jacobianEntry(int row, int col);
	
	void updateOutputs(double[] outputError);
	
	double getMaxErrorRatio();
	
	double getDesiredMaxErrorRatio();
	
	default void finish() {
		return;
	}
}
