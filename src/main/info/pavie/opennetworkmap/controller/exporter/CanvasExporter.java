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

import info.pavie.opennetworkmap.model.draw.Canvas;

import java.io.File;
import java.io.IOException;

/**
 * A {@link Canvas} exporter converts a canvas object into output file.
 * For example, you can convert a canvas into a SVG image.
 */
public interface CanvasExporter {
//OTHER METHODS
	/**
	 * Converts the canvas into a file
	 * @param c The canvas
	 * @param output The output file/folder
	 * @return True if conversion was successful
	 * @throws IOException If an error occur during conversion
	 */
	boolean export(Canvas c, File output) throws IOException;
}
