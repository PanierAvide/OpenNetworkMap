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
import static org.junit.Assert.fail;
import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import info.pavie.opennetworkmap.controller.converter.GridConverter;
import info.pavie.opennetworkmap.controller.converter.NetworkConverter;
import info.pavie.opennetworkmap.controller.exporter.NetworkStyleReader;
import info.pavie.opennetworkmap.controller.exporter.SVGExporter;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestElectricityStandardizer {
//ATTRIBUTES
	private ElectricityStandardizer es1;
	private Node n1_1, n1_2, n1_3, n2_1, n2_2, n2_3, n2_4, n2_5, n2_6, n2_7, n2_8, n2_9, n2_10, n2_11, n2_12,
					n3_1, n3_2, n3_3, n3_4, n3_5, n3_6, n3_7, n3_8, n3_9, n3_10, n3_11, n3_12;
	private Way w1_1, w2_1, w2_2, w2_3, w3_1, w3_2, w3_3, w3_4;
	private Map<String,Element> el1, el2, el3;
	private Map<String,String> tagsGenerator, tagsSubstation, tagsLine, tagsPole;
	private Network result;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		es1 = new ElectricityStandardizer();
		
		//Default tags
		tagsGenerator = new HashMap<String,String>(1);
		tagsGenerator.put("type", "first");
		tagsSubstation = new HashMap<String,String>(1);
		tagsSubstation.put("type", "second");
		tagsPole = new HashMap<String,String>(1);
		tagsPole.put("type", "third");
		tagsLine = new HashMap<String,String>(1);
		tagsLine.put("type", "first");
		
		//Data 1
		el1 = new HashMap<String,Element>(4);
		n1_1 = new Node(1, 0, 0);
		n1_1.addTag("power", "generator");
		n1_2 = new Node(2, 0, 1);
		n1_2.addTag("power", "substation");
		n1_3 = new Node(3, 1, 1);
		n1_3.addTag("amenity", "parking");
		w1_1 = new Way(0);
		w1_1.addNode(n1_1);
		w1_1.addNode(n1_2);
		w1_1.addTag("power", "line");
		el1.put(n1_1.getId(), n1_1);
		el1.put(n1_2.getId(), n1_2);
		el1.put(n1_3.getId(), n1_3);
		el1.put(w1_1.getId(), w1_1);
		
		/*
		 * Data 2
		 */
		el2 = new HashMap<String,Element>(15);
		//Generator
		n2_1 = new Node(1, 0, 0);
		n2_2 = new Node(2, 1, 0);
		n2_11 = new Node(11, 1, 1);
		n2_3 = new Node(3, 0, 1);
		w2_1 = new Way(1);
		w2_1.addNode(n2_1);
		w2_1.addNode(n2_2);
		w2_1.addNode(n2_11);
		w2_1.addNode(n2_3);
		w2_1.addNode(n2_1);
		w2_1.addTag("power", "generator");
		//Substation
		n2_4 = new Node(4, 0, 5);
		n2_5 = new Node(5, 1, 5);
		n2_12 = new Node(12, 1, 6);
		n2_6 = new Node(6, 0, 6);
		w2_2 = new Way(2);
		w2_2.addNode(n2_4);
		w2_2.addNode(n2_5);
		w2_2.addNode(n2_12);
		w2_2.addNode(n2_6);
		w2_2.addNode(n2_4);
		w2_2.addTag("power", "substation");
		//Line
		n2_7 = new Node(7, 0.5, 0.5);
		n2_8 = new Node(8, 0.5, 5.5);
		n2_10 = new Node(10, 0.7, 3);
		w2_3 = new Way(3);
		w2_3.addNode(n2_7);
		w2_3.addNode(n2_10);
		w2_3.addNode(n2_8);
		w2_3.addTag("power", "line");
		//Noise
		n2_9 = new Node(9, 1, 1);
		//Elements in map
		el2.put(n2_1.getId(), n2_1);
		el2.put(n2_2.getId(), n2_2);
		el2.put(n2_3.getId(), n2_3);
		el2.put(n2_4.getId(), n2_4);
		el2.put(n2_5.getId(), n2_5);
		el2.put(n2_6.getId(), n2_6);
		el2.put(n2_7.getId(), n2_7);
		el2.put(n2_8.getId(), n2_8);
		el2.put(n2_9.getId(), n2_9);
		el2.put(n2_10.getId(), n2_10);
		el2.put(n2_11.getId(), n2_11);
		el2.put(n2_12.getId(), n2_12);
		el2.put(w2_1.getId(), w2_1);
		el2.put(w2_2.getId(), w2_2);
		el2.put(w2_3.getId(), w2_3);
		
		/*
		 * Data 3
		 */
		el3 = new HashMap<String,Element>(16);
		//Generator
		n3_1 = new Node(1, 0, 0);
		n3_2 = new Node(2, 1, 0);
		n3_11 = new Node(11, 1, 1);
		n3_3 = new Node(3, 0, 1);
		w3_1 = new Way(1);
		w3_1.addNode(n3_1);
		w3_1.addNode(n3_2);
		w3_1.addNode(n3_11);
		w3_1.addNode(n3_3);
		w3_1.addNode(n3_1);
		w3_1.addTag("power", "generator");
		w3_1.addTag("ref", "Generator 1");
		//Substation
		n3_4 = new Node(4, 0, 5);
		n3_5 = new Node(5, 1, 5);
		n3_12 = new Node(12, 1, 6);
		n3_6 = new Node(6, 0, 6);
		w3_2 = new Way(2);
		w3_2.addNode(n3_4);
		w3_2.addNode(n3_5);
		w3_2.addNode(n3_12);
		w3_2.addNode(n3_6);
		w3_2.addNode(n3_4);
		w3_2.addTag("power", "substation");
		w3_2.addTag("ref", "Substation 1");
		//Line 1
		n3_7 = new Node(7, 0.5, 0.5);
		n3_10 = new Node(10, 0.7, 3);
		n3_10.addTag("power", "pole");
		n3_10.addTag("ref", "Pole 1");
		w3_3 = new Way(3);
		w3_3.addNode(n3_7);
		w3_3.addNode(n3_10);
		w3_3.addTag("power", "line");
		w3_3.addTag("ref", "Line 1");
		//Line 2
		n3_8 = new Node(8, 0.5, 5.5);
		w3_4 = new Way(4);
		w3_4.addNode(n3_10);
		w3_4.addNode(n3_8);
		w3_4.addTag("power", "line");
		w3_4.addTag("ref", "Line 2");
		//Noise
		n3_9 = new Node(9, 1, 1);
		//Elements in map
		el3.put(n3_1.getId(), n3_1);
		el3.put(n3_2.getId(), n3_2);
		el3.put(n3_3.getId(), n3_3);
		el3.put(n3_4.getId(), n3_4);
		el3.put(n3_5.getId(), n3_5);
		el3.put(n3_6.getId(), n3_6);
		el3.put(n3_7.getId(), n3_7);
		el3.put(n3_8.getId(), n3_8);
		el3.put(n3_9.getId(), n3_9);
		el3.put(n3_10.getId(), n3_10);
		el3.put(n3_11.getId(), n3_11);
		el3.put(n3_12.getId(), n3_12);
		el3.put(w3_1.getId(), w3_1);
		el3.put(w3_2.getId(), w3_2);
		el3.put(w3_3.getId(), w3_3);
		el3.put(w3_4.getId(), w3_4);
	}

//TESTS
	@Test
	public void testStandardizeSimple() {
		result = es1.standardize(el1);
		assertEquals(2, result.getVertices().size());
		assertEquals(1, result.getEdges().size());
		
		Vertex v1 = new Vertex(null, tagsGenerator, 0, 0);
		Vertex v2 = new Vertex(null, tagsSubstation, 0, 1);
		Edge e1 = new Edge(null, tagsLine, v1, v2);
		
		assertTrue(result.getVertices().contains(v1));
		assertTrue(result.getVertices().contains(v2));
		assertTrue(result.getEdges().contains(e1));
	}
	
	@Test
	public void testStandardizeStationsAsAreas() {
		result = es1.standardize(el2);
		assertEquals(2, result.getVertices().size());
		assertEquals(1, result.getEdges().size());
		
		Vertex v1 = new Vertex(null, tagsGenerator, 0.5, 0.5);
		Vertex v2 = new Vertex(null, tagsSubstation, 0.5, 5.5);
		Edge e1 = new Edge(null, tagsLine, v1, v2);
		
		assertTrue(result.getVertices().contains(v1));
		assertTrue(result.getVertices().contains(v2));
		assertTrue(result.getEdges().contains(e1));
	}
	
	@Test
	public void testStandardizeTwoLinesBetweenStations() {
		result = es1.standardize(el3);
		assertEquals(3, result.getVertices().size());
		assertEquals(2, result.getEdges().size());
		
		Vertex v1 = new Vertex("Generator 1", tagsGenerator, 0.5, 0.5);
		Vertex v2 = new Vertex("Substation 1", tagsSubstation, 0.5, 5.5);
		Vertex v3 = new Vertex("Pole 1", tagsPole, 0.7, 3);
		Edge e1 = new Edge("Line 1", tagsLine, v1, v3);
		Edge e2 = new Edge("Line 2", tagsLine, v3, v2);
		
		assertTrue(result.getVertices().contains(v1));
		assertTrue(result.getVertices().contains(v2));
		assertTrue(result.getVertices().contains(v3));
		assertTrue(result.getEdges().contains(e1));
		assertTrue(result.getEdges().contains(e2));
		
		for(Vertex v : result.getVertices()) {
			switch(v.getLabel()) {
				case "Generator 1":
					assertEquals(1, v.getLinkedVertices().size());
					assertTrue(v.getLinkedVertices().contains(v3));
					break;
				case "Pole 1":
					assertEquals(1, v.getEndOf().size());
					assertTrue(v.getEndOf().contains(e1));
					assertEquals(1, v.getStartOf().size());
					assertTrue(v.getStartOf().contains(e2));
					assertEquals(2, v.getLinkedVertices().size());
					assertTrue(v.getLinkedVertices().contains(v1));
					assertTrue(v.getLinkedVertices().contains(v2));
					break;
				case "Substation 1":
					assertEquals(1, v.getLinkedVertices().size());
					assertTrue(v.getLinkedVertices().contains(v3));
					break;
				default:
					fail("Not listed vertex");
			}
		}
		
		NetworkConverter nc = new GridConverter();
		SVGExporter exporter = new SVGExporter();
		try {
			NetworkStyleReader nsr = new NetworkStyleReader();
			exporter.export(nc.createRepresentation(result), nsr.readStyleFile(new File("style/power.onmcss")), new File("test/svg/TestElectricityStandardizer.TwoLinesBetweenStations.svg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
