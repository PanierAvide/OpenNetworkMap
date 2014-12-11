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

package info.pavie.opennetworkmap.controller.exporter;

import info.pavie.opennetworkmap.model.draw.RepresentableNetwork;
import info.pavie.opennetworkmap.model.draw.network.Canvas;
import info.pavie.opennetworkmap.model.draw.style.NetworkStyle;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TestSVGExporter {
//ATTRIBUTES
	private SVGExporter exp1;
	private RepresentableNetwork rn1;
	private NetworkStyle ns1;
	private File output;
	private Canvas c1;
	private Vertex v1, v2, v3, v4, v5, v6, v7, v8, v9, v10, v11;
	private Edge e1, e2, e3, e4;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		exp1 = new SVGExporter();
		output = new File("test/svg/TestSVGExporter.Export.svg");
		
		v1 = new Vertex("v1", null, 0, 0);
		v4 = new Vertex("v4", null, 1, 1);
		v5 = new Vertex("v5", null, 0, 1);
		v6 = new Vertex("v6", null, -1, 1);
		v2 = new Vertex("v2", null, 0, -1);
		e1 = new Edge("e1", null, v1, v4);
		e2 = new Edge("e2", null, v1, v5);
		e3 = new Edge("e3", null, v1, v6);
		e4 = new Edge("e4", null, v2, v1);
		
		c1 = new Canvas(v1);
		rn1 = c1;
		ns1 = new NetworkStyle();
	}

//TESTS
// export()
	@Test
	public void testExportThreeForkRight() throws IOException {
		c1.add(v4, v1);
		c1.add(v5, v1);
		c1.add(v6, v1);
		c1.add(v2, v1);
		exp1.export(rn1, ns1, output);
	}

}
