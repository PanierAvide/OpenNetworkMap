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

import static org.junit.Assert.*;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Vertex;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link RepresentableEdge}.
 * @author Adrien PAVIE
 */
public class TestRepresentableEdge {
//ATTRIBUTES
	private Vertex v1, v2;
	private RepresentableVertex rv1, rv2;
	private Edge e1, e2;
	private RepresentableEdge re1, re2;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		v1 = new Vertex("V1", null, 0, 0);
		v2 = new Vertex("V2", null, 12, 12);
		rv1 = new RepresentableVertex(v1, 0, 0);
		rv2 = new RepresentableVertex(v2, 1, 1);
		e1 = new Edge("E1", null, v1, v2);
		re1 = new RepresentableEdge(e1, rv1, rv2);
		
		e2 = new Edge("E2", null, v2, v1);
		re2 = new RepresentableEdge(e2, rv2, rv1);
	}

//TESTS
// Constructor
	@Test
	@SuppressWarnings("unused")
	public void testRepresentableEdgeOk() {
		RepresentableEdge re = new RepresentableEdge(e1, rv1, rv2);
	}
	
	@Test(expected=NullPointerException.class)
	@SuppressWarnings("unused")
	public void testRepresentableEdgeNoEdge() {
		RepresentableEdge re = new RepresentableEdge(null, rv1, rv2);
	}
	
	@Test(expected=NullPointerException.class)
	@SuppressWarnings("unused")
	public void testRepresentableEdgeNoStart() {
		RepresentableEdge re = new RepresentableEdge(e1, null, rv2);
	}
	
	@Test(expected=NullPointerException.class)
	@SuppressWarnings("unused")
	public void testRepresentableEdgeNoEnd() {
		RepresentableEdge re = new RepresentableEdge(e1, rv1, null);
	}
	
	@Test(expected=RuntimeException.class)
	@SuppressWarnings("unused")
	public void testRepresentableEdgeWrongStart() {
		RepresentableEdge re = new RepresentableEdge(e1, rv2, rv2);
	}
	
	@Test(expected=RuntimeException.class)
	@SuppressWarnings("unused")
	public void testRepresentableEdgeWrongEnd() {
		RepresentableEdge re = new RepresentableEdge(e1, rv1, rv1);
	}

// getStart()
	@Test
	public void testGetStart() {
		assertEquals(rv1, re1.getStart());
	}
	
// getEnd()
	@Test
	public void testGetEnd() {
		assertEquals(rv2, re1.getEnd());
	}

// getEdge()
	@Test
	public void testGetEdge() {
		assertEquals(e1, re1.getEdge());
	}

// getDirection()
	@Test
	public void testGetDirectionDiagonal() {
		assertEquals(Math.PI/4, re1.getDirection(), 0);
	}

	@Test
	public void testGetDirectionOtherDiagonal() {
		assertEquals(-3*Math.PI/4, re2.getDirection(), 0);
	}
}
