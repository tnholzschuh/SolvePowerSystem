package com.tylerholzschuh.SolvePowerSystem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.complex.Complex;

import com.tylerholzschuh.PowerSystem.*;

public class GeneratePowerSystemFromFile {
	static PowerSystem generate(String filename) throws IOException {
		final double defaultP = 1;
		final double defaultQ = 0;
		final double defaultV = 1;
		final double defaultDelta = 0;
		
		PowerSystem powerSystem = new PowerSystem(.001);		
		String line;
    	String csvSplitBy = ",";
    	String[] splitLine;
    	
    	String nameString, PString, QString, VString, deltaString;
    	
    	Bus newBus;
    	
    	try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
    		br.readLine();
    		
            while (!"".equals((splitLine = br.readLine().split(csvSplitBy,-1))[0])) {

            	
            	nameString = splitLine[0].trim();
            	PString = splitLine[1].trim();
            	QString = splitLine[2].trim();
            	VString = splitLine[3].trim();
            	deltaString = splitLine[4].trim();
                
                if ("".equals(PString) && "".equals(QString)) {
                	// Vdelta Bus             	
                	newBus = new Bus(nameString, 
                			         Bus.VDelta,
                			         defaultP,
                			         defaultQ,
                			         Double.parseDouble(VString),
                			         Double.parseDouble(deltaString));
                } else if ("".equals(QString) && "".equals(deltaString)) {
                	// PV Bus
                	newBus = new Bus(nameString, 
       			                     Bus.PV,
       			                     Double.parseDouble(PString),
       		            	         defaultQ,
       		            	         Double.parseDouble(VString),
       		            	         defaultDelta);
                } else {
                	// PQ Bus
                   	newBus = new Bus(nameString, 
       			                     Bus.PQ,
       			                     Double.parseDouble(PString),
       			                     Double.parseDouble(QString),
       		            	         defaultV,
       		            	         defaultDelta);
                }
                powerSystem.addBus(newBus);                
            }
            
            br.readLine();
            
            Complex c;
            
            while ((line = br.readLine()) != null) {
                splitLine = line.split(csvSplitBy);
                c = parseComplex(splitLine[2]);
                
                powerSystem.addLine(splitLine[0], splitLine[1], c);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return powerSystem;
	}
	
	static Complex parseComplex(String complexString) {
		complexString = complexString.replaceAll("\\s", "");
		Pattern complexP = Pattern.compile("([\\d.]*)[+]([\\d.]*)[ij]");
		Pattern imaginaryP = Pattern.compile("([\\d.]*)[ij]");
		Pattern realP = Pattern.compile("([\\d.]*)");
		
		Matcher mComplex = complexP.matcher(complexString);
		Matcher mImaginary = imaginaryP.matcher(complexString);
		Matcher mReal = realP.matcher(complexString);
		
		Double re, im;
				
		if (mComplex.matches()) {
			re = Double.parseDouble(mComplex.group(1));
			im = Double.parseDouble(mComplex.group(2));
			return new Complex(re, im);
		} else if (mImaginary.matches()) {
			im = Double.parseDouble(mImaginary.group(1));
			return new Complex(0, im);
		} else if (mReal.matches()) {
			re = Double.parseDouble(mReal.group(1));
			return new Complex(re, 0);
		} else {
			return new Complex(0, 0);
		}
	}
}
