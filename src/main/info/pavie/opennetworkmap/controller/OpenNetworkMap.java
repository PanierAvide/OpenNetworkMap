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

import info.pavie.basicosmparser.controller.OSMParser;
import info.pavie.basicosmparser.model.Element;
import info.pavie.opennetworkmap.controller.converter.NetworkConverter;
import info.pavie.opennetworkmap.controller.exporter.NetworkExporter;
import info.pavie.opennetworkmap.controller.exporter.NetworkStyleReader;
import info.pavie.opennetworkmap.controller.standardizer.NetworkStandardizer;
import info.pavie.opennetworkmap.model.draw.RepresentableNetwork;
import info.pavie.opennetworkmap.model.draw.style.NetworkStyle;
import info.pavie.opennetworkmap.model.network.Network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.xml.sax.SAXException;

/**
 * Main controller of the application.
 * Can control the whole conversion process, from data reading to canvas export.
 */
public class OpenNetworkMap {
//OTHER METHODS
	/**
	 * Processes OSM data to create a network file
	 * @param osmData The OSM data XML file
	 * @param standardizer The network standardizer to use (depends of your needs)
	 * @param converter The network converter to use (depends of your needs)
	 * @param css The CSS file to use
	 * @param exporter The exporter to use (depends of wanted output format)
	 * @param output The output file
	 * @return True if process worked
	 * @throws SAXException Error during OSM data parsing
	 * @throws FileNotFoundException If given CSS doesn't exist
	 * @throws IOException If an error occurs during export
	 * @throws Exception If an error occurs during network conversion
	 */
	public boolean process(File osmData, NetworkStandardizer standardizer, NetworkConverter converter, File css, NetworkExporter exporter, File output) throws SAXException, FileNotFoundException, IOException, Exception {
		//Parse data
		System.out.println("-Parse OSM data");
		OSMParser parser = new OSMParser();
		Map<String,Element> osmElements = parser.parse(osmData);
		
		//Create standard network
		System.out.println("-Create standard network");
		Network standardNetwork = standardizer.standardize(osmElements);
		
		//Convert network into canvas
		System.out.println("-Make network representable");
		RepresentableNetwork createdCanvas = converter.createRepresentation(standardNetwork);
		
		//Read style rules
		System.out.println("-Read style rules from CSS");
		NetworkStyleReader nsr = new NetworkStyleReader();
		NetworkStyle style = nsr.readStyleFile(css);
		
		//Export canvas
		System.out.println("-Create output file");
		return exporter.export(createdCanvas, style, output);
	}
}
