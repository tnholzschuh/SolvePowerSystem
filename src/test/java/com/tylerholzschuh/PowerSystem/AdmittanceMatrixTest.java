package com.tylerholzschuh.PowerSystem;

import static org.junit.Assert.*;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexFormat;
import org.junit.Test;

public class AdmittanceMatrixTest {

	@Test
	public void passesSmallExample() {
		String admittanceString = ".05+.06i";
		AdmittanceMatrix admittanceMatrix = new AdmittanceMatrix();
		
		Bus[] buses = {new Bus("First Bus", Bus.VDelta, 0, 0, 0, 0),
		               new Bus("Second Bus", Bus.PQ, 0, 0, 0, 0)};
		
		ComplexFormat complexFormat = new ComplexFormat();
		Complex y = complexFormat.parse(admittanceString);
				
		Complex[][] calculatedValues = new Complex[2][2];
		Complex[][] correctValues = { {
			                           y,
				                       y.negate()
				                      }, 
				                    { 
				                       y.negate(),
							           y 
				                    } };
				
		admittanceMatrix.addLine(buses[0], buses[1], complexFormat.parse(admittanceString));
				
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				calculatedValues[i][j] = admittanceMatrix.getComplexElement(buses[i], buses[j]);
								
				assertTrue(Math.abs(calculatedValues[i][j].getReal()-correctValues[i][j].getReal()) < .001);
				assertTrue(Math.abs(calculatedValues[i][j].getImaginary()-correctValues[i][j].getImaginary()) < .001);				
			}
		}
	}

}
