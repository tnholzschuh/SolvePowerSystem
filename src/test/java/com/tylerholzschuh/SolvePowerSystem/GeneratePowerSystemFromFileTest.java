package com.tylerholzschuh.SolvePowerSystem;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.complex.Complex;
import org.junit.Test;

import com.tylerholzschuh.PowerSystem.*;

/**
 * Unit test for simple App.
 */
public class GeneratePowerSystemFromFileTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void generateTest()
    {
    	PowerSystem ps;
    	List<Bus> buses;
    	
    	try {
			ps = GeneratePowerSystemFromFile.generate("src/test/java/com/tylerholzschuh/SolvePowerSystem/Simple System.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}    	
    	
    	try {
    		NewtonRaphsonSolver.solve(ps);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	buses = ps.getBuses();
    	    	
    	assertTrue(Math.abs(-1-ps.getCalculatedPAtBus(buses.get(1))) < .001);
    	assertTrue(Math.abs(-.2-ps.getCalculatedQAtBus(buses.get(1))) < .001);
    	assertTrue(Math.abs(.97419-buses.get(1).getV()) < .001);
    	assertTrue(Math.abs(-.10282-buses.get(1).getDelta()) < .001);
    }
    
    @Test
    public void testParseComplex() {
    	Complex c1 = GeneratePowerSystemFromFile.parseComplex("1.1+2.1i");
    	Complex c2 = GeneratePowerSystemFromFile.parseComplex("2.0i");
    	Complex c3 = GeneratePowerSystemFromFile.parseComplex("1.0");

    	assertTrue(Math.abs(c1.getReal()-1.1) < .001);
    	assertTrue(Math.abs(c1.getImaginary()-2.1) < .001);
    	assertTrue(Math.abs(c2.getReal()) < .001);
    	assertTrue(Math.abs(c2.getImaginary()-2) < .001);
    	assertTrue(Math.abs(c3.getReal()-1) < .001);
    	assertTrue(Math.abs(c3.getImaginary()) < .001);
    }
}
