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

//TODO Avoid collisions during simplification
/**
 * This converter is based on an algorithm which sorts nodes in a Cartesian coordinate plane.
 * Then these nodes are grouped by rows and columns depending of their proximity.
 * @author Adrien PAVIE
 */
public class IntervalConverter implements NetworkConverter {
//CONSTANTS
	/** This percentage (from 0 to 100) defines the range to merge following coordinates. **/
	private static final double REDUCE_PERCENT = 1;
	
//ATTRIBUTES
	/** The list of X coordinates, sorted **/
	private List<Double> sortedXCoordinates;
	
	/** The list of Y coordinates, sorted **/
	private List<Double> sortedYCoordinates;
	
	/** The map of X coordinates to representable X indexes **/
	private Map<Double,Integer> indexesXCoordinates;
	
	/** The map of Y coordinates to representable Y indexes **/
	private Map<Double,Integer> indexesYCoordinates;

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
					indexesXCoordinates.get(v.getLongitude()),
					indexesYCoordinates.get(v.getLatitude())
//					sortedXCoordinates.indexOf(v.getLongitude()),
//					sortedYCoordinates.indexOf(v.getLatitude())
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
		
		//Create map to simplify graph representation
		indexesXCoordinates = new HashMap<Double,Integer>(sortedXCoordinates.size());
		indexesYCoordinates = new HashMap<Double,Integer>(sortedYCoordinates.size());
		indexesXCoordinates.put(sortedXCoordinates.get(0), 0);
		indexesYCoordinates.put(sortedYCoordinates.get(0), 0);
		
		//Define coordinates interval
		double xInterval = sortedXCoordinates.get(sortedXCoordinates.size()-1) - sortedXCoordinates.get(0);
		double yInterval = sortedYCoordinates.get(sortedYCoordinates.size()-1) - sortedYCoordinates.get(0);

		//Reduce coordinates
		for(int i=1; i < sortedXCoordinates.size(); i++) {
			//Reduce X coordinates
			double currentX = sortedXCoordinates.get(i);
			double prevX = sortedXCoordinates.get(i-1);
			
			if((currentX-prevX)/xInterval*100 <= REDUCE_PERCENT) {
				indexesXCoordinates.put(currentX, indexesXCoordinates.get(prevX));
			} else {
				indexesXCoordinates.put(currentX, indexesXCoordinates.get(prevX)+1);
			}
			
			//Reduce Y coordinates
			double currentY = sortedYCoordinates.get(i);
			double prevY = sortedYCoordinates.get(i-1);
			
			if((currentY-prevY)/yInterval*100 <= REDUCE_PERCENT) {
				indexesYCoordinates.put(currentY, indexesYCoordinates.get(prevY));
			} else {
				indexesYCoordinates.put(currentY, indexesYCoordinates.get(prevY)+1);
			}
		}
	}
}
