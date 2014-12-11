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

package info.pavie.opennetworkmap.model.draw.network;

import info.pavie.opennetworkmap.model.draw.Spatializable;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.Map;

/**
 * A layer is a spatialized grid (see {@link FlexibleGrid}) which contains {@link Vertex} objects.
 * It allows to create diagrams from connected networks.
 */
public class Layer implements Spatializable {
//ATTRIBUTES
	/** The layer grid **/
	private FlexibleGrid<Vertex> grid;
	/** The layer average latitude **/
	private Double avgLat;
	/** The layer average longitude **/
	private Double avgLon;

//CONSTRUCTOR
	/**
	 * Creates a new layer with one vertex
	 * @param first The first vertex
	 */
	public Layer(Vertex first) {
		grid = new FlexibleGrid<Vertex>(first);
		avgLat = first.getLatitude();
		avgLon = first.getLongitude();
	}
	
//ACCESSORS
	/**
	 * @return The layer width
	 */
	public int getWidth() {
		return grid.getWidth();
	}
	
	/**
	 * @return The layer height
	 */
	public int getHeight() {
		return grid.getHeight();
	}
	
	/**
	 * Returns the wanted vertex
	 * @param row The row of the vertex
	 * @param col The column of the vertex
	 * @return The vertex, or null if the cell is empty
	 */
	public Vertex getVertex(int row, int col) {
		return grid.get(row, col);
	}
	
	/**
	 * @return The vertices positions, [x, y] with x = column and y = row
	 */
	public Map<Vertex,int[]> getVerticesPositions() {
		return grid.getPositions();
	}
	
//	/**
//	 * Get the direction of an edge in this layer, in radians
//	 * @param e The edge
//	 * @return The direction, in radians (0 = West to East)
//	 */
//	public double getEdgeDirection(Edge e) {
//		Map<Vertex,int[]> verticesPos = getVerticesPositions();
//		int[] posFrom = verticesPos.get(e.getStartVertex());
//		int[] posTo = verticesPos.get(e.getEndVertex());
//		double angleRad = Math.atan2(posTo[0]-posFrom[0], posTo[1]-posFrom[1]);
//		
//		return angleRad;
//	}
	
	@Override
	public double getLatitude() {
		if(avgLat == null) {
			processCoordinates();
		}
		return avgLat;
	}

	@Override
	public double getLongitude() {
		if(avgLon == null) {
			processCoordinates();
		}
		return avgLon;
	}
	
	@Override
	public String toString() {
		return grid.toString();
	}

//MODIFIERS
	/**
	 * Adds a vertex in the layer
	 * @param v The vertex to add
	 * @param from A vertex next to the vertex to add
	 */
	public void add(Vertex v, Vertex from) {
		grid.add(v, from);
		avgLat = null;
		avgLon = null;
	}
	
//OTHER METHODS
	/**
	 * Calculates actual average coordinate.
	 */
	private void processCoordinates() {
		double[] result = new double[2];
		
		for(Vertex v : grid.getPositions().keySet()) {
			result[0] += v.getLatitude();
			result[1] += v.getLongitude();
		}
		
		avgLat = result[0] / grid.getPositions().size();
		avgLon = result[1] / grid.getPositions().size();
		
	}
}
