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

import info.pavie.opennetworkmap.model.draw.Spatializable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A vertex is the node element of a {@link Network}.
 */
public class Vertex extends Component implements Spatializable {
//ATTRIBUTES
	/** The latitude **/
	private double lat;
	/** The longitude **/
	private double lon;
	/** The set of edges pointing to this vertex. **/
	private Set<Edge> endOf;
	/** The set of edges starting with this vertex. **/
	private Set<Edge> startOf;
	/** The set of following vertices **/
//	private Set<Vertex> followingVertices;
	/** The set of previous vertices **/
//	private Set<Vertex> previousVertices;
	/** The set of linked vertices **/
	private Set<Vertex> linkedVertices;

//CONSTRUCTOR
	/**
	 * Class constructor
	 * @param label The vertex label
	 * @param tags The vertex tags
	 * @param lat The vertex latitude
	 * @param lon The vertex longitude
	 */
	public Vertex(String label, Map<String,String> tags, double lat, double lon) {
		super(label, tags);
		this.lat = lat;
		this.lon = lon;
		endOf = new HashSet<Edge>();
		startOf = new HashSet<Edge>();
//		followingVertices = new LinkedHashSet<Vertex>();
//		previousVertices = new LinkedHashSet<Vertex>();
		linkedVertices = new HashSet<Vertex>();
		this.tags.put("node", "yes");
	}

//ACCESSORS
	@Override
	public double getLatitude() {
		return lat;
	}
	
	@Override
	public double getLongitude() {
		return lon;
	}
	
//	/**
//	 * @return The vertices which are the end of the edges which start at the current vertex.
//	 */
//	public Set<Vertex> getFollowingVertices() {
//		return followingVertices;
//	}
//	
//	/**
//	 * @return The vertices which are the start of the edges which end at the current vertex.
//	 */
//	public Set<Vertex> getPreviousVertices() {
//		return previousVertices;
//	}
	
	/**
	 * @return The vertices linked to this vertex
	 */
	public Set<Vertex> getLinkedVertices() {
		return new HashSet<Vertex>(linkedVertices);
	}
	
	/**
	 * @return The edges starting at this vertex
	 */
	public Set<Edge> getStartOf() {
		return new HashSet<Edge>(startOf);
	}
	
	/**
	 * @return The edges ending at this vertex
	 */
	public Set<Edge> getEndOf() {
		return new HashSet<Edge>(endOf);
	}
	
	@Override
	public String toString() {
		return "Vertex"+((label != null) ? " "+label : "");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || (super.equals(obj) && lat == ((Vertex)obj).lat && lon == ((Vertex)obj).lon);
	}

//MODIFIERS
	/**
	 * Sets this vertex as the start node of given edge
	 * @param e The edge
	 */
	public void setStartOf(Edge e) {
		startOf.add(e);
//		System.out.println("Set "+toString()+" "+this.getStartOf());
//		followingVertices.add(e.getEndVertex());
		linkedVertices.add(e.getEndVertex());
//		System.out.println("Add "+toString()+" edge "+e+" ends "+e.getEndVertex()+" size "+linkedVertices.size());
	}
	
	/**
	 * Sets this vertex as the end node of given edge
	 * @param e The edge
	 */
	public void setEndOf(Edge e) {
		endOf.add(e);
//		previousVertices.add(e.getStartVertex());
		linkedVertices.add(e.getStartVertex());
//		System.out.println("Add "+toString()+" edge "+e+" starts "+e.getStartVertex()+" size "+linkedVertices.size());
	}
	
	/**
	 * Unsets this vertex as the start node of given edge
	 * @param e The edge
	 */
	public void unsetStartOf(Edge e) {
		startOf.remove(e);
		reevaluateFollowingVertices();
	}
	
	/**
	 * Unsets this vertex as the end node of given edge
	 * @param e The edge
	 */
	public void unsetEndOf(Edge e) {
		endOf.remove(e);
		reevaluateFollowingVertices();
	}
	
	/**
	 * Evaluates following vertices
	 */
	private void reevaluateFollowingVertices() {
		linkedVertices.clear();
		for(Edge e : startOf) {
			linkedVertices.add(e.getEndVertex());
		}
		for(Edge e : endOf) {
			linkedVertices.add(e.getStartVertex());
		}
	}
}
