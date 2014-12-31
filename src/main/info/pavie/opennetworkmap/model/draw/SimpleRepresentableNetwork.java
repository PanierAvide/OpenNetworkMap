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

import java.util.Iterator;
import java.util.Set;

/**
 * The simplest implementation of {@link RepresentableNetwork}.
 * This class uses two sets to store edges and vertices.
 * @author Adrien PAVIE
 */
public class SimpleRepresentableNetwork implements RepresentableNetwork {
//ATTRIBUTES
	/** The set of representable vertices **/
	private Set<RepresentableVertex> vertices;
	
	/** The set of representable edges **/
	private Set<RepresentableEdge> edges;
	
//CONSTRUCTOR
	/**
	 * Class constructor
	 * @param v The representable vertices set
	 * @param e The representable edges set
	 */
	public SimpleRepresentableNetwork(Set<RepresentableVertex> v, Set<RepresentableEdge> e) {
		vertices = v;
		edges = e;
	}
	
//ACCESSORS
	@Override
	public Set<RepresentableVertex> getRepresentableVertices() {
		return vertices;
	}

	@Override
	public Set<RepresentableEdge> getRepresentableEdges() {
		return edges;
	}

	@Override
	public RepresentableVertex getRepresentableVertex(Vertex v) {
		RepresentableVertex result = null;
		boolean found = false;
		Iterator<RepresentableVertex> itRVertices = vertices.iterator();
		
		RepresentableVertex rv;
		while(itRVertices.hasNext() && !found) {
			rv = itRVertices.next();
			
			if(rv.getVertex().equals(v)) {
				found = true;
				result = rv;
			}
		}
		
		return result;
	}

}
