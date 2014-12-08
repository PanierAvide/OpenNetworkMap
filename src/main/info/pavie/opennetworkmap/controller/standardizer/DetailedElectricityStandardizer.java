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
import java.util.Map;

//TODO Complete tags
/**
 * Creates detailed electricity networks (all poles displayed).
 * Tags: power=minor_line / power=pole / power=line / power=cable / ref=*
 */
public class DetailedElectricityStandardizer implements NetworkStandardizer {
//ATTRIBUTES
	/** Already created vertices (key: OSM Node ID) **/
	private Map<String, Vertex> vertices;
	
//OTHER METHODS
	@Override
	public Network standardize(Map<String, Element> elements) {
		Network result = new Network();
		
		vertices = new HashMap<String,Vertex>();
		elements = keepInterestingData(elements);
		Way currentElement = null;
		Edge currentEdge = null;
		Vertex start, end;
		Map<String,String> currentTags;
		
		//Default tags for power=line
		Map<String,String> tagsLine = new HashMap<String,String>(1);
		tagsLine.put("type", "first");
		
		//Default tags for power=line
		Map<String,String> tagsCable = new HashMap<String,String>(1);
		tagsCable.put("type", "second");
		
		//Default tags for power=minor_line
		Map<String,String> tagsMinorLine = new HashMap<String,String>(1);
		tagsMinorLine.put("type", "third");
		
		//Create edges
		for(String id : elements.keySet()) {
			currentElement = (Way) elements.get(id);
			
			//Create edge
			switch(currentElement.getTags().get("power")) {
				case "line":
					currentTags = tagsLine;
					break;
				case "cable":
					currentTags = tagsCable;
					break;
				default:
					currentTags = tagsMinorLine;
					break;
			}
			
			for(int i=0; i < currentElement.getNodes().size()-1; i++) {
				start = vertices.get(currentElement.getNodes().get(i).getId());
				end = vertices.get(currentElement.getNodes().get(i+1).getId());
				
				if(start != null && end != null) {
					currentEdge = new Edge(
									currentElement.getTags().get("ref"),
									currentTags,
									start,
									end
									);
					result.addEdge(currentEdge);
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
		
		//Default tags for power=pole
		Map<String,String> tagsPole = new HashMap<String,String>(1);
		tagsPole.put("type", "second");
		
		//Keep only ways with power key
		for(String id : elements.keySet()) {
			if(elements.get(id).getTags().containsKey("power")) {
				//If way, add it to kept elements
				if(elements.get(id).getClass().equals(Way.class)) {
					result.put(id, elements.get(id));
				}
				//If node, create a vertex
				else if(elements.get(id).getClass().equals(Node.class)) {
					vertices.put(
							id,
							new Vertex(
									elements.get(id).getTags().get("ref"),
									new HashMap<String,String>(tagsPole),
									((Node) elements.get(id)).getLat(),
									((Node) elements.get(id)).getLon()
									)
							);
				}
			}
		}
		
		return result;
	}
}
