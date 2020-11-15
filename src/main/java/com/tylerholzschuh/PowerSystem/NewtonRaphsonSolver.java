package com.tylerholzschuh.PowerSystem;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class NewtonRaphsonSolver {
	public static void solve(NewtonRaphsonCapable nrc) {
		RealMatrix jacobian = new Array2DRowRealMatrix(nrc.getNumberOfInputs(), nrc.getNumberOfInputs());
		RealVector inputErrorVector;
		RealVector outputErrorVector;
		DecompositionSolver solver;

		
		while (nrc.getMaxErrorRatio() > nrc.getDesiredMaxErrorRatio()) {
			generateJacobian(jacobian, nrc);
			
			inputErrorVector = new ArrayRealVector(nrc.getInputErrorVector());
			solver = new LUDecomposition(jacobian).getSolver();
			outputErrorVector = solver.solve(inputErrorVector);
			
			nrc.updateOutputs(outputErrorVector.toArray());			
		}
		
		nrc.finish();
	}
	
	private static void generateJacobian(RealMatrix jacobian, NewtonRaphsonCapable nrc) {
		for (int i = 0; i < jacobian.getRowDimension(); i++) {
			for (int j = 0; j < jacobian.getColumnDimension(); j++) {
				jacobian.setEntry(i, j, nrc.jacobianEntry(i, j));
			}
		}
	}
}
