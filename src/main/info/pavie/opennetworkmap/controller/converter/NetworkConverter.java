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
import info.pavie.opennetworkmap.model.network.Network;

/**
 * A network converter will convert a {@link Network} into a {@link RepresentableNetwork}.
 * This allows to create representations of given network (to export them later).
 */
public interface NetworkConverter {
//OTHER METHODS
	/**
	 * Creates a representation of the given network (to make it exportable)
	 * @param n The network
	 * @return The representable network
	 */
	RepresentableNetwork createRepresentation(Network n);
}
