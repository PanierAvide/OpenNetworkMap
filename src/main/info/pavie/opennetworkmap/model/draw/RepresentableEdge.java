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

package info.pavie.opennetworkmap.model.draw;

import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Vertex;

/**
 * A representable edge is an {@link Edge} which can be drawn using a {@link RepresentableNetwork}.
 * It is based on {@link RepresentableVertex}, and works similarly as {@link Vertex} and {@link Edge}.
 * @author Adrien PAVIE
 */
public class RepresentableEdge {
//ATTRIBUTES
	/** The vertex from which this edge starts **/
	private RepresentableVertex start;
	
	/** The vertex where this edge ends **/
	private RepresentableVertex end;
	
	/** The base edge **/
	private Edge e;

//CONSTRUCTOR
	/**
	 * Default constructor
	 * @param e The base edge
	 * @param start The representable vertex corresponding to the start vertex
	 * @param end The representable vertex corresponding to the end vertex
	 */
	public RepresentableEdge(Edge e, RepresentableVertex start, RepresentableVertex end) {
		this.start = start;
		this.end = end;
		this.e = e;
	}

//ACCESSORS
	/**
	 * @return the start vertex
	 */
	public RepresentableVertex getStart() {
		return start;
	}

	/**
	 * @return the end vertex
	 */
	public RepresentableVertex getEnd() {
		return end;
	}

	/**
	 * @return the base edge
	 */
	public Edge getEdge() {
		return e;
	}
}
