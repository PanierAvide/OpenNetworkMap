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

package info.pavie.opennetworkmap.controller.converter;

import info.pavie.opennetworkmap.model.draw.RepresentableEdge;
import info.pavie.opennetworkmap.model.draw.RepresentableNetwork;
import info.pavie.opennetworkmap.model.draw.RepresentableVertex;
import info.pavie.opennetworkmap.model.draw.SimpleRepresentableNetwork;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

//TODO Reduce indexes of coordinates (to simplify representation)
/**
 * This converter is based on an algorithm which sorts nodes in a Cartesian coordinate plane.
 * Then these nodes are grouped by rows and columns depending of their proximity.
 * @author Adrien PAVIE
 */
public class IntervalConverter implements NetworkConverter {
//ATTRIBUTES
	/** The list of X coordinates, sorted **/
	private List<Double> sortedXCoordinates;
	
	/** The list of Y coordinates, sorted **/
	private List<Double> sortedYCoordinates;

//OTHER METHODS
	@Override
	public RepresentableNetwork createRepresentation(Network n) {
		sortCoordinates(n.getVertices());
		
		//Init sets of representable elements
		Map<Vertex,RepresentableVertex> mapVertices = new HashMap<Vertex,RepresentableVertex>(n.getVertices().size());
		Set<RepresentableEdge> rEdges = new HashSet<RepresentableEdge>(n.getEdges().size());
		
		//Create vertices representation
		RepresentableVertex rVertex;
		for(Vertex v : n.getVertices()) {
			rVertex = new RepresentableVertex(
					v,
					sortedXCoordinates.indexOf(v.getLongitude()),
					sortedYCoordinates.indexOf(v.getLatitude())
					);
			mapVertices.put(v, rVertex);
		}
		
		//For each edge, create representation and associate vertices
		for(Edge e : n.getEdges()) {
			rEdges.add(
					new RepresentableEdge(
							e,
							mapVertices.get(e.getStartVertex()),
							mapVertices.get(e.getEndVertex())
							)
					);
		}
		
		//Create representable network
		RepresentableNetwork rNetwork =
				new SimpleRepresentableNetwork(
						new HashSet<RepresentableVertex>(mapVertices.values()),
						rEdges
						);
		
		return rNetwork;
	}

	/**
	 * Sort coordinates of vertices and fill sorted coordinates lists.
	 * @param vertices The network vertices
	 */
	private void sortCoordinates(Set<Vertex> vertices) {
		//Reset sorted coordinates lists
		sortedXCoordinates = new ArrayList<Double>();
		sortedYCoordinates = new ArrayList<Double>();
		
		//Add each vertex coordinates in lists
		for(Vertex v : vertices) {
			sortedXCoordinates.add(v.getLongitude());
			sortedYCoordinates.add(v.getLatitude());
		}
		
		//Sort lists
		Collections.sort(sortedXCoordinates);
		Collections.sort(sortedYCoordinates);
	}
}
