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
import info.pavie.opennetworkmap.model.network.Vertex;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link RepresentableVertex}.
 * @author Adrien PAVIE
 */
public class TestRepresentableVertex {
//ATTRIBUTES
	private Vertex v1;
	private RepresentableVertex rv1;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		v1 = new Vertex("V1", null, 48.12, -1.42);
		rv1 = new RepresentableVertex(v1, -1, 3);
	}

//TESTS
// Constructor
	@Test
	@SuppressWarnings("unused")
	public void testRepresentableVertex() {
		RepresentableVertex rv = new RepresentableVertex(v1, 0, 0);
	}

// getX()
	@Test
	public void testGetX() {
		assertEquals(-1, rv1.getX());
	}

// getY()
	@Test
	public void testGetY() {
		assertEquals(3, rv1.getY());
	}

// getVertex()
	@Test
	public void testGetVertex() {
		assertEquals(v1, rv1.getVertex());
	}

}
