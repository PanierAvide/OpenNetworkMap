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
import info.pavie.opennetworkmap.model.draw.style.NetworkStyle;

import java.io.File;
import java.io.IOException;

/**
 * A network exporter allows to create a representation of a network and writes it on filesystem.
 * See sub-classes for more details.
 * @author Adrien PAVIE
 */
public interface NetworkExporter {
//OTHER METHODS
	/**
	 * Converts a representable network into a file
	 * @param net The representable network to export
	 * @param style The list of style rules to use
	 * @param output The output file/folder
	 * @return True if conversion was successful
	 * @throws IOException If an error occur during conversion
	 */
	boolean export(RepresentableNetwork net, NetworkStyle style, File output) throws IOException;
}
