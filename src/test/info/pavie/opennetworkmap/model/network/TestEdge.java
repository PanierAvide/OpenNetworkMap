/*
	Copyright 2014 Adrien PAVIE
	
	This file is part of OpenNetworkMap.
	
	OpenNetworkMap is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	
	OpenNetworkMap is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with OpenNetworkMap. If not, see <http://www.gnu.org/licenses/>.
 */

package info.pavie.opennetworkmap.model.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestEdge {
//ATTRIBUTES
	private Edge e1, e2, e3, e4, e5;
	private Vertex v1, v2, v3, v4;
	private Map<String,String> t1, t2, t3;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		t1 = new HashMap<String,String>(1);
		t1.put("type", "first");
		
		t2 = new HashMap<String,String>(1);
		t2.put("type", "second");
		
		t3 = new HashMap<String,String>(1);
		t3.put("type", "first");
		
		v1 = new Vertex("v1", t1, 0, 0);
		v2 = new Vertex("v2", t2, 1, 1);
		v3 = new Vertex("v1", t3, 0, 0);
		v4 = new Vertex("v2", t2, 1, 1);
		
		e1 = new Edge("e1", t1, v1, v2);
		e2 = new Edge("e1", t3, v3, v4);
		e3 = new Edge("e3", t2, v1, v3);
	}

//TESTS
// constructor
	@Test
	public void testConstructorAddTwoSimilarEdges() {
		e4 = new Edge("e4", t3, v3, v4);
		e5 = new Edge("e5", t2, v1, v3);
		assertEquals(2, v3.getStartOf().size());
		assertEquals(3, v1.getStartOf().size());
		assertEquals(2, v4.getEndOf().size());
		assertEquals(2, v3.getEndOf().size());
	}
	
// hashCode()
	@Test
	public void testHashCodeEquals() {
		assertEquals(e1.hashCode(), e2.hashCode());
	}
	
	@Test
	public void testHashCodeDifferent() {
		assertNotEquals(e1.hashCode(), e3.hashCode());
	}

// equals()
	@Test
	public void testEqualsObject() {
		assertTrue(e1.equals(e2));
	}
	
	@Test
	public void testEqualsDifferent() {
		assertFalse(e1.equals(e3));
	}

// getStartVertex()
	@Test
	public void testGetStartVertex() {
		assertEquals(v1, e1.getStartVertex());
	}

// getEndVertex()
	@Test
	public void testGetEndVertex() {
		assertEquals(v2, e1.getEndVertex());
	}

// toString()
	@Test
	public void testToString() {
		assertEquals("Edge e1", e1.toString());
	}

// setStartVertex()
	@Test
	public void testSetStartVertex() {
		e1.setStartVertex(v2);
		assertEquals(v2, e1.getStartVertex());
		assertTrue(v2.getStartOf().contains(e1));
	}

// setEndVertex()
	@Test
	public void testSetEndVertex() {
		e1.setEndVertex(v1);
		assertEquals(v1, e1.getEndVertex());
		assertTrue(v1.getEndOf().contains(e1));
	}

}
