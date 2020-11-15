package com.tylerholzschuh.PowerSystem;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class BusTest {

	@Test
	public void test() {
		Bus bus = new Bus("Ground", Bus.VDelta, 0, 0, 0, 0);
		Set<Bus> s = new HashSet<Bus>();
		s.add(bus);
		assertTrue(s.contains(bus));
	}

}
