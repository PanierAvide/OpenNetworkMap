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

package info.pavie.opennetworkmap.model.draw.network;

import info.pavie.opennetworkmap.model.draw.RepresentableEdge;
import info.pavie.opennetworkmap.model.draw.RepresentableNetwork;
import info.pavie.opennetworkmap.model.draw.RepresentableVertex;
import info.pavie.opennetworkmap.model.draw.style.TagBasedRule;
import info.pavie.opennetworkmap.model.network.Component;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Network;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.osbcp.cssparser.PropertyValue;
import com.osbcp.cssparser.Rule;

//TODO Use completely the new Representable* scheme
/**
 * A canvas is an editable table where you can insert {@link Network} vertices.
 * It is a grid of different {@link Layer}s, which allows to insert disconnected graphs.
 * It allows to create a schematic representation of the network in a simple way.
 */
public class Canvas implements RepresentableNetwork {
//ATTRIBUTES
	/** The canvas grid **/
	private FlexibleGrid<Layer> grid;
	/** The last added layer **/
	private Layer lastLayer, pastLayer;
	/** The canvas style rules **/
	private List<TagBasedRule> styleRules;
	/** The vertices positions **/
	private Map<Vertex,int[]> verticesPositions;
	/** The edges positions **/
	private Map<Edge,int[]> edgesPositions;

//CONSTRUCTOR
	/**
	 * Creates a new canvas, with one vertex
	 * @param v The vertex (can't be null)
	 */
	public Canvas(Vertex v) {
		lastLayer = new Layer(v);
		grid = new FlexibleGrid<Layer>(lastLayer);
		styleRules = new ArrayList<TagBasedRule>();
	}

//ACCESSORS
	@Override
	public Set<RepresentableVertex> getRepresentableVertices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<RepresentableEdge> getRepresentableEdges() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Returns the wanted vertex.
	 * Don't forget to call once setLastLayerDone() before calling this method.
	 * @param row The row of the vertex
	 * @param col The column of the vertex
	 * @return The vertex, or null if the cell is empty
	 */
	public Vertex getVertex(int row, int col) {
		int layerRowId = 0, layerColId = 0;
		int lastMaxHeight = 0, lastMaxWidth = 0;
		int actRow = 0, actCol = 0;
		
		do {
			lastMaxHeight = getMaximumLayerHeight(layerRowId);
//			System.out.println("LastMaxHeight: "+lastMaxHeight);
			actRow += lastMaxHeight;
			layerRowId++;
		} while(actRow <= row && layerRowId < grid.getHeight());
		layerRowId--;
		
		do {
			lastMaxWidth = getMaximumLayerWidth(layerColId);
//			System.out.println("LastMaxWidth: "+lastMaxWidth);
			actCol += lastMaxWidth;
			layerColId++;
		} while(actCol <= col && layerColId < grid.getWidth());
		layerColId--;
		
//		System.out.println("LayerRow: "+layerRowId+" LayerCol: "+layerColId
//				+" InRow: "+(row - actRow + lastMaxHeight)
//				+" InCol: "+(col - actCol + lastMaxWidth));
			
		return grid.get(layerRowId, layerColId).getVertex(
				row - actRow + lastMaxHeight,
				col - actCol + lastMaxWidth
				);
	}
	
	/**
	 * @return The vertices positions, [x, y] with x = column and y = row
	 */
	public Map<Vertex,int[]> getVerticesPositions() {
		if(verticesPositions == null) {
			verticesPositions = new HashMap<Vertex,int[]>();
			int cumulRowId = 0, cumulColId = 0;
			
			for(int row = 0; row < grid.getHeight(); row++) {
				cumulColId = 0;
				for(int col = 0; col < grid.getWidth(); col++) {
					//Get vertices positions from current layer
					Layer l = grid.get(row, col);
					if(l != null) {
						for(Vertex v : l.getVerticesPositions().keySet()) {
							//Increment position
							int[] pos = l.getVerticesPositions().get(v);
							int[] newPos = new int[2];
							newPos[0] = pos[0] + cumulRowId;
							newPos[1] = pos[1] + cumulColId;
							verticesPositions.put(v, newPos);
						}
					}
					
					cumulColId += getMaximumLayerWidth(col);
				}
				cumulRowId += getMaximumLayerHeight(row);
			}
		}
		
		return verticesPositions;
	}
	
	/**
	 * @return The edges positions, [startX, startY, endX, endY]
	 */
	public Map<Edge,int[]> getEdgesPositions() {
		if(edgesPositions == null) {
			edgesPositions = new HashMap<Edge,int[]>();
			Map<Vertex,int[]> vertPos = getVerticesPositions();
			
			for(Vertex v : vertPos.keySet()) {
//				System.out.println("Get "+v+" "+v.getStartOf());
				for(Edge e : v.getStartOf()) {
					int[] pos = {
									vertPos.get(v)[0],
									vertPos.get(v)[1],
									vertPos.get(e.getEndVertex())[0],
									vertPos.get(e.getEndVertex())[1]
								};
					edgesPositions.put(e, pos);
				}
			}
		}
		
		return edgesPositions;
	}
	
	/**
	 * Returns the applyable properties for a given component
	 * @param c The component
	 * @return The style properties
	 */
	public Map<String,String> getComponentStyle(Component c) {
		//Search applyable rules
		boolean isApplyable;
		Iterator<String> ite;
		String key;
		Map<String,String> style = new HashMap<String,String>(0);
		
		for(TagBasedRule tbr : styleRules) {
			isApplyable = true;
			ite = tbr.getSelectorTags().keySet().iterator();
			
			//Look for needed tags
			while(ite.hasNext() && isApplyable) {
				key = ite.next();
				isApplyable = c.getTags().containsKey(key) && isTagValueValid(tbr.getSelectorTags().get(key), c.getTags().get(key));
			}
			
			//Set style
			if(isApplyable) {
				for(PropertyValue p : tbr.getProperties()) {
					style.put(p.getProperty(), p.getValue());
				}
			}
		}
		
		//Replace fill color value by color tag if defined
		if(c.getTags().containsKey("colour")) {
			style.put("fill", c.getTags().get("colour"));
		}
		
		return style;
	}
	
	/**
	 * @return The style rules for canvas
	 */
	public Map<String,String> getCanvasStyle() {
		Map<String,String> style = new HashMap<String,String>(0);
		
		for(TagBasedRule tbr : styleRules) {
			//Set style
			if(tbr.getSelectorTags().get("grid") != null 
					&& tbr.getSelectorTags().get("grid").equals("yes")) {
				for(PropertyValue p : tbr.getProperties()) {
					style.put(p.getProperty(), p.getValue());
				}
			}
		}
		
		return style;
	}
	
	/**
	 * Get the direction of an edge in this canvas, in radians
	 * @param e The edge
	 * @return The direction, in radians (0 = West to East)
	 */
	public double getEdgeDirection(Edge e) {
		Map<Vertex,int[]> verticesPos = getVerticesPositions();
		int[] posFrom = verticesPos.get(e.getStartVertex());
		int[] posTo = verticesPos.get(e.getEndVertex());
		double angleRad = Math.atan2(posTo[0]-posFrom[0], posTo[1]-posFrom[1]);

		return angleRad;
	}
	
	/**
	 * Is the value valid compared to the rule value ?
	 * @param rule The rule value (null for any value)
	 * @param actual The actual value
	 * @return True if actual value accepted, false else
	 */
	private boolean isTagValueValid(String rule, String actual) {
		return rule == null || rule.equals(actual);
	}
	
//MODIFIERS
	/**
	 * Sets the style rules to use in this canvas.
	 * @param rules The CSS rules
	 */
	public void defineStyle(List<Rule> rules) {
		styleRules = TagBasedRule.parseRules(rules);
	}
	
	/**
	 * Adds a vertex at the given direction of an already contained vertex.
	 * For example, add a vertex "v" to the right of already contained vertex "from".
	 * @param v The vertex to add
	 * @param from The vertex already contained in canvas
	 */
	public void add(Vertex v, Vertex from) {
		lastLayer.add(v, from);
		verticesPositions = null;
		edgesPositions = null;
	}
	
	/**
	 * Adds a new layer to canvas
	 * @param v The first vertex of this layer
	 */
	public void addLayer(Vertex v) {
		pastLayer = lastLayer;
		lastLayer = new Layer(v);
		verticesPositions = null;
		edgesPositions = null;
	}
	
	/**
	 * Call this method when you have done current layer edition
	 */
	public void setLastLayerDone() {
		//Not necessary for first layer
		if(pastLayer != null) {
			grid.add(lastLayer, pastLayer);
			verticesPositions = null;
			edgesPositions = null;
		}
	}
	
//OTHER METHODS
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("= Canvas =\n");
		
		for(Layer l : grid.getPositions().keySet()) {
			int[] pos = grid.getPositions().get(l);
			sb.append("== Layer ("+pos[0]+", "+pos[1]+") ==\n"+l.toString());
		}
		
		return sb.toString();
	}
	
	/**
	 * @param col The column ID
	 * @return The maximum layer width in the given column
	 */
	private int getMaximumLayerWidth(int col) {
		int max = 0;
		for(int i=0; i < grid.getHeight(); i++) {
			if(grid.get(i, col) != null) {
				max = Math.max(max, grid.get(i, col).getWidth());
			}
		}
		return max;
	}
	
	/**
	 * @param row The row ID
	 * @return The maximum layer height in the given row
	 */
	private int getMaximumLayerHeight(int row) {
		int max = 0;
		for(int i=0; i < grid.getWidth(); i++) {
			if(grid.get(row, i) != null) {
				max = Math.max(max, grid.get(row, i).getHeight());
			}
		}
		return max;
	}
}
