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

import java.util.HashMap;
import java.util.Map;

/**
 * A component is a generic network component (ie a {@link Vertex} or an {@link Edge}).
 * It has a label (generally its name) and tags (which describes it).
 * Tags are useful to render more precisely components.
 * Common tags: color, type=first/second/third
 */
public abstract class Component {
//ATTRIBUTES
	/** The component label, generally its name **/
	protected String label;
	/** The component tags, to describe it precisely with pairs of keys/values **/
	protected Map<String,String> tags;
	
//CONSTRUCTOR
	/**
	 * Class constructor
	 * @param label The label
	 * @param tags The tags
	 */
	public Component(String label, Map<String, String> tags) {
		this.label = label;
		this.tags = (tags != null) ? new HashMap<String,String>(tags) : new HashMap<String,String>();
	}

//ACCESSORS
	/**
	 * @return The label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * @return The tags of the object
	 */
	public Map<String,String> getTags() {
		return tags;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		Component other = (Component) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		return true;
	}
}
