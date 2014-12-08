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
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import org.junit.Before;
import org.junit.Test;

public class TestNetwork {
//ATTRIBUTES
	private Network n1, n2;
	private Vertex v1_1, v1_2, v1_3, v1_4, v1_5, v1_6, v2_1, v2_2;
	private Edge e1_1, e1_2, e1_3, e1_4, e1_5, e2_1;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		//Network 1
		n1 = new Network();
		v1_1 = new Vertex("v1", null, 0, 0);
		v1_2 = new Vertex("v2", null, 0, 1);
		v1_3 = new Vertex("v3", null, 1, 0);
		v1_4 = new Vertex("v4", null, 1, 1);
		v1_5 = new Vertex("v5", null, -1, 0);
		v1_6 = new Vertex("v6", null, -1, -1);
		
		e1_1 = new Edge("e1", null, v1_1, v1_2);
		e1_2 = new Edge("e2", null, v1_3, v1_1);
		e1_3 = new Edge("e3", null, v1_1, v1_5);
		e1_4 = new Edge("e4", null, v1_4, v1_2);
		e1_5 = new Edge("e5", null, v1_2, v1_6);
		
		n1.addEdge(e1_1);
		n1.addEdge(e1_2);
		n1.addEdge(e1_3);
		n1.addEdge(e1_4);
		n1.addEdge(e1_5);
		
		//Network 2
		n2 = new Network();
		v2_1 = new Vertex("v1", null, 0, 0);
		v2_2 = new Vertex("v2", null, 0, 1);
		e2_1 = new Edge("e1", null, v2_1, v2_2);
	}

//TESTS
	@Test
	public void testGetEdges() {
		assertEquals(5, n1.getEdges().size());
		assertTrue(n1.getEdges().contains(e1_1));
		assertTrue(n1.getEdges().contains(e1_2));
		assertTrue(n1.getEdges().contains(e1_3));
		assertTrue(n1.getEdges().contains(e1_4));
		assertTrue(n1.getEdges().contains(e1_5));
	}

	@Test
	public void testGetVertices() {
		assertEquals(6, n1.getVertices().size());
		assertTrue(n1.getVertices().contains(v1_1));
		assertTrue(n1.getVertices().contains(v1_2));
		assertTrue(n1.getVertices().contains(v1_3));
		assertTrue(n1.getVertices().contains(v1_4));
		assertTrue(n1.getVertices().contains(v1_5));
		assertTrue(n1.getVertices().contains(v1_6));
	}

	@Test
	public void testAddEdge() {
		assertEquals(0, n2.getEdges().size());
		assertEquals(0, n2.getVertices().size());
		n2.addEdge(e2_1);
		assertEquals(1, n2.getEdges().size());
		assertEquals(2, n2.getVertices().size());
		assertTrue(n2.getEdges().contains(e2_1));
		assertTrue(n2.getVertices().contains(v2_1));
		assertTrue(n2.getVertices().contains(v2_2));
	}

	@Test
	public void testSimplifyEdge() {
		n1.simplifyEdge(e1_1);
		assertFalse(n1.getEdges().contains(e1_1));
//		System.out.println(v1_1.getStartOf());
		assertEquals(2, v1_1.getStartOf().size());
		assertEquals(2, v1_1.getEndOf().size());
		assertEquals(v1_1, e1_4.getEndVertex());
		assertEquals(v1_1, e1_5.getStartVertex());
		assertFalse(v1_1.getLinkedVertices().contains(v1_2));
		assertFalse(v1_4.getLinkedVertices().contains(v1_2));
		assertFalse(v1_6.getLinkedVertices().contains(v1_2));
	}

}
