package com.tylerholzschuh.SolvePowerSystem;

import java.io.IOException;
import java.util.List;

import com.tylerholzschuh.PowerSystem.Bus;
import com.tylerholzschuh.PowerSystem.NewtonRaphsonSolver;
import com.tylerholzschuh.PowerSystem.PowerSystem;

/**
 * Hello world!
 *
 */
public class SolvePowerSystem {
    public static void main( String[] args ) {
    	PowerSystem ps;
    	
    	try {
    		ps = GeneratePowerSystemFromFile.generate(args[0]);
    	} catch (IOException | IndexOutOfBoundsException e) {
    		System.out.println("Please enter a valid filename as the first argument");
    		return;
    	}
    	
    	NewtonRaphsonSolver.solve(ps);
    	printBuses(ps.getBuses());
    }
    
    static void printBuses(List<Bus> busList) {
    	System.out.printf("Outputing%n%n");
    	
    	for (Bus bus : busList) {
    		System.out.println(bus.getName());
    		System.out.println("P: " + bus.getP());
    		System.out.println("Q: " + bus.getQ());
    		System.out.println("V: " + bus.getV());
    		System.out.println("delta: " + bus.getDelta());
    		System.out.println();
    	}
    }
}
