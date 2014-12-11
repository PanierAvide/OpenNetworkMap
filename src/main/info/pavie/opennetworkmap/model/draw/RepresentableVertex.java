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

import info.pavie.opennetworkmap.model.network.Vertex;

/**
 * A representable vertex is a {@link Vertex} which can be drawn using a {@link RepresentableNetwork}.
 * It has coordinates which can be used directly for rendering.
 * @author Adrien PAVIE
 */
public class RepresentableVertex {
//ATTRIBUTES
	/** The horizontal coordinate **/
	private int x;
	
	/** The vertical coordinate **/
	private int y;
	
	/** The base vertex **/
	private Vertex v;
	
//CONSTRUCTOR
	/**
	 * Default constructor
	 * @param v The base vertex
	 * @param x The horizontal coordinate
	 * @param y The vertical coordinate
	 */
	public RepresentableVertex(Vertex v, int x, int y) {
		this.x = x;
		this.y = y;
		this.v = v;
	}

//ACCESSORS
	/**
	 * @return the horizontal coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the vertical coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the base vertex
	 */
	public Vertex getVertex() {
		return v;
	}
}
