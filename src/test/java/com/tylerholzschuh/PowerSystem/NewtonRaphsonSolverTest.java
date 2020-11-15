package com.tylerholzschuh.PowerSystem;

import static org.junit.Assert.*;

import org.junit.Test;

public class NewtonRaphsonSolverTest {

	@Test
	public void test() {
		NewtonRaphsonCapableExample nrc = new NewtonRaphsonCapableExample();
		
		NewtonRaphsonSolver.solve(nrc);
		
		assertTrue(Math.abs(nrc.outputs[0]+1) < .001);
		assertTrue(Math.abs(nrc.outputs[1]-2) < .001);
	}

}
