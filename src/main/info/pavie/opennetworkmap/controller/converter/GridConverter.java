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

import info.pavie.opennetworkmap.model.draw.RepresentableNetwork;
import info.pavie.opennetworkmap.model.draw.network.Canvas;
import info.pavie.opennetworkmap.model.draw.network.FlexibleGrid;
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.HashSet;
import java.util.Set;

/**
 * This converter is based on {@link FlexibleGrid} and {@link Canvas}.
 * @author Adrien PAVIE
 */
public class GridConverter implements NetworkConverter {
//ATTRIBUTES
	/** Already visited vertices **/
	private Set<Vertex> visitedVertices;
	
//OTHER METHODS
	@Override
	public RepresentableNetwork createRepresentation(Network n) {
		//Check given network
		if(n.getVertices().size() == 0) {
			throw new RuntimeException("The given network is empty");
		}
		
		//Init vertices
		Set<Vertex> vertices = n.getVertices();
		Canvas result = null;
		visitedVertices = new HashSet<Vertex>();
		
		//Add each vertex to the canvas
		do {
			Vertex start = vertices.iterator().next();
			result = addVertexToCanvas(null, start, result);
			vertices.removeAll(visitedVertices);
		} while(vertices.size() > 0);
		
		result.setLastLayerDone();
		
		return result;
	}
	
	/**
	 * Adds the next vertex in the canvas, next to "from" vertex.
	 * Then calls recursively itself to add following vertices.
	 * @param from The already contained vertex
	 * @param next The next vertex to add
	 * @param c The canvas
	 * @return The edited canvas
	 */
	private Canvas addVertexToCanvas(Vertex from, Vertex v, Canvas c) {
		//Create canvas if not defined
		if(c == null) {
			c = new Canvas(v);
		}
		//Create new layer
		else if(from == null) {
			c.setLastLayerDone();
			c.addLayer(v);
		}
		//Add next vertex else
		else {
			c.add(v, from);
		}
		
		//Add following vertices (with depth first search)
		visitedVertices.add(v);
		
		for(Vertex next : v.getLinkedVertices()) {
			if(!visitedVertices.contains(next)) {
				c = addVertexToCanvas(v, next, c);
			}
		}
		
		return c;
	}
}
