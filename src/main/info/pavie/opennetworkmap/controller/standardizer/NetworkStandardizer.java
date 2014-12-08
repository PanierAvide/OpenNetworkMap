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

package info.pavie.opennetworkmap.controller.standardizer;

import info.pavie.basicosmparser.model.Element;
import info.pavie.opennetworkmap.model.network.Network;

import java.util.Map;

/**
 * A network standardizer allows to transform a lot of OSM data into a standard {@link Network}.
 * It depends of the elements you want to keep, for example keep only bus lines or water pipelines.
 * A class which implements this interface is able to give you a standard network for your purpose.
 */
public interface NetworkStandardizer {
//OTHER METHODS
	/**
	 * Creates a standard {@link Network} from OSM {@link Element}s.
	 * @param elements The OSM data
	 * @return The wanted network
	 */
	Network standardize(Map<String,Element> elements);
}
