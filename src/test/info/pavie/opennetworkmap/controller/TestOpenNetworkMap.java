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

package info.pavie.opennetworkmap.controller;

import info.pavie.opennetworkmap.controller.converter.GridConverter;
import info.pavie.opennetworkmap.controller.converter.NetworkConverter;
import info.pavie.opennetworkmap.controller.exporter.NetworkExporter;
import info.pavie.opennetworkmap.controller.exporter.SVGExporter;
import info.pavie.opennetworkmap.controller.standardizer.BusLineStandardizer;
import info.pavie.opennetworkmap.controller.standardizer.DetailedElectricityStandardizer;
import info.pavie.opennetworkmap.controller.standardizer.ElectricityStandardizer;
import info.pavie.opennetworkmap.controller.standardizer.NetworkStandardizer;
import info.pavie.opennetworkmap.controller.standardizer.SubwayStandardizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;


public class TestOpenNetworkMap {
//ATTRIBUTES
	private OpenNetworkMap onm1;
	private File osmFile1, osmFile2, osmFile3, osmFile4, osmFile5, osmFile6, osmFile7;
	private NetworkStandardizer ns1, ns2, ns3, ns4;
	private NetworkConverter nc1;
	private File css1, css2, css3;
	private NetworkExporter ce1/*, ce2*/;
	private File out1, out2, out3, out4, out5, out6, out7, out8;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		onm1 = new OpenNetworkMap();
		ce1 = new SVGExporter();
//		ce2 = new CanvasToWebExporter();
		nc1 = new GridConverter();

		//Village detailed power network
		osmFile1 = new File("test/xml/bleruais.osm");
		ns1 = new DetailedElectricityStandardizer();
		css1 = new File("style/default.onmcss");
		out1 = new File("test/svg/TestOpenNetworkMap.VillagePower.svg");
		
		//Region simplified power network
		osmFile2 = new File("test/xml/power_35.osm");
		ns2 = new ElectricityStandardizer();
		css2 = new File("style/power.onmcss");
		out2 = new File("test/svg/TestOpenNetworkMap.RegionPower.svg");
		
		//Region simplified power network (light)
//		osmFile5 = new File("test/xml/power_35_light.osm");
//		out5 = new File("test/svg/TestOpenNetworkMap.RegionPowerLight.svg");
		
		//North of France power network
		osmFile5 = new File("test/xml/power_0.5FR.osm");
		out5 = new File("test/svg/TestOpenNetworkMap.FrancePower.svg");
		
		//London subway
		osmFile3 = new File("test/xml/london_subway_rels.osm");
		ns3 = new SubwayStandardizer();
		css3 = new File("style/subway.onmcss");
		out3 = new File("test/svg/TestOpenNetworkMap.Subway.svg");
		
		//London subway (light)
		osmFile4 = new File("test/xml/london_subway_light.osm");
		out4 = new File("test/svg/TestOpenNetworkMap.SubwayLight.svg");
		
		//Rennes subway
		osmFile5 = new File("test/xml/rennes_subway.osm");
		out5 = new File("test/svg/TestOpenNetworkMap.SubwayRennes.svg");
		
		//Rennes buses
		osmFile6 = new File("test/xml/rennes_star.osm");
		ns4 = new BusLineStandardizer();
		out6 = new File("test/svg/TestOpenNetworkMap.BusRennes.svg");
		
		//Rennes buses light
		osmFile7 = new File("test/xml/rennes_bus_maj.osm");
		out7 = new File("test/svg/TestOpenNetworkMap.BusRennesLight.svg");
		out8 = new File("test/html/TestOpenNetworkMap.BusRennesLight.html");
	}

//TESTS
	@Test
	public void testProcessVillagePower() throws FileNotFoundException, SAXException, IOException, Exception {
		onm1.process(osmFile1, ns1, nc1, css1, ce1, out1);
	}
	
	@Test
	public void testProcessRegionPower() throws FileNotFoundException, SAXException, IOException, Exception {
		onm1.process(osmFile2, ns2, nc1, css2, ce1, out2);
	}
	
//	@Test
//	public void testProcessRegionPowerLight() throws FileNotFoundException, SAXException, IOException, Exception {
//		onm1.process(osmFile5, ns2, css2, ce1, out5);
//	}
	
//	@Test
//	public void testProcessFrancePower() throws FileNotFoundException, SAXException, IOException, Exception {
//		onm1.process(osmFile5, ns2, nc1, css2, ce1, out5);
//	}
	
	@Test
	public void testProcessLondonSubwayLight() throws FileNotFoundException, SAXException, IOException, Exception {
		onm1.process(osmFile4, ns3, nc1, css3, ce1, out4);
	}
	
	@Test
	public void testProcessLondonSubway() throws FileNotFoundException, SAXException, IOException, Exception {
		onm1.process(osmFile3, ns3, nc1, css3, ce1, out3);
	}
	
	@Test
	public void testProcessRennesSubway() throws FileNotFoundException, SAXException, IOException, Exception {
		onm1.process(osmFile5, ns3, nc1, css3, ce1, out5);
	}
	
	@Test
	public void testProcessRennesBus() throws FileNotFoundException, SAXException, IOException, Exception {
		onm1.process(osmFile6, ns4, nc1, css3, ce1, out6);
	}
	
	@Test
	public void testProcessRennesBusLight() throws FileNotFoundException, SAXException, IOException, Exception {
		onm1.process(osmFile7, ns4, nc1, css3, ce1, out7);
	}
	
//	@Test
//	public void testProcessRennesBusLightWeb() throws FileNotFoundException, SAXException, IOException, Exception {
//		onm1.process(osmFile7, ns4, nc1, css3, ce2, out8);
//	}
}
