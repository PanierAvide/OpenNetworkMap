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

package info.pavie.opennetworkmap.model.network;

import java.util.HashSet;
import java.util.Set;

/**
 * A network is a standard representation of a real network.
 * It could be used to store public transport network, or electricity network.
 * The nodes are {@link Vertex} objects, and edges are {@link Edge} objects. 
 */
public class Network {
//ATTRIBUTES
	/** The edges of the network **/
	private Set<Edge> edges;

//CONSTRUCTOR
	/**
	 * Creates an empty network.
	 */
	public Network() {
		edges = new HashSet<Edge>();
	}

//ACCESSORS
	/**
	 * @return The network edges
	 */
	public Set<Edge> getEdges() {
		return new HashSet<Edge>(edges);
	}
	
	/**
	 * @return The network vertices
	 */
	public Set<Vertex> getVertices() {
		Set<Vertex> vertices = new HashSet<Vertex>(edges.size());
		for(Edge e : edges) {
			vertices.add(e.getStartVertex());
			vertices.add(e.getEndVertex());
		}
		return vertices;
	}
	
//MODIFIERS
	/**
	 * Adds an edge to the network.
	 * @param e The edge to add
	 */
	public void addEdge(Edge e) {
		edges.add(e);
	}
	
	/**
	 * Removes one edge, and edits attached vertices.
	 * @param e The edge to remove
	 */
	public void removeEdge(Edge e) {
		e.getStartVertex().unsetStartOf(e);
		e.getEndVertex().unsetEndOf(e);
		edges.remove(e);
	}
	
	/**
	 * Removes the given edge, and merges start and end vertices (only start is kept).
	 * @param e The edge to remove
	 */
	public void simplifyEdge(Edge e) {
		edges.remove(e);
		e.getStartVertex().unsetStartOf(e);
		e.getEndVertex().unsetEndOf(e);
		Edge[] endStartingEdges = e.getEndVertex().getStartOf().toArray(new Edge[1]);
		for(int i=0; i < endStartingEdges.length; i++) {
			if(endStartingEdges[i] != null) {
				endStartingEdges[i].setStartVertex(e.getStartVertex());
				endStartingEdges[i].getEndVertex().unsetEndOf(e);
			}
		}
		Edge[] endEndingEdges = e.getEndVertex().getEndOf().toArray(new Edge[1]);
		for(int i=0; i < endEndingEdges.length; i++) {
			if(endEndingEdges[i] != null) {
				endEndingEdges[i].setEndVertex(e.getStartVertex());
				endEndingEdges[i].getStartVertex().unsetStartOf(e);
			}
		}
//		for(Edge endStartingEdge : e.getEndVertex().getStartOf()) {
//			endStartingEdge.setStartVertex(e.getStartVertex());
//			endStartingEdge.getEndVertex().unsetEndOf(e);
//		}
//		for(Edge endEndingEdge : e.getEndVertex().getEndOf()) {
//			endEndingEdge.setEndVertex(e.getStartVertex());
//			endEndingEdge.getStartVertex().unsetStartOf(e);
//		}
	}
}
