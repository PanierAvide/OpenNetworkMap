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
import info.pavie.basicosmparser.model.Way;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Creates subway networks, with stations.
 * Only works if subway lines elements (ie stations and rails) are in a "route" relation. 
 * Tags: railway=subway / railway=station / name=*
 */
public class SubwayStandardizer implements NetworkStandardizer {
//ATTRIBUTES
//	private Map<Bounds,Vertex> stationsBounds;
	private Map<String,Vertex> stationsNodes;
	/** How many ways are connected to the given node (represented by its ID) **/
	private Map<String,Integer> linkedWaysOnNode;
	private Map<String,Node> linesNodes;
	
//OTHER METHODS
	@Override
	public Network standardize(Map<String, Element> elements) {
		Network result = new Network();
		
		elements = keepInterestingData(elements);
		Vertex start, end;
		
		//Check each railway line
		for(Element e : elements.values()) {
			/*
			 * We add a line if one of its vertices is linked to a railway station
			 * or to another line.
			 */
			Way line = (Way) e;
			
			for(int i=0; i < line.getNodes().size(); i++) {
				//Find first vertex
				start = null;
				do {
					Node current = line.getNodes().get(i);
					
					//Is it a station node ?
					if(stationsNodes.containsKey(current.getId())) {
						start = stationsNodes.get(current.getId());
					}
					else {
//						//Check stations bounds
//						for(Bounds b : stationsBounds.keySet()) {
//							if(b.isInBounds(current.getLat(), current.getLon())) {
//								start = stationsBounds.get(b);
//								break;
//							}
//						}
						
						//If no vertex found, check if it's a node linking two lines
						if(start == null
								&&linkedWaysOnNode.containsKey(current.getId())
								&& linkedWaysOnNode.get(current.getId()) > 1) {
								
							Map<String,String> startTags = new HashMap<String,String>();
							startTags.put("type", "third");
							start = new Vertex(
											current.getTags().get("ref"),
											startTags,
											current.getLat(),
											current.getLon()
											);
							stationsNodes.put(current.getId(), start);
						}
					}
					i++;
				} while(start == null && i < line.getNodes().size());
				
				//Find following vertex
				end = null;
				while(end == null && i < line.getNodes().size()) {
					Node current = line.getNodes().get(i);
					
					//Is it a station node ?
					if(stationsNodes.containsKey(current.getId())) {
						end = stationsNodes.get(current.getId());
					}
					else {
//						//Check stations bounds
//						for(Bounds b : stationsBounds.keySet()) {
//							if(b.isInBounds(current.getLat(), current.getLon())) {
//								end = stationsBounds.get(b);
//								break;
//							}
//						}
						
						//If no vertex found, check if it's a node linking two lines
						if(end == null
								&&linkedWaysOnNode.containsKey(current.getId())
								&& linkedWaysOnNode.get(current.getId()) > 1) {
								
							Map<String,String> startTags = new HashMap<String,String>();
							startTags.put("type", "third");
							end = new Vertex(
											current.getTags().get("ref"),
											startTags,
											current.getLat(),
											current.getLon()
											);
							stationsNodes.put(current.getId(), end);
						}
					}
					i++;
				}
				
				//Create edge
				if(start != null && end != null) {
					//Define tags
					Map<String,String> lineTags = new HashMap<String,String>(1);
					if(line.getTags().get("railway").equals("subway")) {
						lineTags.put("type", "first");
					}
//					else if(line.getTags().get("railway").equals("cable")) {
//						lineTags.put("type", "second");
//					}
//					else {
//						lineTags.put("type", "third");
//					}
					if(line.getTags().containsKey("colour")) {
						lineTags.put("colour", line.getTags().get("colour"));
					}
					
					Edge lineEdge = new Edge(
										line.getTags().get("ref"),
										lineTags,
										start,
										end);
					result.addEdge(lineEdge);
//					System.out.println("Edges: "+result.getEdges().size());
					i -= 2; //This is done to make next loop start at the current end node
				}
			}
		}
		
		//Remove extra edges
		boolean edited;
		do {
			edited = false;
			Iterator<Vertex> vertices = result.getVertices().iterator();
			do {
				if(vertices.hasNext()) {
					Vertex v = vertices.next();
					if(v.getTags().get("type").equals("third")
							&& v.getLinkedVertices().size() == 2) {
						
						Edge e, e2;
						Iterator<Edge> itEdge;
						switch(v.getEndOf().size()) {
							case 1:
								e = v.getEndOf().iterator().next();
								result.simplifyEdge(e);
								edited = true;
								break;
							case 2:
								itEdge = v.getEndOf().iterator();
								e = itEdge.next();
								e2 = itEdge.next();
								result.addEdge(new Edge(
										e.getLabel(),
										e.getTags(),
										e.getStartVertex(),
										e2.getStartVertex()
										));
								result.removeEdge(e);
								result.removeEdge(e2);
								break;
							case 0:
								itEdge = v.getStartOf().iterator();
								e = itEdge.next();
								e2 = itEdge.next();
								result.addEdge(new Edge(
										e.getLabel(),
										e.getTags(),
										e.getEndVertex(),
										e2.getEndVertex()
										));
								result.removeEdge(e);
								result.removeEdge(e2);
								break;
						}
					}
				}
			} while(!edited && vertices.hasNext());
		} while(edited);
		
		return result;
	}

	/**
	 * Keeps only related elements in data
	 * @param elements The input data
	 * @return The elements we want to keep
	 */
	private Map<String,Element> keepInterestingData(Map<String,Element> elements) {
		Map<String,Element> result = new HashMap<String,Element>();

		//Init stations
//		stationsBounds = new HashMap<Bounds,Vertex>();
		stationsNodes = new HashMap<String,Vertex>();
		linkedWaysOnNode = new HashMap<String,Integer>();
		linesNodes = new HashMap<String,Node>();
		
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
									&& r.getTags().get("route").equals("subway")) {
								
								//We keep this relation only if not a variant of a route_master
								if(!excludedRoutes.contains(r)) {
									routes.add(r);
								}
							}
							break;
						
						//Line master route
						case "route_master":
							if(r.getTags().containsKey("route_master")
									&& r.getTags().get("route_master").equals("subway")) {
								
								//We keep only the first variant
								routes.add((Relation) r.getMembers().get(0));
								
								//And we exclude each other variant
								for(int i=1; i < r.getMembers().size(); i++) {
									excludedRoutes.add((Relation) r.getMembers().get(i));
									routes.remove((Relation) r.getMembers().get(i));
								}
							}
							break;
					}
				}
			}
		}
		
		//Check elements
		for(Relation r : routes) {
//			for(String id : elements.keySet()) {
//				Element current = elements.get(id);
			for(Element current : r.getMembers()) {
				
				//Case of stations
				if(current.getTags().containsKey("railway")
						&& current.getTags().get("railway").equals("station")) {
					
					//Define tags
					Map<String,String> tags = new HashMap<String,String>(1);
					if(current.getTags().get("railway").equals("station")) {
						tags.put("type", "first");
					}/* else {
						tags.put("type", "second");
					}*/
					
					//Create vertex
					Vertex v;
					if(current instanceof Node) {
						Node n = (Node) current;
						v = new Vertex(
								getStationName(n),
								tags,
								n.getLat(),
								n.getLon()
								);
						stationsNodes.put(n.getId(), v);
					}
	//				else if(current instanceof Way) {
	//					Way w = (Way) current;
	//					Bounds b = new Bounds(w);
	//					v = new Vertex(
	//							getStationName(w),
	//							tags,
	//							b.getAverageLat(),
	//							b.getAverageLon()
	//							);
	//					stationsBounds.put(b, v);
	//				}
				}
				//Case of railway lines
				else if(current instanceof Way
						&& current.getTags().containsKey("railway")
						&& current.getTags().get("railway").equals("subway")
						&& current.getTags().get("service") == null) {
					
					//Add line in result
					result.put(current.getId(), current);
					
					//Increment connected ways to line nodes
					Way w = (Way) current;
					
					//Add color if available
					if(r.getTags().containsKey("colour")) {
						w.addTag("colour", r.getTags().get("colour"));
					}
					
					for(Node lineNode : w.getNodes()) {
						if(linkedWaysOnNode.containsKey(lineNode.getId())) {
							linkedWaysOnNode.put(lineNode.getId(), linkedWaysOnNode.get(lineNode.getId())+1);
						} else {
							linkedWaysOnNode.put(lineNode.getId(), 1);
							linesNodes.put(lineNode.getId(), lineNode);
						}
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the name of a railway station
	 * @param e The element object
	 * @return Its name, or its reference if no name, or null if no name or reference
	 */
	private String getStationName(Element e) {
		return (e.getTags().containsKey("name")) ? e.getTags().get("name") : e.getTags().get("ref");
	}
}