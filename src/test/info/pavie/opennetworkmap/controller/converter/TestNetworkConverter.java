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

package info.pavie.opennetworkmap.controller.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import info.pavie.basicosmparser.controller.OSMParser;
import info.pavie.opennetworkmap.controller.converter.NetworkConverter;
import info.pavie.opennetworkmap.controller.exporter.CanvasToSVGExporter;
import info.pavie.opennetworkmap.controller.standardizer.DetailedElectricityStandardizer;
import info.pavie.opennetworkmap.model.draw.network.Canvas;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

//TODO Test if CSS rules are correctly applied
public class TestNetworkConverter {
//ATTRIBUTES
	private NetworkConverter nc1;
	private Network n1, n2, n3, n4, n5, n6;
	private Edge e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17;
	private Vertex v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11, v12, v13, v14,
					v15, v16, v17, v18, v19, v20, v21, v22, v23;
	private File css1;
	private Canvas c1;

//SETUP
	@Before
	public void setUp() throws Exception {
		nc1 = new NetworkConverter();
		css1 = new File("style/default.onmcss");

		//Define network 1, v1 -> v2
		v1 = new Vertex("v1", null, 0, 0);
		v2 = new Vertex("v2", null, 0, 1);
		n1 = new Network();
		e1 = new Edge("e1", null, v1, v2);
		n1.addEdge(e1);
		
		//Define network 2, v3 -> v4 -> v5
		v3 = new Vertex("v3", null, 0, 0);
		v4 = new Vertex("v4", null, 0, 1);
		v5 = new Vertex("v5", null, 0, 2);
		n2 = new Network();
		e2 = new Edge("e2", null, v3, v4);
		e3 = new Edge("e3", null, v4, v5);
		n2.addEdge(e2);
		n2.addEdge(e3);
		
		//Define network 3, fork v6 -> v7, v6 -> v8, v6 -> v9
		v6 = new Vertex("v6", null, 0, 0);
		v7 = new Vertex("v7", null, -1, 1);
		v8 = new Vertex("v8", null, 0, 1);
		v9 = new Vertex("v9", null, 1, 1);
		n3 = new Network();
		e4 = new Edge("e4", null, v6, v7);
		e5 = new Edge("e5", null, v6, v8);
		e6 = new Edge("e6", null, v6, v9);
		n3.addEdge(e4);
		n3.addEdge(e5);
		n3.addEdge(e6);
		
		//Define network 4
		/*
		 * v13 -> v11 -> v14
		 * v10 -> v11 -> v12
		 */
		v10 = new Vertex("v10", null, 0,0);
		v11 = new Vertex("v11", null, 0,1);
		v12 = new Vertex("v12", null, 0,2);
		v13 = new Vertex("v13", null, 1,0);
		v14 = new Vertex("v14", null, 1,2);
		n4 = new Network();
		e7 = new Edge("e7", null, v10, v11);
		e8 = new Edge("e8", null, v11, v12);
		e9 = new Edge("e9", null, v13, v11);
		e10 = new Edge("e10", null, v11, v14);
		n4.addEdge(e7);
		n4.addEdge(e8);
		n4.addEdge(e9);
		n4.addEdge(e10);
		
		//Define network 5
		/*
		 * v18 -> v16 -> v19
		 * v15 -> v16 -> v17
		 * v18 -> v19
		 */
		v15 = new Vertex("v15", null, 0,0);
		v16 = new Vertex("v16", null, 0,1);
		v17 = new Vertex("v17", null, 0,2);
		v18 = new Vertex("v18", null, 1,0);
		v19 = new Vertex("v19", null, 1,2);
		n5 = new Network();
		e11 = new Edge("e11", null, v15, v16);
		e12 = new Edge("e12", null, v16, v17);
		e13 = new Edge("e13", null, v18, v16);
		e14 = new Edge("e14", null, v16, v19);
		e15 = new Edge("e15", null, v18, v19);
		n5.addEdge(e11);
		n5.addEdge(e12);
		n5.addEdge(e13);
		n5.addEdge(e14);
		n5.addEdge(e15);
		
		//Define network 6
		/*
		 * v22 -> v23	v20 -> v21
		 */
		n6 = new Network();
		v20 = new Vertex("v20", null, 0, 0);
		v21 = new Vertex("v21", null, 0, 1);
		v22 = new Vertex("v22", null, 0, -2);
		v23 = new Vertex("v23", null, 0, -1);
		e16 = new Edge("e16", null, v20, v21);
		e17 = new Edge("e17", null, v22, v23);
		n6.addEdge(e16);
		n6.addEdge(e17);
	}

//TESTS
// convert()
	@Test
	public void testConvertSimple() throws FileNotFoundException, Exception {
		c1 = nc1.convert(n1, css1);
		assertEquals(v1, c1.getVertex(0, 0));
		assertEquals(v2, c1.getVertex(0, 1));
	}
	
	@Test
	public void testConvertThreeVertices() throws FileNotFoundException, Exception {
		c1 = nc1.convert(n2, css1);
		assertEquals(v3, c1.getVertex(0, 0));
		assertEquals(v4, c1.getVertex(0, 1));
		assertEquals(v5, c1.getVertex(0, 2));
		assertEquals(1, c1.getVertex(0, 0).getLinkedVertices().size());
		assertEquals(2, c1.getVertex(0, 1).getLinkedVertices().size());
		assertEquals(1, c1.getVertex(0, 2).getLinkedVertices().size());
		assertTrue(c1.getVertex(0, 0).getLinkedVertices().contains(c1.getVertex(0, 1)));
		assertTrue(c1.getVertex(0, 2).getLinkedVertices().contains(c1.getVertex(0, 1)));
		assertTrue(c1.getVertex(0, 1).getLinkedVertices().contains(c1.getVertex(0, 0)));
		assertTrue(c1.getVertex(0, 1).getLinkedVertices().contains(c1.getVertex(0, 2)));
		
//		CanvasToSVGExporter exporter = new CanvasToSVGExporter();
//		exporter.export(c1, new File("test/testThree.svg"));
	}
	
	@Test
	public void testConvertForkSameDirection() throws FileNotFoundException, Exception {
		c1 = nc1.convert(n3, css1);
//		System.out.println(c1);
		assertEquals(v6, c1.getVertex(1, 0));
		assertEquals(v7, c1.getVertex(0, 1));
		assertEquals(v8, c1.getVertex(1, 1));
		assertEquals(v9, c1.getVertex(2, 1));
	}
	
	@Test(expected=NullPointerException.class)
	public void testConvertNoCSS() throws FileNotFoundException, Exception {
		c1 = nc1.convert(n1, null);
	}
	
	@Test
	public void testConvertCSSDefined() throws FileNotFoundException, Exception {
		c1 = nc1.convert(n1, css1);
		assertEquals(8, c1.getComponentStyle(v1).size());
	}
	
	@Test
	public void testConvertTwoLinesDifferentStart() throws FileNotFoundException, Exception {
		c1 = nc1.convert(n4, css1);
//		System.out.println(c1);
		assertEquals(v10, c1.getVertex(0, 0));
		assertEquals(v11, c1.getVertex(0, 1));
		assertEquals(v12, c1.getVertex(0, 2));
		assertEquals(v13, c1.getVertex(1, 0));
		assertEquals(v14, c1.getVertex(1, 2));
		assertNull(c1.getVertex(1, 1));
	}
	
	@Test
	public void testConvertTwoLinesDifferentStartCrossing() throws FileNotFoundException, Exception {
		c1 = nc1.convert(n5, css1);
//		System.out.println(c1);
		assertEquals(v15, c1.getVertex(0, 0));
		assertEquals(v16, c1.getVertex(0, 1));
		assertEquals(v17, c1.getVertex(0, 2));
		assertEquals(v18, c1.getVertex(1, 1));
		assertEquals(v19, c1.getVertex(1, 2));
		assertNull(c1.getVertex(1, 0));
	}
	
	@Test
	public void testConvertTwoDisconnectedEdges() throws FileNotFoundException, Exception {
		c1 = nc1.convert(n6, css1);
//		System.out.println(c1);
		assertEquals(v22, c1.getVertex(0, 0));
		assertEquals(v23, c1.getVertex(0, 1));
		assertEquals(v20, c1.getVertex(0, 2));
		assertEquals(v21, c1.getVertex(0, 3));
	}
	
	@Test
	public void testConvertPowerTest1() throws FileNotFoundException, Exception {
		OSMParser p1 = new OSMParser();
		DetailedElectricityStandardizer s1 = new DetailedElectricityStandardizer();
		Network n = s1.standardize(p1.parse(new File("test/xml/power_test1.osm")));
		c1 = nc1.convert(n, css1);
		
		//Default tags for pole
		Map<String,String> mapPole = new HashMap<String,String>(1);
		mapPole.put("type", "second");
		
		//Default tags for line
		Map<String,String> mapLine = new HashMap<String,String>(1);
		mapLine.put("type", "first");
		
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
		
//		Edge e1 = new Edge("L1", mapLine, v1_1, v1_2);
//		Edge e2 = new Edge("L1", mapLine, v1_2, v1_3);
//		Edge e3 = new Edge("L1", mapLine, v1_3, v1_4);
//		Edge e4 = new Edge("L1bis", mapLine, v1_2, v1_5);
//		Edge e5 = new Edge("L2", mapLine, v2_1, v2_2);
//		Edge e6 = new Edge("L2", mapLine, v2_2, v2_3);
//		Edge e7 = new Edge("L2bis", mapLine, v2_5, v2_2);
//		Edge e8 = new Edge("L2bis", mapLine, v2_2, v2_4);
//		Edge e9 = new Edge("L3", mapLine, v3_1, v3_2);
		
		assertEquals(v2_1, c1.getVertex(0, 0));
		assertEquals(v2_5, c1.getVertex(0, 2));
		assertEquals(v3_1, c1.getVertex(0, 3));
		assertEquals(v3_2, c1.getVertex(0, 4));
		assertEquals(v1_1, c1.getVertex(0, 5));
		
		assertEquals(v2_2, c1.getVertex(1, 1));
		assertEquals(v1_2, c1.getVertex(1, 5));
		assertEquals(v1_5, c1.getVertex(1, 6));
		
		assertEquals(v2_4, c1.getVertex(2, 0));
		assertEquals(v2_3, c1.getVertex(2, 2));
		assertEquals(v1_3, c1.getVertex(2, 5));
		
		assertEquals(v1_4, c1.getVertex(3, 6));
		
		CanvasToSVGExporter export = new CanvasToSVGExporter();
		export.export(c1, new File("test/svg/TestNetworkConverter.PowerTest1.svg"));
	}
}
