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

package info.pavie.opennetworkmap.model.draw.network;

import static org.junit.Assert.assertEquals;
import info.pavie.opennetworkmap.model.draw.network.Layer;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestLayer {
//ATTRIBUTES
	private Layer l1;
	private Vertex v1, v2, v3, v4, v5;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		v1 = new Vertex("v1", null, 0, 0);
		v2 = new Vertex("v2", null, 0, 1);
		v3 = new Vertex("v3", null, 0, 2);
		v4 = new Vertex("v4", null, 0, 1.5);
		v5 = new Vertex("v5", null, 1, 0);
		l1 = new Layer(v1);
		l1.add(v2, v1);
		l1.add(v3, v1);
	}

//TESTS
	@Test
	public void testGetWidth() {
		assertEquals(3, l1.getWidth());
	}

	@Test
	public void testGetHeight() {
		assertEquals(1, l1.getHeight());
	}

	@Test
	public void testGetVertex() {
		assertEquals(v1, l1.getVertex(0, 0));
		assertEquals(v2, l1.getVertex(0, 1));
		assertEquals(v3, l1.getVertex(0, 2));
	}

	@Test
	public void testGetVerticesPositions() {
		l1.add(v5, v1);
		Map<Vertex,int[]> verticesPositions = l1.getVerticesPositions();
		assertEquals(0, verticesPositions.get(v1)[0]);
		assertEquals(0, verticesPositions.get(v1)[1]);
		assertEquals(0, verticesPositions.get(v2)[0]);
		assertEquals(1, verticesPositions.get(v2)[1]);
		assertEquals(0, verticesPositions.get(v3)[0]);
		assertEquals(2, verticesPositions.get(v3)[1]);
		assertEquals(1, verticesPositions.get(v5)[0]);
		assertEquals(0, verticesPositions.get(v5)[1]);
	}

	@Test
	public void testGetLatitude() {
		assertEquals(0, l1.getLatitude(), 0);
	}

	@Test
	public void testGetLongitude() {
		assertEquals(1, l1.getLongitude(), 0);
	}

	@Test
	public void testAdd() {
		l1.add(v4, v1);
		assertEquals(v1, l1.getVertex(0, 0));
		assertEquals(v2, l1.getVertex(0, 1));
		assertEquals(v4, l1.getVertex(0, 2));
		assertEquals(v3, l1.getVertex(0, 3));
	}
	
//	@Test
//	public void testGetEdgeDirection() {
//		l1.add(v5, v1);
//		
////		System.out.println(l1);
//		Edge e1 = new Edge("e1", null, v1, v2);
//		assertEquals(0, l1.getEdgeDirection(e1), 0);
//		
//		Edge e2 = new Edge("e2", null, v1, v5);
//		assertEquals(Math.PI/2, l1.getEdgeDirection(e2), 0);
//		
//		Edge e3 = new Edge("e3", null, v5, v1);
//		assertEquals(-Math.PI/2, l1.getEdgeDirection(e3), 0);
//	}
}
