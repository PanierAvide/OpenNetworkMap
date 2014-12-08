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

package info.pavie.opennetworkmap;

import info.pavie.opennetworkmap.controller.converter.TestNetworkConverter;
import info.pavie.opennetworkmap.controller.exporter.TestCanvasToSVGExporter;
import info.pavie.opennetworkmap.controller.standardizer.TestDetailedElectricityStandardizer;
import info.pavie.opennetworkmap.controller.standardizer.TestElectricityStandardizer;
import info.pavie.opennetworkmap.model.draw.TestCanvas;
import info.pavie.opennetworkmap.model.draw.TestFlexibleGrid;
import info.pavie.opennetworkmap.model.draw.TestLayer;
import info.pavie.opennetworkmap.model.network.TestEdge;
import info.pavie.opennetworkmap.model.network.TestNetwork;
import info.pavie.opennetworkmap.model.network.TestVertex;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		TestNetworkConverter.class,
		TestCanvasToSVGExporter.class,
		TestDetailedElectricityStandardizer.class,
		TestCanvas.class,
		TestNetwork.class,
		TestVertex.class,
		TestEdge.class,
		TestFlexibleGrid.class,
		TestLayer.class,
		TestElectricityStandardizer.class
		})
public class TestSuite {
  //nothing
}