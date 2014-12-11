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

package info.pavie.opennetworkmap.controller.standardizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import info.pavie.basicosmparser.controller.OSMParser;
import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import info.pavie.opennetworkmap.controller.converter.GridConverter;
import info.pavie.opennetworkmap.controller.converter.NetworkConverter;
import info.pavie.opennetworkmap.model.draw.RepresentableNetwork;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestDetailedElectricityStandardizer {
//ATTRIBUTES
	private DetailedElectricityStandardizer s1;
	private Network result;
	private Node n1, n2, n3, n4;
	private Way w1;
	private List<Node> ln1;
	private Map<String,Element> el1;
	private Map<String,String> mapPole, mapLine;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		s1 = new DetailedElectricityStandardizer();
		
		//Default tags for pole
		mapPole = new HashMap<String,String>(1);
		mapPole.put("type", "second");
		
		//Default tags for line
		mapLine = new HashMap<String,String>(1);
		mapLine.put("type", "first");
		
		//Init way 1
		n1 = new Node(1, 0, 0);
		n2 = new Node(2, 0.01, 1);
		n3 = new Node(3, 0.02, 2);
		n4 = new Node(4, 0.02, 2.1);
		n1.addTag("power", "pole");
		n2.addTag("power", "pole");
		n3.addTag("power", "pole");
		n1.addTag("ref", "P1");
		n2.addTag("ref", "P2");
		n3.addTag("ref", "P3");
		n4.addTag("traffic_sign", "city_limit");
		ln1 = new ArrayList<Node>(3);
		ln1.add(n1);
		ln1.add(n2);
		ln1.add(n3);
		w1 = new Way(1, ln1);
		w1.addTag("power", "line");
		w1.addTag("ref", "L1");
		el1 = new HashMap<String,Element>(5);
		el1.put(n1.getId(), n1);
		el1.put(n2.getId(), n2);
		el1.put(n3.getId(), n3);
		el1.put(n4.getId(), n4);
		el1.put(w1.getId(), w1);
	}

//TESTS
// standardize()
	@Test
	public void testStandardizeSimple() {
		result = s1.standardize(el1);
		
		//Test vertices
		Vertex v1 = new Vertex("P1", mapPole, 0, 0);
		Vertex v2 = new Vertex("P2", mapPole, 0.01, 1);
		Vertex v3 = new Vertex("P3", mapPole, 0.02, 2);
		assertEquals(3, result.getVertices().size());
//		System.out.println(result.getVertices());
		assertTrue(result.getVertices().contains(v1));
		assertTrue(result.getVertices().contains(v2));
		assertTrue(result.getVertices().contains(v3));
		
		//Test edges
		assertEquals(2, result.getEdges().size());
		result.getEdges().contains(new Edge("L1", mapLine, v1, v2));
		result.getEdges().contains(new Edge("L1", mapLine, v2, v3));
	}
	
	@Test
	public void testStandardizePower1() throws Exception {
		OSMParser p1 = new OSMParser();
		result = s1.standardize(p1.parse(new File("test/xml/power_test1.osm")));
		
		Vertex v1_1 = new Vertex("1_1", mapPole, 48.1150563133682, -2.114647948794484);
		Vertex v1_2 = new Vertex("1_2", mapPole, 48.115157099361824, -2.114647707971771);
		Vertex v1_3 = new Vertex("1_3", mapPole, 48.11525384374734, -2.114647707971771);
		Vertex v1_4 = new Vertex("1_4", mapPole, 48.1153512505816, -2.1145107419305083);
		Vertex v1_5 = new Vertex("1_5", mapPole, 48.11515780435415, -2.114511878445158);
		
		Vertex v2_1 = new Vertex("2_1", mapPole, 48.115055659793434, -2.115063500174313);
		Vertex v2_2 = new Vertex("2_2", mapPole, 48.115142639226754, -2.1149141546773107);
		Vertex v2_3 = new Vertex("2_3", mapPole, 48.1152440179538, -2.114779969412763);
		Vertex v2_4 = new Vertex("2_4", mapPole, 48.11525338898851, -2.115036831439316);
		Vertex v2_5 = new Vertex("2_5", mapPole, 48.11505191136531, -2.1148136562359174);
		
		Vertex v3_1 = new Vertex("3_1", mapPole, 48.11514843330199, -2.1148024272948653);
		Vertex v3_2 = new Vertex("3_2", mapPole, 48.11515030751255, -2.114697155972508);
		
		Edge e1 = new Edge("L1", mapLine, v1_1, v1_2);
		Edge e2 = new Edge("L1", mapLine, v1_2, v1_3);
		Edge e3 = new Edge("L1", mapLine, v1_3, v1_4);
		Edge e4 = new Edge("L1bis", mapLine, v1_2, v1_5);
		Edge e5 = new Edge("L2", mapLine, v2_1, v2_2);
		Edge e6 = new Edge("L2", mapLine, v2_2, v2_3);
		Edge e7 = new Edge("L2bis", mapLine, v2_5, v2_2);
		Edge e8 = new Edge("L2bis", mapLine, v2_2, v2_4);
		Edge e9 = new Edge("L3", mapLine, v3_1, v3_2);
		
		assertEquals(12, result.getVertices().size());
		assertTrue(result.getVertices().contains(v1_1));
		assertTrue(result.getVertices().contains(v1_2));
		assertTrue(result.getVertices().contains(v1_3));
		assertTrue(result.getVertices().contains(v1_4));
		assertTrue(result.getVertices().contains(v1_5));
		assertTrue(result.getVertices().contains(v2_1));
		assertTrue(result.getVertices().contains(v2_2));
		assertTrue(result.getVertices().contains(v2_3));
		assertTrue(result.getVertices().contains(v2_4));
		assertTrue(result.getVertices().contains(v2_5));
		assertTrue(result.getVertices().contains(v3_1));
		assertTrue(result.getVertices().contains(v3_2));
		
		assertEquals(9, result.getEdges().size());
//		for(Edge e : result.getEdges()) {
//			System.out.println(e.getLabel()+" (start: "+e.getStartVertex()+" | end: "+e.getEndVertex()+" | tags: "+e.getTags()+")");
//		}
		assertTrue(result.getEdges().contains(e1));
		assertTrue(result.getEdges().contains(e2));
		assertTrue(result.getEdges().contains(e3));
		assertTrue(result.getEdges().contains(e4));
		assertTrue(result.getEdges().contains(e5));
		assertTrue(result.getEdges().contains(e6));
		assertTrue(result.getEdges().contains(e7));
		assertTrue(result.getEdges().contains(e8));
		assertTrue(result.getEdges().contains(e9));
	}

	@Test
	public void testStandardizeVillage() throws Exception {
		OSMParser p1 = new OSMParser();
		result = s1.standardize(p1.parse(new File("test/xml/bleruais.osm")));
		NetworkConverter nc1 = new GridConverter();
		RepresentableNetwork c1 = nc1.createRepresentation(result);
//		System.out.println(c1);
	}
}
