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
		/*
		 * Check given parameters
		 */
		//Are they defined ?
		if(e == null) {
			throw new NullPointerException("Undefined edge");
		}
		if(start == null) {
			throw new NullPointerException("No start vertex defined");
		}
		if(end == null) {
			throw new NullPointerException("No end vertex defined");
		}
		
		//Are they correct ?
		if(!e.getStartVertex().equals(start.getVertex())) {
			throw new RuntimeException("The given representable start vertex isn't the start vertex of this edge");
		}
		if(!e.getEndVertex().equals(end.getVertex())) {
			throw new RuntimeException("The given representable end vertex isn't the end vertex of this edge");
		}
		
		//Set attributes
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
	
	/**
	 * Get the direction of the edge in this representation, in radians
	 * @return The direction, in radians (0 = West to East)
	 */
	public double getDirection() {
		return Math.atan2(end.getY()-start.getY(), end.getX()-start.getX());
	}
}
