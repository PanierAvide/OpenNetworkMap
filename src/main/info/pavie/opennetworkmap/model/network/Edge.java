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

import java.util.Map;

/**
 * An edge allows to link vertices in {@link Network}.
 */
public class Edge extends Component {
//ATTRIBUTES
	/** The vertex where this edge starts **/
	private Vertex from;
	/** The vertex where this edge ends **/
	private Vertex to;

//CONSTRUCTOR
	/**
	 * Class constructor (also updates vertices)
	 * @param label The label or name
	 * @param tags The tags
	 * @param start The vertex where the edge starts
	 * @param end The vertex where the edge ends
	 */
	public Edge(String label, Map<String, String> tags, Vertex start, Vertex end) {
		super(label, tags);
		from = start;
		to = end;
		from.setStartOf(this);
		to.setEndOf(this);
		this.tags.put("link", "yes");
	}

//ACCESSORS
	/**
	 * @return The vertex where this edge starts
	 */
	public Vertex getStartVertex() {
		return from;
	}
	
	/**
	 * @return The vertex where this edge ends
	 */
	public Vertex getEndVertex() {
		return to;
	}

	@Override
	public String toString() {
		return "Edge "+((label != null) ? label : "");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (super.equals(obj) && from.equals(((Edge)obj).from) && to.equals(((Edge)obj).to));
	}

//MODIFIERS
	/**
	 * Change the start vertex of this edge
	 * @param v The new start vertex
	 */
	public void setStartVertex(Vertex v) {
		from = v;
		from.setStartOf(this);
	}
	
	/**
	 * Change the end vertex of this edge
	 * @param v The new end vertex
	 */
	public void setEndVertex(Vertex v) {
		to = v;
		to.setEndOf(this);
	}
}
