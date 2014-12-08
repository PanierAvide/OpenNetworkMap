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

package info.pavie.opennetworkmap.controller.exporter;

import info.pavie.opennetworkmap.model.draw.Canvas;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Vertex;
import info.pavie.opennetworkmap.util.RWFile;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//TODO CSS: handle color names
/**
 * Exports {@link Canvas} as web pages, working with SigmaJS.
 */
public class CanvasToWebExporter implements CanvasExporter {
//CONSTANTS
	private static final int DEFAULT_NODE_MARGINS = 50;
	private static final int DEFAULT_NODE_WIDTH = 10;
	private static final String DEFAULT_NODE_SHAPE = "circle";
	private static final Color DEFAULT_NODE_FILL = Color.WHITE;
	private static final Color DEFAULT_NODE_STROKE = Color.BLACK;
	private static final int DEFAULT_NODE_STROKE_WIDTH = 2;
	private static final String DEFAULT_NODE_FONT = "Arial";
	private static final int DEFAULT_NODE_FONT_SIZE = 12;
	private static final Color DEFAULT_NODE_FONT_COLOR = Color.BLACK;
	private static final int DEFAULT_EDGE_WIDTH = 4;
	private static final Color DEFAULT_EDGE_FILL = Color.RED;
	private static final int DEFAULT_EDGE_STROKE_WIDTH = 0;
	private static final Color DEFAULT_EDGE_STROKE = Color.BLACK;
	private static final String DEFAULT_EDGE_FONT = "Arial";
	private static final int DEFAULT_EDGE_FONT_SIZE = 12;
	private static final Color DEFAULT_EDGE_FONT_COLOR = Color.BLUE;
	
//ATTRIBUTES
	private int nodeMargins;
	private Map<Vertex,Integer> verticesId;

//OTHER METHODS
	@Override
	public boolean export(Canvas c, File output) throws IOException {
		verticesId = new HashMap<Vertex,Integer>(c.getVerticesPositions().size());
		
		//Initialize HTML output
		StringBuilder html = new StringBuilder(
				"<!DOCTYPE html>"
				+ "\n<html>"
				+ "\n<head>"
				+ "\n	<title>OpenNetworkMap</title>"
				+ "\n	<meta charset=\"UTF-8\">"
				+ "\n	<style type=\"text/css\">"
				+ "\n		body {"
				+ "\n			margin: 0;"
				+ "\n		}"
				+ "\n		#container {"
				+ "\n			position: absolute;"
				+ "\n			width: 100%;"
				+ "\n			height: 100%;"
				+ "\n		}"
				+ "\n	</style>"
				+ "\n</head>"
				+ "\n<body>"
				+ "\n	<div id=\"container\"></div>"
				+ "\n	<script src=\"./sigma.min.js\"></script>"
				+ "\n	<script>"
				+ "\n		var s = new sigma('container');"
				+ "\n		s.graph");
		
		//Render nodes
		html.append(renderNodes(c));
		
		//Render edges
		html.append(renderEdges(c));
		
		//End HTML output
		html.append(
				";"
				+ "\n		s.refresh();"
				+ "\n	</script>"
				+ "\n</body>"
				+ "\n</html>");
		
		//Write string into file
		RWFile.writeTextFile(output, html.toString());
		
		return true;
	}
//
//	/**
//	 * Makes rendering of canvas
//	 * @param c The canvas
//	 * @param generator The graphics handler
//	 */
//	private void renderCanvas(Canvas c, Graphics2D generator) {
//		Map<String,String> style = c.getCanvasStyle();
//		
//		//Node margins
//		if(style.containsKey("node-margins")) {
//			nodeMargins = Integer.parseInt(style.get("node-margins"));
//		} else {
//			nodeMargins = DEFAULT_NODE_MARGINS;
//		}
//		
////		//Background color
////		if(style.containsKey("background-color")) {
////			generator.setBackground(Color.decode(style.get("background-color")));
////		}
//	}
//	
	/**
	 * Makes rendering of nodes
	 * @param c The canvas
	 * @return The code corresponding to nodes
	 */
	private String renderNodes(Canvas c) {
		Map<Vertex,int[]> nodesPos = c.getVerticesPositions();
		int nodeWidth, strokeWidth, fontSize, labelWidth;
		String nodeShape, fontName;
		Color fillColor, strokeColor, fontColor;
		Map<String,String> nodeStyle;
		
		StringBuilder html = new StringBuilder();
		int vertId = 0;
		
		for(Vertex v : nodesPos.keySet()) {
			int[] pos = nodesPos.get(v);
			verticesId.put(v, vertId);
			
			//Get style for current node
			nodeStyle = c.getComponentStyle(v);
			nodeWidth = (nodeStyle.containsKey("width")) ?
							Integer.parseInt(nodeStyle.get("width"))
							: DEFAULT_NODE_WIDTH;
			nodeShape = (nodeStyle.containsKey("shape")) ?
							nodeStyle.get("shape")
							: DEFAULT_NODE_SHAPE;
			fillColor = setColor(nodeStyle, "fill", DEFAULT_NODE_FILL);
			strokeColor = setColor(nodeStyle, "stroke", DEFAULT_NODE_STROKE);
			strokeWidth = (nodeStyle.containsKey("stroke-width")) ?
							Integer.parseInt(nodeStyle.get("stroke-width"))
							: DEFAULT_NODE_STROKE_WIDTH;
			
			html.append(
					".addNode({"
					+ "\n		id: 'n"+vertId+"',"
					+ "\n		label: '"+v.getLabel().replaceAll("'", "&quote;")+"',"
					+ "\n		x: "+pos[1]+","
					+ "\n		y: -"+pos[0]+","
					+ "\n		size: "+nodeWidth+","
					+ "\n		color: '#"+strokeColor.getRGB()+"'"
					+ "\n		})");
			
			vertId++;
		}
		
		return html.toString();
	}
	
	/**
	 * Makes rendering of edges
	 * @param c The canvas
	 * @return The code corresponding to edges
	 */
	private String renderEdges(Canvas c) {
		Map<Edge,int[]> edgesPos = c.getEdgesPositions();
		int edgeWidth, edgeStroke, fontSize, labelWidth;
		Color fillColor, strokeColor, fontColor;
		String fontName;
		Map<String,String> edgeStyle;
		Font textFont;
		
		StringBuilder html = new StringBuilder();
		int edgeId = 0;
		
//		//Find couples of vertices which have more than one edge between them
//		Map<Couple,Byte> edgesBetween = new HashMap<Couple,Byte>(edgesPos.size());
//		for(Edge e : edgesPos.keySet()) {
//			Couple cple = new Couple(e.getStartVertex(), e.getEndVertex());
//			if(edgesBetween.containsKey(cple)) {
//				edgesBetween.put(cple, (byte) (edgesBetween.get(cple) + 1));
//			} else {
//				edgesBetween.put(cple, (byte) 1);
//			}
//		}
//		Map<Couple,Byte> edgesBetweenDone = new HashMap<Couple,Byte>();
		
		for(Edge e : edgesPos.keySet()) {
			int[] pos = edgesPos.get(e);
			
			//Get edge style
			edgeStyle = c.getComponentStyle(e);
			edgeWidth = (edgeStyle.containsKey("width")) ?
							Integer.parseInt(edgeStyle.get("width"))
							: DEFAULT_EDGE_WIDTH;
			edgeStroke = (edgeStyle.containsKey("stroke-width")) ?
					Integer.parseInt(edgeStyle.get("stroke-width"))
					: DEFAULT_EDGE_STROKE_WIDTH;
			fillColor = setColor(edgeStyle, "fill", DEFAULT_EDGE_FILL);
			strokeColor = setColor(edgeStyle, "stroke", DEFAULT_EDGE_STROKE);
			
			/*
			 * Draw edge
			 */
			html.append(
					".addEdge({"
					+ "\n		id: 'e"+edgeId+"',"
					+ "\n		label: '"+e.getLabel().replaceAll("'", "&quote;")+"',"
					+ "\n		source: 'n"+verticesId.get(e.getStartVertex())+"',"
					+ "\n		target: 'n"+verticesId.get(e.getEndVertex())+"',"
					+ "\n		size: "+edgeWidth+","
					+ "\n		color: '#"+fillColor.getRGB()+"'"
					+ "\n		})");
			
			edgeId++;
		}
		
		return html.toString();
	}
//	
//	/**
//	 * Draws a lozenge
//	 * @param generator The graphics handler
//	 * @param x The X start coordinate
//	 * @param y The Y start coordinate
//	 * @param width The lozenge width
//	 * @param height The lozenge height
//	 */
//	private void drawLozenge(Graphics2D generator, int x, int y, int width, int height) {
//		// draw GeneralPath (polygon)
//		int x1Points[] = {x, x+width/2, x+width, x+width/2};
//		int y1Points[] = {y+height/2, y+height, y+height/2, y};
//		GeneralPath polygon = 
//		        new GeneralPath(GeneralPath.WIND_EVEN_ODD,
//		                        x1Points.length);
//		polygon.moveTo(x1Points[0], y1Points[0]);
//
//		for(int index = 1; index < x1Points.length; index++) {
//		        polygon.lineTo(x1Points[index], y1Points[index]);
//		}
//
//		polygon.closePath();
//		generator.draw(polygon);
//	}
//	
//	/**
//	 * Fills a lozenge
//	 * @param generator The graphics handler
//	 * @param x The X start coordinate
//	 * @param y The Y start coordinate
//	 * @param width The lozenge width
//	 * @param height The lozenge height
//	 */
//	private void fillLozenge(Graphics2D generator, int x, int y, int width, int height) {
//		// draw GeneralPath (polygon)
//		int x1Points[] = {x, x+width/2, x+width, x+width/2};
//		int y1Points[] = {y+height/2, y+height, y+height/2, y};
//		GeneralPath polygon = 
//		        new GeneralPath(GeneralPath.WIND_EVEN_ODD,
//		                        x1Points.length);
//		polygon.moveTo(x1Points[0], y1Points[0]);
//
//		for(int index = 1; index < x1Points.length; index++) {
//		        polygon.lineTo(x1Points[index], y1Points[index]);
//		}
//
//		polygon.closePath();
//		generator.fill(polygon);
//	}
//	
	//TODO Try to parse color names
	/**
	 * Tries to parse custom color from tags 
	 * @param tags The tags
	 * @param key The key of the color to parse
	 * @param def The default color value
	 * @return The custom color, or default if an error occurs during parsing
	 */
	private Color setColor(Map<String,String> tags, String key, Color def) {
		Color result = def;
		
		if(tags.containsKey(key)) {
			try {
				result = Color.decode(tags.get(key));
			} catch(NumberFormatException e) {
				System.err.println("\t-Invalid color value: "+tags.get(key));
			}
		}
		
		return result;
	}
//	
////INTERN CLASS Couple
//	/**
//	 * Couples of vertices, in order to handle several edges rendering between two vertices.
//	 */
//	private class Couple {
//	//ATTRIBUTES
//		Set<Vertex> vertices;
//		
//	//CONSTRUCTOR
//		Couple(Vertex from, Vertex to) {
//			vertices = new HashSet<Vertex>(2);
//			vertices.add(from);
//			vertices.add(to);
//		}
//		
//	//OTHER METHODS
//		@Override
//		public String toString() {
//			StringBuilder sb = new StringBuilder("(");
//			boolean first = true;
//			for(Vertex v : vertices) {
//				if(!first) {
//					sb.append(", ");
//				} else {
//					first = false;
//				}
//				sb.append(v);
//			}
//			sb.append(")");
//			return sb.toString();
//		}
//
//		@Override
//		public int hashCode() {
//			final int prime = 31;
//			int result = 1;
//			result = prime * result + getOuterType().hashCode();
//			result = prime * result
//					+ ((vertices == null) ? 0 : vertices.hashCode());
//			return result;
//		}
//
//		@Override
//		public boolean equals(Object obj) {
//			if (this == obj)
//				return true;
//			if (obj == null)
//				return false;
//			if (!(obj instanceof Couple))
//				return false;
//			Couple other = (Couple) obj;
//			if (!getOuterType().equals(other.getOuterType()))
//				return false;
//			if (vertices == null) {
//				if (other.vertices != null)
//					return false;
//			} else if (!vertices.equals(other.vertices))
//				return false;
//			return vertices.containsAll(((Couple) obj).vertices);
//		}
//
//		private CanvasToWebExporter getOuterType() {
//			return CanvasToWebExporter.this;
//		}
//	}
}