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

package info.pavie.opennetworkmap.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

public class RWFile {
	/**
	 * Reads a text file
	 * @param f The file to read
	 * @return The readen text
	 * @throws FileNotFoundException If the file wasn't found
	 */
	public static String readTextFile(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		StringBuilder result = new StringBuilder();
		while(s.hasNextLine()) {
			result.append(s.nextLine()+"\n");
		}
		s.close();
		return result.toString();
	}
	
	/**
	 * Writes a text file
	 * @param f The file to write in
	 * @param s The text to write
	 * @throws IOException If an error occurs during file writing
	 */
	public static void writeTextFile(File f, String s) throws IOException {
		Writer w = new OutputStreamWriter(new FileOutputStream(f));
		w.write(s);
		w.close();
	}
}
