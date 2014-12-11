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
import info.pavie.opennetworkmap.model.draw.network.FlexibleGrid;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestFlexibleGrid {
//ATTRIBUTES
	private FlexibleGrid<Vertex> fg1, fg2;
	private Vertex v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14, v15, v16;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		v1 = new Vertex("v1", null, 0, 0);
		v2 = new Vertex("v2", null, 1, 1);
		v3 = new Vertex("v3", null, 0.5, 0.01);
		v4 = new Vertex("v4", null, 1, 1);
		v5 = new Vertex("v5", null, 0, 1);
		v6 = new Vertex("v6", null, -1, 1);
		v7 = new Vertex("v7", null, 1, -1);
		v8 = new Vertex("v8", null, 0, -1);
		v9 = new Vertex("v9", null, -1, -1);
		v10 = new Vertex("v10", null, -1, 0);
		v11 = new Vertex("v11", null, -0.5, 0.01);
		fg1 = new FlexibleGrid<Vertex>(v1);
		
		//FlexibleGrid 2
		v12 = new Vertex("v12", null, 0, 0);
		v13 = new Vertex("v13", null, 0, 1);
		v14 = new Vertex("v14", null, 0, -2);
		v15 = new Vertex("v15", null, 0, -1);
		v16 = new Vertex("v16", null, 1, -1);
		fg2 = new FlexibleGrid<Vertex>(v12);
	}

//TESTS
// Constructor
	@Test
	public void testFlexibleGrid() {
		assertEquals(v1, fg1.get(0, 0));
	}
	
	@Test(expected=NullPointerException.class)
	public void testFlexibleGridNullVertex() {
		fg1 = new FlexibleGrid<Vertex>(null);
	}

// get()
	@Test
	public void testGetVertex() {
		assertEquals(v1, fg1.get(0, 0));
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testGetVertexOutBounds() {
		fg1.get(1,0);
	}

// add()
	@Test
	public void testAddSimpleTop() {
		v2 = new Vertex("v2", null, 1, 0);
		fg1.add(v2, v1);
		assertEquals(v2, fg1.get(1, 0));
	}

	@Test
	public void testAddSimpleBottom() {
		v2 = new Vertex("v2", null, -1, 0);
		fg1.add(v2, v1);
		assertEquals(v2, fg1.get(0, 0));
		assertEquals(v1, fg1.get(1, 0));
	}
	
	@Test
	public void testAddSimpleLeft() {
		v2 = new Vertex("v2", null, 0, -1);
		fg1.add(v2, v1);
		assertEquals(v2, fg1.get(0, 0));
		assertEquals(v1, fg1.get(0, 1));
	}
	
	@Test
	public void testAddSimpleRight() {
		v2 = new Vertex("v2", null, 0, 1);
		fg1.add(v2, v1);
		assertEquals(v2, fg1.get(0, 1));
		assertEquals(v1, fg1.get(0, 0));
	}
	
	@Test
	public void testAddSimpleTopRight() {
		v2 = new Vertex("v2", null, 1, 1);
		fg1.add(v2, v1);
		assertEquals(v2, fg1.get(1, 1));
		assertEquals(v1, fg1.get(0, 0));
	}
	
	@Test
	public void testAddSimpleBottomLeft() {
		v2 = new Vertex("v2", null, -1, -1);
		fg1.add(v2, v1);
		assertEquals(v1, fg1.get(1, 1));
		assertEquals(v2, fg1.get(0, 0));
	}
	
	@Test
	public void testAddMiddleTop() {
		v2 = new Vertex("v2", null, 1, 0);
		fg1.add(v2, v1);
		fg1.add(v3, v1);
		assertEquals(v3, fg1.get(1, 0));
		assertEquals(v2, fg1.get(2, 0));
		assertEquals(v1, fg1.get(0, 0));
	}
	
	@Test
	public void testAddMiddleBottom() {
		fg1.add(v10, v1);
		fg1.add(v11, v1);
//		System.out.println(c1);
		assertEquals(v11, fg1.get(1, 0));
		assertEquals(v10, fg1.get(0, 0));
		assertEquals(v1, fg1.get(2, 0));
	}
	
	@Test
	public void testAddThreeAsForkBottomRight() {
		fg1.add(v4, v1);
		fg1.add(v5, v1);
		fg1.add(v6, v1);
//		System.out.println(c1);
		assertEquals(v1, fg1.get(1, 0));
		assertEquals(v4, fg1.get(2, 1));
		assertEquals(v5, fg1.get(1, 1));
		assertEquals(v6, fg1.get(0, 1));
	}
	
	@Test
	public void testAddThreeAsForkTopRight() {
		fg1.add(v6, v1);
//		System.out.println(c1);
		fg1.add(v5, v1);
//		System.out.println(c1);
		fg1.add(v4, v1);
//		System.out.println(c1);
		assertEquals(v1, fg1.get(1, 0));
		assertEquals(v4, fg1.get(2, 1));
		assertEquals(v5, fg1.get(1, 1));
		assertEquals(v6, fg1.get(0, 1));
	}
	
	@Test
	public void testAddThreeAsForkBottomLeft() {
		fg1.add(v7, v1);
//		System.out.println(c1);
		fg1.add(v8, v1);
//		System.out.println(c1);
		fg1.add(v9, v1);
//		System.out.println(c1);
		assertEquals(v1, fg1.get(1, 1));
		assertEquals(v7, fg1.get(2, 0));
		assertEquals(v8, fg1.get(1, 0));
		assertEquals(v9, fg1.get(0, 0));
	}
	
	@Test
	public void testAddThreeAsForkTopLeft() {
		fg1.add(v9, v1);
//		System.out.println(c1);
		fg1.add(v8, v1);
//		System.out.println(c1);
		fg1.add(v7, v1);
//		System.out.println(c1);
		assertEquals(v1, fg1.get(1, 1));
		assertEquals(v7, fg1.get(2, 0));
		assertEquals(v8, fg1.get(1, 0));
		assertEquals(v9, fg1.get(0, 0));
	}
	
	@Test
	public void testAddFourInLine() {
		fg2.add(v13, v12);
		fg2.add(v14, v12);
		fg2.add(v15, v14);
//		System.out.println(c2);
		assertEquals(v14, fg2.get(0, 0));
		assertEquals(v15, fg2.get(0, 1));
		assertEquals(v12, fg2.get(0, 2));
		assertEquals(v13, fg2.get(0, 3));
	}

// getWidth()
	@Test
	public void testGetWidth() {
		fg2.add(v13, v12);
		fg2.add(v14, v12);
		fg2.add(v15, v14);
		assertEquals(4, fg2.getWidth());
	}

// getHeight()
	@Test
	public void testGetHeight() {
		fg2.add(v13, v12);
		fg2.add(v14, v12);
		fg2.add(v15, v14);
		assertEquals(1, fg2.getHeight());
	}

// getPositions()
	@Test
	public void testGetPositions() {
		fg2.add(v13, v12);
		fg2.add(v14, v12);
		fg2.add(v15, v14);
		fg2.add(v16,  v15);
		Map<Vertex,int[]> vertPos = fg2.getPositions();
		assertEquals(0, vertPos.get(v14)[0]);
		assertEquals(0, vertPos.get(v14)[1]);
		assertEquals(0, vertPos.get(v15)[0]);
		assertEquals(1, vertPos.get(v15)[1]);
		assertEquals(0, vertPos.get(v12)[0]);
		assertEquals(2, vertPos.get(v12)[1]);
		assertEquals(0, vertPos.get(v13)[0]);
		assertEquals(3, vertPos.get(v13)[1]);
		assertEquals(1, vertPos.get(v16)[0]);
		assertEquals(1, vertPos.get(v16)[1]);
	}

//TODO toString()
//	@Test
//	public void testToString() {
//		fail("Not yet implemented"); // TODO
//	}
}
