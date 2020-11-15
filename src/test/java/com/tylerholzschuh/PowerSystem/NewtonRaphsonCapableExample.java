package com.tylerholzschuh.PowerSystem;

public class NewtonRaphsonCapableExample implements NewtonRaphsonCapable {
	final double desiredMaxError = 0.005;
	public double[][] jacobian;
	public double[] outputs;
	public double[] inputs;
	
	public NewtonRaphsonCapableExample() {
		jacobian = new double[2][2];
		jacobian[0][0] = 1;
		jacobian[0][1] = 2;
		jacobian[1][0] = 4;
		jacobian[1][1] = 5;
		
		inputs = new double[2];
		inputs[0] = 3;
		inputs[1] = 6;
		
		outputs = new double[2];
		outputs[0] = 0;
		outputs[1] = 0;
	}
	
	@Override
	public int getNumberOfInputs() {
		return 2;
	}

	@Override
	public int getNumberOfOutputs() {
		return 2;
	}

	@Override
	public double[] getInputErrorVector() {
		double[] inputErrorVector = {3.0-1*outputs[0]-2*outputs[1], 6.0-4*outputs[0]-5*outputs[1]};
		return inputErrorVector;
	}

	@Override
	public double jacobianEntry(int row, int col) {		
		return jacobian[row][col];
	}

	@Override
	public void updateOutputs(double[] outputError) {
		for (int i = 0; i < getNumberOfOutputs(); i++) {
			outputs[i] += outputError[i];
		}
	}

	@Override
	public double getMaxErrorRatio() {
		double[] errors = getInputErrorVector();
		double maxErrorRatio = 0;
		
		for (int i = 0; i < getNumberOfInputs(); i++) {
			maxErrorRatio = Math.max(maxErrorRatio, errors[i]/inputs[i]);
		}
		return maxErrorRatio;
	}

	@Override
	public double getDesiredMaxErrorRatio() {
		return desiredMaxError;
	}

}
