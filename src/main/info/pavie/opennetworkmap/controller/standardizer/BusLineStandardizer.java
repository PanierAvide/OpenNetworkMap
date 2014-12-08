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
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Relation;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Creates bus lines networks, with bus stops.
 * Only works if lines elements (ie stations and roads) are in a "route" relation. 
 * Tags: public_transport=*
 */
public class BusLineStandardizer implements NetworkStandardizer {
//ATTRIBUTES
	private Map<String,Vertex> stopsNodes;
	
//OTHER METHODS
	@Override
	public Network standardize(Map<String, Element> elements) {
		Network result = new Network();
		
		elements = keepInterestingData(elements);
		stopsNodes = new HashMap<String,Vertex>();

		/*
		 * We have kept only route relations
		 * So we look each route, and create edges between each stop member
		 */
		for(Element e : elements.values()) {
			Relation r = (Relation) e;
			
			Vertex last = null;
			String lastName = null;
			
			//For each relation member
			for(Element member : r.getMembers()) {
				String role = r.getMemberRole(member);
				if(role == null) { role = ""; }
				
				switch(role) {
					//Route path
					case "":
						break;
						
					//Route platform (added only if no associated stop)
					case "platform_exit_only":
					case "platform":
					case "platform_entry_only":
						String platformName = getStationName(member);
						if(platformName.equals(lastName)) {
							break;
						}
						
					//Route stop
					case "stop_exit_only":
					case "stop":
					case "stop_entry_only":
						//Create vertex if new stop
//						if(!stopsNodes.containsKey(member.getId())) {
						String stationName = getStationName(member);
						if(!stopsNodes.containsKey(stationName)) {
							if(member instanceof Node) {
								Map<String,String> tags = new HashMap<String,String>(1);
								
								//Distinguish each kind of stop
								switch(role) {
									case "stop_exit_only":
										tags.put("type", "third");
										break;
									case "stop":
										tags.put("type", "first");
										break;
									case "stop_entry_only":
										tags.put("type", "second");
										break;
								}
								
								stopsNodes.put(stationName/*member.getId()*/, new Vertex(
										stationName,
										tags,
										((Node) member).getLat(),
										((Node) member).getLon()
										));
							}
						}
						
						//Create edge if not first stop
						if(last != null) {
							Map<String,String> lineTags = new HashMap<String,String>();
							lineTags.put("type", "first");
							
							if(e.getTags().containsKey("colour")) {
								lineTags.put("colour", e.getTags().get("colour"));
							}
							
							result.addEdge(new Edge(
									e.getTags().get("ref"),
									lineTags,
									last,
									stopsNodes.get(stationName) //member.getId())
									));
						}
						
						//Define current stop as last
						last = stopsNodes.get(stationName); //member.getId());
						lastName = stationName; //stopsNodes.get(member.getId()).getLabel();
						
						break;
				}
			}
		}
		
		return result;
	}

	/**
	 * Keeps only related elements in data
	 * @param elements The input data
	 * @return The elements we want to keep
	 */
	private Map<String,Element> keepInterestingData(Map<String,Element> elements) {
		Map<String,Element> result = new HashMap<String,Element>();

		//Init routes list
		Set<Relation> routes = new HashSet<Relation>();
		Set<Relation> excludedRoutes = new HashSet<Relation>();
		
		//Keep only route/route_master elements
		for(String id : elements.keySet()) {
			Element current = elements.get(id);
			if(current instanceof Relation) {
				Relation r = (Relation) current;
				if(r.getTags().containsKey("type")) {
					switch(r.getTags().get("type")) {
						//Line route
						case "route":
							if(r.getTags().containsKey("route")
									&& r.getTags().get("route").equals("bus")) {
								
								//We keep this relation only if not a variant of a route_master
								if(!excludedRoutes.contains(r)) {
									routes.add(r);
								}
							}
							break;
						
						//Line master route
						case "route_master":
							if(r.getTags().containsKey("route_master")
									&& r.getTags().get("route_master").equals("bus")) {
								
								//We keep only the first variant
								Relation firstRoute = (Relation) r.getMembers().get(0);
								addTagFromMaster(firstRoute, r, "colour");
								addTagFromMaster(firstRoute, r, "ref");
								addTagFromMaster(firstRoute, r, "name");
								routes.add(firstRoute);
								
								//And we exclude each other variant
								for(int i=1; i < r.getMembers().size(); i++) {
									if(r.getMembers().get(i) instanceof Relation) {
										excludedRoutes.add((Relation) r.getMembers().get(i));
										routes.remove(r.getMembers().get(i));
									}
								}
							}
							break;
					}
				}
			}
		}
		
		for(Relation r : routes) {
			result.put(r.getId(), r);
		}
		
		return result;
	}
	
	/**
	 * Returns the name of a railway station
	 * @param e The element object
	 * @return Its name, or its reference if no name, or empty string if no name or reference
	 */
	private String getStationName(Element e) {
		String result = "";
		
		if(e.getTags().containsKey("name")) {
			result = e.getTags().get("name");
		}
		else if(e.getTags().containsKey("ref")) {
			result = e.getTags().get("ref");
		}
		else {
			//TODO Try to find stop_area relation
		}
		
		return result;
	}
	
	/**
	 * Adds a given tag from route_master to route if route doesn't contain it already
	 * @param route The route relation
	 * @param master The route_master relation
	 * @param key The key of the tag to add
	 */
	private void addTagFromMaster(Relation route, Relation master, String key) {
		if(master.getTags().containsKey(key)) {
			if(!route.getTags().containsKey(key)) {
				route.addTag(key, master.getTags().get(key));
			}
		}
	}
}