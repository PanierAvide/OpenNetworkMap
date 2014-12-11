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

package info.pavie.opennetworkmap.model.draw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import info.pavie.opennetworkmap.model.draw.network.Canvas;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

//TODO Complete tests, with layers
/**
 * Test class for {@link Canvas}
 */
public class TestCanvas {
//ATTRIBUTES
	private Canvas c1, c2, c3, c4;
	private Vertex v1_1, v1_2, v1_3, v2_1, v2_2, v2_3, v3_1, v3_2, v3_3,
					v4_1, v4_2, v4_3, v4_4, v4_5, v4_6, v4_7, v4_8, v4_9,
					v4_10, v4_11, v4_12, v4_13, v4_14, v4_15;
	private Edge e1_1, e1_2, e3_1, e3_2;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		//Canvas 1
		v1_1 = new Vertex("v1_1", null, 0, 0);
		v1_2 = new Vertex("v1_2", null, 0, 1);
		v1_3 = new Vertex("v1_3", null, 1, 1);
		e1_1 = new Edge("e1_1", null, v1_1, v1_3);
		e1_2 = new Edge("e1_2", null, v1_3, v1_2);
		c1 = new Canvas(v1_1);
		c1.add(v1_2, v1_1);
		
		//Canvas 2
		v2_1 = new Vertex("v2_1", null, 0, -5);
		v2_2 = new Vertex("v2_2", null, 1, -5);
		v2_3 = new Vertex("v2_3", null, 0, -4);
		c2 = new Canvas(v2_1);
		c2.add(v2_2, v2_1);
		c2.add(v2_3, v2_1);
		c2.setLastLayerDone();
		c2.addLayer(v1_1);
		c2.add(v1_2, v1_1);
		c2.add(v1_3, v1_1);
		c2.setLastLayerDone();
		
		//Canvas 3
		v3_1 = new Vertex("v3_1", null, 5, 0);
		v3_2 = new Vertex("v3_2", null, 6, 0);
		v3_3 = new Vertex("v3_3", null, 5, 1);
		e3_1 = new Edge("e3_1", null, v3_1, v3_2);
		e3_2 = new Edge("e3_2", null, v3_2, v3_3);
		c3 = new Canvas(v3_1);
		c3.add(v3_2, v3_1);
		c3.add(v3_3, v3_1);
		c3.setLastLayerDone();
		c3.addLayer(v1_1);
		c3.add(v1_2, v1_1);
		c3.add(v1_3, v1_1);
		c3.setLastLayerDone();
		
		//Canvas 4
		v4_1 = new Vertex("v4_1", null, 0, 0);
		v4_2 = new Vertex("v4_2", null, 0, 1);
		
		v4_3 = new Vertex("v4_3", null, 0, 4);
		v4_4 = new Vertex("v4_4", null, 1, 5);
		v4_5 = new Vertex("v4_5", null, 0, 5);
		
		v4_6 = new Vertex("v4_6", null, 2, 0);
		v4_7 = new Vertex("v4_7", null, 3, 0);
		v4_8 = new Vertex("v4_8", null, 4, 1);
		v4_9 = new Vertex("v4_9", null, 3, 2);
		v4_10 = new Vertex("v4_10", null, 4, 2);
		v4_11 = new Vertex("v4_11", null, 2, 3);
		
		v4_12 = new Vertex("v4_12", null, 2, 4);
		v4_13 = new Vertex("v4_13", null, 3, 4);
		v4_14 = new Vertex("v4_14", null, 4, 4);
		v4_15 = new Vertex("v4_15", null, 5, 4);
		
		c4 = new Canvas(v4_1);
		c4.add(v4_2, v4_1);
		c4.setLastLayerDone();
		
		c4.addLayer(v4_6);
		c4.add(v4_7, v4_6);
		c4.add(v4_8, v4_7);
		c4.add(v4_9, v4_8);
		c4.add(v4_10, v4_9);
		c4.add(v4_11, v4_10);
		c4.setLastLayerDone();
		
		c4.addLayer(v4_3);
		c4.add(v4_4, v4_3);
		c4.add(v4_5, v4_4);
		c4.setLastLayerDone();
		
		c4.addLayer(v4_12);
		c4.add(v4_13, v4_12);
		c4.add(v4_14, v4_13);
		c4.add(v4_15, v4_14);
		c4.setLastLayerDone();
	}

//TESTS
// Constructor
	@Test
	public void testCanvas() {
		assertEquals(v1_1, c1.getVertex(0, 0));
	}
	
	@Test(expected=NullPointerException.class)
	public void testCanvasNullVertex() {
		c1 = new Canvas(null);
	}

// getVertex()
	@Test
	public void testGetVertexOneLayer() {
//		System.out.println(c1);
		assertEquals(v1_1, c1.getVertex(0, 0));
		assertEquals(v1_2, c1.getVertex(0, 1));
	}
	
	@Test
	public void testGetVertexTwoLayersInLine() {
		assertEquals(v2_1, c2.getVertex(0, 0));
		assertEquals(v2_2, c2.getVertex(1, 0));
		assertEquals(v2_3, c2.getVertex(0, 1));
		assertEquals(v1_1, c2.getVertex(0, 2));
		assertEquals(v1_2, c2.getVertex(0, 3));
		assertEquals(v1_3, c2.getVertex(1, 3));
		assertNull(c2.getVertex(1, 1));
		assertNull(c2.getVertex(1, 2));
	}
	
	@Test
	public void testGetVertexTwoLayersInColumn() {
		assertEquals(v3_1, c3.getVertex(2, 0));
		assertEquals(v3_2, c3.getVertex(3, 0));
		assertEquals(v3_3, c3.getVertex(2, 1));
		assertEquals(v1_1, c3.getVertex(0, 0));
		assertEquals(v1_2, c3.getVertex(0, 1));
		assertEquals(v1_3, c3.getVertex(1, 1));
		assertNull(c3.getVertex(1, 0));
		assertNull(c3.getVertex(3, 1));
	}
	
	@Test
	public void testGetVertexFourSquare() {
//		System.out.println(c4);
		assertEquals(15, c4.getVerticesPositions().size());
		assertEquals(v4_1, c4.getVertex(0, 0));
		assertEquals(v4_2, c4.getVertex(0, 1));
		assertEquals(v4_3, c4.getVertex(0, 4));
		assertEquals(v4_4, c4.getVertex(1, 5));
		assertEquals(v4_5, c4.getVertex(0, 5));
		assertEquals(v4_6, c4.getVertex(2, 0));
		assertEquals(v4_7, c4.getVertex(3, 0));
		assertEquals(v4_8, c4.getVertex(4, 1));
		assertEquals(v4_9, c4.getVertex(3, 2));
		assertEquals(v4_10, c4.getVertex(4, 2));
		assertEquals(v4_11, c4.getVertex(3, 3));
		assertEquals(v4_12, c4.getVertex(2, 4));
		assertEquals(v4_13, c4.getVertex(3, 4));
		assertEquals(v4_14, c4.getVertex(4, 4));
		assertEquals(v4_15, c4.getVertex(5, 4));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetVertexOutBounds() {
		c1.getVertex(1,0);
	}

// add()
	@Test
	public void testAddOneLayerTop() {
		c1.add(v1_3, v1_2);
		assertEquals(v1_1, c1.getVertex(0, 0));
		assertEquals(v1_2, c1.getVertex(0, 1));
		assertEquals(v1_3, c1.getVertex(1, 1));
		assertNull(c1.getVertex(1, 0));
	}

// testGetVerticesPositions()
	@Test
	public void testGetVerticesPositions() {
		Map<Vertex,int[]> vertPos = c3.getVerticesPositions();
		assertEquals(2, vertPos.get(v3_1)[0]);
		assertEquals(0, vertPos.get(v3_1)[1]);
		
		assertEquals(3, vertPos.get(v3_2)[0]);
		assertEquals(0, vertPos.get(v3_2)[1]);
		
		assertEquals(2, vertPos.get(v3_3)[0]);
		assertEquals(1, vertPos.get(v3_3)[1]);
		
		assertEquals(0, vertPos.get(v1_1)[0]);
		assertEquals(0, vertPos.get(v1_1)[1]);
		
		assertEquals(0, vertPos.get(v1_2)[0]);
		assertEquals(1, vertPos.get(v1_2)[1]);
		
		assertEquals(1, vertPos.get(v1_3)[0]);
		assertEquals(1, vertPos.get(v1_3)[1]);
	}

// testGetEdgesPositions()
	@Test
	public void testGetEdgesPositions() {
		Map<Edge,int[]> edgePos = c3.getEdgesPositions();
		assertEquals(2, edgePos.get(e3_1)[0]);
		assertEquals(0, edgePos.get(e3_1)[1]);
		assertEquals(3, edgePos.get(e3_1)[2]);
		assertEquals(0, edgePos.get(e3_1)[3]);
		
		assertEquals(3, edgePos.get(e3_2)[0]);
		assertEquals(0, edgePos.get(e3_2)[1]);
		assertEquals(2, edgePos.get(e3_2)[2]);
		assertEquals(1, edgePos.get(e3_2)[3]);
		
		assertEquals(0, edgePos.get(e1_1)[0]);
		assertEquals(0, edgePos.get(e1_1)[1]);
		assertEquals(1, edgePos.get(e1_1)[2]);
		assertEquals(1, edgePos.get(e1_1)[3]);
		
		assertEquals(1, edgePos.get(e1_2)[0]);
		assertEquals(1, edgePos.get(e1_2)[1]);
		assertEquals(0, edgePos.get(e1_2)[2]);
		assertEquals(1, edgePos.get(e1_2)[3]);
	}

// testGetComponentStyle()
	@Test
	public void testGetComponentStyle() {
		fail("Not yet implemented"); // TODO
	}

// testGetCanvasStyle()
	@Test
	public void testGetCanvasStyle() {
		fail("Not yet implemented"); // TODO
	}

// testGetEdgeDirection()
	@Test
	public void testGetEdgeDirection() {
		assertEquals(Math.PI/4, c3.getEdgeDirection(e1_1), 0);
		assertEquals(-Math.PI/2, c3.getEdgeDirection(e1_2), 0);
		assertEquals(Math.PI/2, c3.getEdgeDirection(e3_1), 0);
		assertEquals(-Math.PI/4, c3.getEdgeDirection(e3_2), 0);
	}

// testDefineStyle()
	@Test
	public void testDefineStyle() {
		fail("Not yet implemented"); // TODO
	}

// addLayer() / setLastLayerDone()
	@Test
	public void testAddLayerAndSetLastLayerDone() {
		Vertex vc_1 = new Vertex("v1", null, 0, 0);
		Vertex vc_2 = new Vertex("v2", null, 0, 1);
		Canvas c = new Canvas(vc_1);
		c.setLastLayerDone();
		c.addLayer(vc_2);
		c.setLastLayerDone();
		assertEquals(vc_1, c.getVertex(0, 0));
		assertEquals(vc_2, c.getVertex(0, 1));
	}

// toString()
//	@Test
//	public void testToString() {
//		fail("Not yet implemented"); // TODO
//	}
}
