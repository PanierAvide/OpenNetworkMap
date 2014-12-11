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

import info.pavie.opennetworkmap.model.draw.style.NetworkStyle;
import info.pavie.opennetworkmap.model.draw.style.TagBasedRule;
import info.pavie.opennetworkmap.util.RWFile;

import java.io.File;
import java.io.FileNotFoundException;

import com.osbcp.cssparser.CSSParser;

/**
 * This class reads a CSS file and creates the corresponding {@link NetworkStyle}
 * @author Adrien PAVIE
 */
public class NetworkStyleReader {
//OTHER METHODS
	/**
	 * Reads style rules from an OpenNetworkMap CSS file
	 * @param css The CSS file to read
	 * @return The read style
	 * @throws FileNotFoundException If the given CSS file doesn't exist
	 * @throws Exception If an error occurs during parsing
	 */
	public NetworkStyle readStyleFile(File css) throws FileNotFoundException, Exception {
		return new NetworkStyle(
				TagBasedRule.parseRules(
						CSSParser.parse(
								RWFile.readTextFile(css)
						)
				)
		);
	}
}
