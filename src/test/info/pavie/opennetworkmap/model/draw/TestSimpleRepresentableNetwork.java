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

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link SimpleRepresentableNetwork}.
 * @author Adrien PAVIE
 */
public class TestSimpleRepresentableNetwork {
//ATTRIBUTES
	private SimpleRepresentableNetwork srn1;
	private Set<RepresentableVertex> srv1;
	private Set<RepresentableEdge> sre1;
	private RepresentableVertex rv1, rv2;
	private RepresentableEdge re1;
	private Vertex v1, v2;
	private Edge e1;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		//Init vertices
		v1 = new Vertex("V1", null, 0, 0);
		v2 = new Vertex("V2", null, 1, 1);
		
		//Init edges
		e1 = new Edge("E1", null, v1, v2);
		
		//Init representable vertices
		rv1 = new RepresentableVertex(v1, 0, 0);
		rv2 = new RepresentableVertex(v2, 1, 1);
		
		//Init representable edges
		re1 = new RepresentableEdge(e1, rv1, rv2);
		
		//Init sets
		srv1 = new HashSet<RepresentableVertex>();
		sre1 = new HashSet<RepresentableEdge>();
		
		//Fill sets
		srv1.add(rv1);
		srv1.add(rv2);
		sre1.add(re1);
		
		//Init network
		srn1 = new SimpleRepresentableNetwork(srv1, sre1);
	}

//TESTS
// Constructor
	@Test
	public void testSimpleRepresentableNetwork() {
		srn1 = new SimpleRepresentableNetwork(srv1, sre1);
	}

// getRepresentableVertices()
	@Test
	public void testGetRepresentableVertices() {
		assertEquals(srv1, srn1.getRepresentableVertices());
	}

// getRepresentableEdges()
	@Test
	public void testGetRepresentableEdges() {
		assertEquals(sre1, srn1.getRepresentableEdges());
	}

// getRepresentableVertex()
	@Test
	public void testGetRepresentableVertexExisting() {
		assertEquals(rv1, srn1.getRepresentableVertex(v1));
		assertEquals(rv2, srn1.getRepresentableVertex(v2));
	}

	@Test
	public void testGetRepresentableVertexNotExisting() {
		assertNull(srn1.getRepresentableVertex(new Vertex("V3", null, 2, 2)));
	}
}
