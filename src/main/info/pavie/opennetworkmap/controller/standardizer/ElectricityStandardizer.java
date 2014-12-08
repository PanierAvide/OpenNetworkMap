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
import info.pavie.basicosmparser.model.Way;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates standard networks from electricity data.
 * Nodes are stations, links are power lines.
 * Tags: power=generator / power=substation / power=line / power=minor_line / power=cable
 *
 */
public class ElectricityStandardizer implements NetworkStandardizer {
//ATTRIBUTES
	private Map<Bounds,Vertex> stationsBounds;
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
		
		//Check each power line
		for(Element e : elements.values()) {
			/*
			 * We add a line if one of its vertices is linked to a power station
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
						//Check stations bounds
						for(Bounds b : stationsBounds.keySet()) {
							if(b.isInBounds(current.getLat(), current.getLon())) {
								start = stationsBounds.get(b);
								break;
							}
						}
						
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
						//Check stations bounds
						for(Bounds b : stationsBounds.keySet()) {
							if(b.isInBounds(current.getLat(), current.getLon())) {
								end = stationsBounds.get(b);
								break;
							}
						}
						
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
					if(line.getTags().get("power").equals("line")) {
						lineTags.put("type", "first");
					}
					else if(line.getTags().get("power").equals("cable")) {
						lineTags.put("type", "second");
					}
					else {
						lineTags.put("type", "third");
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
		stationsBounds = new HashMap<Bounds,Vertex>();
		stationsNodes = new HashMap<String,Vertex>();
		linkedWaysOnNode = new HashMap<String,Integer>();
		linesNodes = new HashMap<String,Node>();
		
		//Check elements
		for(String id : elements.keySet()) {
			Element current = elements.get(id);
			
			//Case of generator/substation
			if(current.getTags().containsKey("power")
					&& (current.getTags().get("power").equals("generator")
							|| current.getTags().get("power").equals("substation")
							|| current.getTags().get("power").equals("station")
							|| current.getTags().get("power").equals("plant"))) {
				
				//Define tags
				Map<String,String> tags = new HashMap<String,String>(1);
				if(current.getTags().get("power").equals("generator")) {
					tags.put("type", "first");
				} else {
					tags.put("type", "second");
				}
				
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
				else if(current instanceof Way) {
					Way w = (Way) current;
					Bounds b = new Bounds(w);
					v = new Vertex(
							getStationName(w),
							tags,
							b.getAverageLat(),
							b.getAverageLon()
							);
					stationsBounds.put(b, v);
				}
			}
			//Case of power lines
			else if(current instanceof Way
					&& current.getTags().containsKey("power")
					&& (current.getTags().get("power").equals("line")
							|| current.getTags().get("power").equals("cable")
							|| current.getTags().get("power").equals("minor_line"))) {
				
				//Add line in result
				result.put(current.getId(), current);
				
				//Increment connected ways to line nodes
				Way w = (Way) current;
				
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
		
		return result;
	}
	
	/**
	 * Returns the name of a power station or generator
	 * @param e The element object
	 * @return Its name, or its reference if no name, or null if no name or reference
	 */
	private String getStationName(Element e) {
		return (e.getTags().containsKey("name")) ? e.getTags().get("name") : e.getTags().get("ref");
	}

//INNER CLASS Bounds
	/**
	 * Bounding box of an {@link Element}
	 */
	private class Bounds {
	//ATTRIBUTES
		/** Bounding box coordinates **/
		private double minlat, minlon, maxlat, maxlon;
		
	//CONSTRUCTOR
		private Bounds(Way w) {
			List<Node> nodes = w.getNodes();
			minlat = maxlat = nodes.get(0).getLat();
			minlon = maxlon = nodes.get(0).getLon();
			
			for(int i=1; i < nodes.size(); i++) {
				Node n = nodes.get(i);
				if(n.getLat() < minlat) { minlat = n.getLat(); }
				else if(n.getLat() > maxlat) { maxlat = n.getLat(); }
				if(n.getLon() < minlon) { minlon = n.getLon(); }
				else if(n.getLon() > maxlon) { maxlon = n.getLon(); }
			}
		}
	
	//ACCESSORS
		private double getAverageLon() {
			return (maxlon-minlon)/2 + minlon;
		}
		
		private double getAverageLat() {
			return (maxlat-minlat)/2 + minlat;
		}
		
		/**
		 * Are coordinates in the bounding box ?
		 * @param lat The latitude
		 * @param lon The longitude
		 * @return True if in the box
		 */
		private boolean isInBounds(double lat, double lon) {
			return lat >= minlat && lat <= maxlat && lon >= minlon && lon <= maxlon;
		}
	}
}
