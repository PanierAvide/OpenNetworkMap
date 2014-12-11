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

import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.Set;

/**
 * A representable network is a {@link Network} which can be drawn on a plane surface.
 * Each element of the network has defined coordinates which can used to draw the network.
 * This interface allows to use different algorithms for network representations, see implementations for details.
 * @author Adrien PAVIE
 */
public interface RepresentableNetwork {
//ACCESSORS
	/**
	 * @return The set of the representable vertices
	 */
	Set<RepresentableVertex> getRepresentableVertices();
	
	/**
	 * @return The set of the representable edges
	 */
	Set<RepresentableEdge> getRepresentableEdges();
	
	/**
	 * @param v The vertex to find
	 * @return The representation of the given vertex
	 */
	RepresentableVertex getRepresentableVertex(Vertex v);
}
