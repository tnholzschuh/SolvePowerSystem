package com.tylerholzschuh.PowerSystem;

import static org.junit.Assert.*;

import org.apache.commons.math3.complex.ComplexFormat;
import org.junit.Before;
import org.junit.Test;

public class PowerSystemTest {
	static PowerSystem ps;
	static Bus bus1, bus2;
	
	@Before
	public void resetBeforeEachTest() {
		ps = new PowerSystem(.001);
		ComplexFormat complexFormat = new ComplexFormat();
		bus1 = new Bus("Generator", Bus.VDelta, 0.1, 0.1, 1, 0);
		bus2 = new Bus("Load", Bus.PQ, 1, 1, 0.1, 0);
		
		ps.addBus(bus1);
		ps.addBus(bus2);
		ps.addLine(bus1, bus2, complexFormat.parse(".1i"));
	}
	
	@Test
	public void testJacobian() {
		double[][] correctJacobianValues =  {{1, 0}, {0, -.1}};
		
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				assertTrue(Math.abs(ps.jacobianEntry(i, j)-
						            correctJacobianValues[i][j]) < .001);
			}
		}
	}
	
	@Test
	public void testGetNumberOfInputs() {
		assertEquals(ps.getNumberOfInputs(), 2);
	}
	
	@Test
	public void testGetNumberOfOutputs() {
		assertEquals(ps.getNumberOfOutputs(), 2);
	}
	
	@Test
	public void testGetInputErrorVector() {
		double[] correctInputErrorVector = {1.9, 1.0};
		double[] calculatedInputErrorVector = ps.getInputErrorVector();
		
		assertTrue(Math.abs(correctInputErrorVector[0]-calculatedInputErrorVector[0]) < .001);
		assertTrue(Math.abs(correctInputErrorVector[1]-calculatedInputErrorVector[1]) < .001);
	}
	
	@Test
	public void testUpdateOutputs() {
		double[] outputChange = {.1, .25};
		double[] correctFinal = {.2, .25};
		
		ps.updateOutputs(outputChange);
		
		assertTrue(Math.abs(bus2.getV()-correctFinal[0]) < .001);
		assertTrue(Math.abs(bus2.getDelta()-correctFinal[1]) < .001);
	}
	
	@Test
	public void testGetMaxErrorRatio() {
		assertTrue(Math.abs(ps.getMaxErrorRatio()-1.9) < .001);
	}
	

}
