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

import static org.junit.Assert.*;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestVertex {
//ATTRIBUTES
	private Vertex v1, v2, v3, v4;
	private Edge e1, e2;
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
		
		v1 = new Vertex("v1", t1, 15.0, 42.0);
		v2 = new Vertex("v2", t2, 11.7, 37.5);
		v3 = new Vertex("v1", t3, 15.0, 42.0);
		v4 = new Vertex("v4", null, 0, 0);
		
		e1 = new Edge("e1", null, v1, v2);
		e2 = new Edge("e2", null, v4, v1);
	}

//TESTS
// hashCode()
	@Test
	public void testHashCodeEquals() {
		assertEquals(v1.hashCode(), v3.hashCode());
	}
	
	@Test
	public void testHashCodeDifferent() {
		assertNotEquals(v1.hashCode(), v2.hashCode());
	}

// equals()
	@Test
	public void testEqualsTrue() {
		assertTrue(v1.equals(v3));
	}
	
	@Test
	public void testEqualsFalse() {
		assertFalse(v1.equals(v2));
	}

// getLatitude()
	@Test
	public void testGetLatitude() {
		assertEquals(15.0, v1.getLatitude(), 0);
	}

// getLongitude()
	@Test
	public void testGetLongitude() {
		assertEquals(37.5, v2.getLongitude(), 0);
	}

// getLinkedVertices()
	@Test
	public void testGetLinkedVerticesNone() {
		assertEquals(0, v3.getLinkedVertices().size());
	}
	
	@Test
	public void testGetLinkedVerticesMore() {
		assertEquals(2, v1.getLinkedVertices().size());
		assertTrue(v1.getLinkedVertices().contains(v2));
		assertTrue(v1.getLinkedVertices().contains(v4));
	}

// getStartOf()
	@Test
	public void testGetStartOfNone() {
		assertEquals(0, v3.getStartOf().size());
	}
	
	@Test
	public void testGetStartOfMore() {
		assertEquals(1, v1.getStartOf().size());
//		System.out.println(v1.getStartOf());
		assertTrue(v1.getStartOf().contains(e1));
	}

// getEndOf()
	@Test
	public void testGetEndOfNone() {
		assertEquals(0, v3.getEndOf().size());
	}
	
	@Test
	public void testGetEndOfMore() {
		assertEquals(1, v1.getEndOf().size());
		assertTrue(v1.getEndOf().contains(e2));
	}

// toString()
	@Test
	public void testToString() {
		assertEquals("Vertex v1", v1.toString());
	}

// setStartOf()
	@Test
	public void testSetStartOfPastNone() {
		v3.setStartOf(e2);
		assertTrue(v3.getStartOf().contains(e2));
	}
	
	@Test
	public void testSetStartOfPastEdge() {
		v1.setStartOf(e2);
		assertTrue(v1.getStartOf().contains(e2));
	}

// setEndOf()
	@Test
	public void testSetEndOfPastNone() {
		v3.setEndOf(e2);
		assertTrue(v3.getEndOf().contains(e2));
	}
	
	@Test
	public void testSetEndOfPastEdge() {
		v1.setEndOf(e1);
		assertTrue(v1.getEndOf().contains(e1));
	}

// unsetStartOf()
	@Test
	public void testUnsetStartOf() {
		v1.unsetStartOf(e1);
		assertFalse(v1.getStartOf().contains(e1));
	}

// unsetEndOf()
	@Test
	public void testUnsetEndOf() {
		v1.unsetEndOf(e2);
		assertFalse(v1.getEndOf().contains(e2));
	}

}
