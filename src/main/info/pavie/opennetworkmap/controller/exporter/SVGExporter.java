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

import info.pavie.opennetworkmap.model.draw.RepresentableEdge;
import info.pavie.opennetworkmap.model.draw.RepresentableNetwork;
import info.pavie.opennetworkmap.model.draw.RepresentableVertex;
import info.pavie.opennetworkmap.model.draw.style.NetworkStyle;
import info.pavie.opennetworkmap.model.network.Edge;
import info.pavie.opennetworkmap.model.network.Vertex;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.QuadCurve2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

//TODO CSS: handle color names
//TODO Handle edges passing under nodes they are not connected with
/**
 * Exports {@link RepresentableNetwork} as SVG images.
 */
public class SVGExporter implements NetworkExporter {
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

//OTHER METHODS
	@Override
	public boolean export(RepresentableNetwork net, NetworkStyle style, File output) throws IOException {
		//Initializes SVG file
		DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
		String svgNS = "http://www.w3.org/2000/svg";
		Document document = domImpl.createDocument(svgNS, "svg", null);
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
		
		/*
		 * Convert canvas
		 */
		//Canvas style
		renderCanvas(net, style, svgGenerator);
		
		//Render edges and nodes
		renderEdges(net, style, svgGenerator);
		renderNodes(net, style, svgGenerator);
		
		//Write the SVG file
		Writer out = new OutputStreamWriter(new FileOutputStream(output), "UTF-8");
		svgGenerator.stream(out, false);
		
		return true;
	}

	/**
	 * Makes rendering of canvas
	 * @param net The network to render
	 * @param style The style rules to use
	 * @param generator The graphics handler
	 */
	private void renderCanvas(RepresentableNetwork net, NetworkStyle style, Graphics2D generator) {
		Map<String,String> styleC = style.getGridStyle();
		
		//Node margins
		if(styleC.containsKey("node-margins")) {
			nodeMargins = Integer.parseInt(styleC.get("node-margins"));
		} else {
			nodeMargins = DEFAULT_NODE_MARGINS;
		}
		
//		//Background color
//		if(styleC.containsKey("background-color")) {
//			generator.setBackground(Color.decode(styleC.get("background-color")));
//		}
	}
	
	/**
	 * Makes rendering of nodes
	 * @param net The network to render
	 * @param style The style rules to use
	 * @param generator The graphics handler
	 */
	private void renderNodes(RepresentableNetwork net, NetworkStyle style, Graphics2D generator) {
		Set<RepresentableVertex> rVertices = net.getRepresentableVertices();
		int nodeWidth, strokeWidth, fontSize, labelWidth;
		String nodeShape, fontName;
		Color fillColor, strokeColor, fontColor;
		Map<String,String> nodeStyle;
		
		for(RepresentableVertex rv : rVertices) {
			Vertex v = rv.getVertex();
			
			//Get style for current node
			nodeStyle = style.getComponentStyle(v);
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
			
			/*
			 * Draw node
			 */
			generator.setColor(fillColor);
			
			//Fill in function of shape
			int x = rv.getX()*nodeMargins - nodeWidth/2;
			int y = -rv.getY()*nodeMargins - nodeWidth/2;
			switch(nodeShape) {
				case "circle":
					generator.fillOval(x, y, nodeWidth, nodeWidth);
					break;
				case "square":
					generator.fillRect(x, y, nodeWidth, nodeWidth);
					break;
				case "lozenge":
					fillLozenge(generator, x, y, nodeWidth, nodeWidth);
					break;
				default:
					throw new RuntimeException("Unrecognized shape: "+nodeShape);
			}
			
			generator.setColor(strokeColor);
			generator.setStroke(new BasicStroke(strokeWidth));
			
			//Draw in function of shape
			switch(nodeShape) {
				case "circle":
					generator.drawOval(x, y, nodeWidth, nodeWidth);
					break;
				case "square":
					generator.drawRect(x, y, nodeWidth, nodeWidth);
					break;
				case "lozenge":
					drawLozenge(generator, x, y, nodeWidth, nodeWidth);
					break;
				default:
					throw new RuntimeException("Unrecognized shape: "+nodeShape);
			}
			
			//Draw label
			if(v.getLabel() != null) {
				//Get style
				fontSize = (nodeStyle.containsKey("font-size")) ?
								Integer.parseInt(nodeStyle.get("font-size"))
								: DEFAULT_NODE_FONT_SIZE;
				fontColor = setColor(nodeStyle, "font-color", DEFAULT_NODE_FONT_COLOR);
				fontName = (nodeStyle.containsKey("font")) ?
								nodeStyle.get("font")
								: DEFAULT_NODE_FONT;
				generator.setFont(new Font(fontName, Font.PLAIN, fontSize));
				generator.setColor(fontColor);
				
				//Render text (multilines for long labels)
				labelWidth = generator.getFontMetrics().stringWidth(v.getLabel());
				
				if(labelWidth < nodeMargins) {
					generator.drawString(
							v.getLabel(),
							x + nodeWidth,
							y
							);
				} else {
					//Split label with spaces
					String[] splitLabel = v.getLabel().split(" ");
					List<String> labels = new ArrayList<String>();
					labels.add(splitLabel[0]);
					int i = 1;
					
					//Add words at a line if possible, else put it in a new line
					while(i < splitLabel.length) {
						if(generator.getFontMetrics().stringWidth(labels.get(labels.size()-1)+" "+splitLabel[i]) < nodeMargins) {
							labels.set(labels.size()-1, labels.get(labels.size()-1)+" "+splitLabel[i]);
						} else {
							labels.add(splitLabel[i]);
						}
						i++;
					}
					
					//Render labels
					for(int j=0; j < labels.size(); j++) {
						generator.drawString(
								labels.get(labels.size() -1 - j),
								x + nodeWidth,
								y - j * generator.getFontMetrics().getHeight()
								);
					}
				}
			}
		}
	}
	
	/**
	 * Makes rendering of edges
	 * @param net The network to render
	 * @param style The style rules to use
	 * @param generator The graphics handler
	 */
	private void renderEdges(RepresentableNetwork net, NetworkStyle style, Graphics2D generator) {
		Set<RepresentableEdge> rEdges = net.getRepresentableEdges();
		int edgeWidth, edgeStroke, fontSize, labelWidth;
		Color fillColor, strokeColor, fontColor;
		String fontName;
		Map<String,String> edgeStyle;
		Font textFont;
		
		//Find couples of vertices which have more than one edge between them
		Map<Couple,Byte> edgesBetween = new HashMap<Couple,Byte>(rEdges.size());
		for(RepresentableEdge re : rEdges) {
			Edge e = re.getEdge();
			Couple cple = new Couple(e.getStartVertex(), e.getEndVertex());
			if(edgesBetween.containsKey(cple)) {
				edgesBetween.put(cple, (byte) (edgesBetween.get(cple) + 1));
			} else {
				edgesBetween.put(cple, (byte) 1);
			}
		}
		Map<Couple,Byte> edgesBetweenDone = new HashMap<Couple,Byte>();
		
		for(RepresentableEdge re : rEdges) {
			Edge e = re.getEdge();
			
			//Get edge style
			edgeStyle = style.getComponentStyle(e);
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
			int x1 = re.getStart().getX()*nodeMargins;
			int y1 = -re.getStart().getY()*nodeMargins;
			int x2 = re.getEnd().getX()*nodeMargins;
			int y2 = -re.getEnd().getY()*nodeMargins;
			
			//Multiple edges between two nodes rendering
			Couple cple = new Couple(e.getStartVertex(), e.getEndVertex());
			byte edgesDone = (edgesBetweenDone.get(cple) != null) ? edgesBetweenDone.get(cple) : 0;
			int offWidth = (edgeWidth+edgeStroke*2);
//			int offsetLabelX = 0, offsetLabelY = 0;
			int ctrlX = x1 + (x2-x1)/2;
			int ctrlY = y1 + (y2-y1)/2;
			int offsetX = x2-x1;
			int offsetY = y2-y1;
			
			//Edges passing under nodes they aren't connected with
			if(Math.abs(offsetX) > nodeMargins) {
				ctrlX -= nodeMargins / 4;
//				offsetLabelY -= nodeMargins / 4;
			}
			if(Math.abs(offsetY) > nodeMargins) {
				ctrlY -= nodeMargins / 4;
//				offsetLabelX -= nodeMargins / 4;
			}
			
			if(edgesBetween.containsKey(cple) && edgesBetween.get(cple) > 1) {
				if(offsetX == 0) {
					ctrlX -= offWidth*2*edgesDone;
//					offsetLabelX = - offWidth*edgesDone;
//					offsetLabelY = 0;
				}
				else if(offsetY == 0) {
					ctrlY += offWidth*2*edgesDone;
//					offsetLabelX = 0;
//					offsetLabelY = offWidth*edgesDone;
				}
				else if(offsetX != offsetY) {
					ctrlX -= offWidth*2*edgesDone;
					ctrlY -= offWidth*2*edgesDone;
//					offsetLabelX = - offWidth*edgesDone;
//					offsetLabelY = - offWidth*edgesDone;
				}
				else {
					ctrlX -= offWidth*2*edgesDone;
					ctrlY += offWidth*2*edgesDone;
//					offsetLabelX = - offWidth*edgesDone;
//					offsetLabelY = offWidth*edgesDone;
				}
				
				QuadCurve2D curve = new QuadCurve2D.Float(
						x1,
						y1,
						ctrlX,
						ctrlY,
						x2,
						y2
						);
				
				//Edge stroke
				generator.setColor(strokeColor);
				generator.setStroke(new BasicStroke(edgeWidth+edgeStroke*2));
				generator.draw(curve);
				//Edge fill
				generator.setColor(fillColor);
				generator.setStroke(new BasicStroke(edgeWidth));
				generator.draw(curve);
				
				//Increment edges done
				if(edgesBetweenDone.containsKey(cple)) {
					edgesBetweenDone.put(cple, (byte) (edgesBetweenDone.get(cple) + 1));
				} else {
					edgesBetweenDone.put(cple, (byte) 1);
				}
			}
			//Single edge rendering
			else {
				QuadCurve2D curve = new QuadCurve2D.Float(
						x1,
						y1,
						ctrlX,
						ctrlY,
						x2,
						y2
						);
				
				//Edge stroke
				generator.setColor(strokeColor);
				generator.setStroke(new BasicStroke(edgeWidth+edgeStroke*2));
				generator.draw(curve);
				//Edge fill
				generator.setColor(fillColor);
				generator.setStroke(new BasicStroke(edgeWidth));
				generator.draw(curve);
			}
			
			//Draw label
			if(e.getLabel() != null) {
				//Get style
				fontSize = (edgeStyle.containsKey("font-size")) ?
								Integer.parseInt(edgeStyle.get("font-size"))
								: DEFAULT_EDGE_FONT_SIZE;
				fontColor = setColor(edgeStyle, "font-color", DEFAULT_EDGE_FONT_COLOR);
				fontName = (edgeStyle.containsKey("font")) ?
								edgeStyle.get("font")
								: DEFAULT_EDGE_FONT;
				generator.setColor(fontColor);
				textFont = new Font(fontName, Font.PLAIN, fontSize);
				
				//Rotation
				AffineTransform orig = generator.getTransform();
				generator.setFont(textFont);
				labelWidth = generator.getFontMetrics().stringWidth(e.getLabel());
//				generator.translate((x2 - x1)/2 + x1 + offsetLabelX, (y2 - y1)/2 + y1 + offsetLabelY);
				generator.translate(ctrlX, ctrlY);
				
				double angle = re.getDirection() % (2*Math.PI);
				if(angle > Math.PI/2 && angle <= 3*Math.PI/2) { angle += Math.PI; }
				generator.rotate(-angle);
				generator.drawString(e.getLabel(), -labelWidth/2, fontSize/3);
				generator.setTransform(orig);
			}
		}
	}
	
	/**
	 * Draws a lozenge
	 * @param generator The graphics handler
	 * @param x The X start coordinate
	 * @param y The Y start coordinate
	 * @param width The lozenge width
	 * @param height The lozenge height
	 */
	private void drawLozenge(Graphics2D generator, int x, int y, int width, int height) {
		// draw GeneralPath (polygon)
		int x1Points[] = {x, x+width/2, x+width, x+width/2};
		int y1Points[] = {y+height/2, y+height, y+height/2, y};
		GeneralPath polygon = 
		        new GeneralPath(GeneralPath.WIND_EVEN_ODD,
		                        x1Points.length);
		polygon.moveTo(x1Points[0], y1Points[0]);

		for(int index = 1; index < x1Points.length; index++) {
		        polygon.lineTo(x1Points[index], y1Points[index]);
		}

		polygon.closePath();
		generator.draw(polygon);
	}
	
	/**
	 * Fills a lozenge
	 * @param generator The graphics handler
	 * @param x The X start coordinate
	 * @param y The Y start coordinate
	 * @param width The lozenge width
	 * @param height The lozenge height
	 */
	private void fillLozenge(Graphics2D generator, int x, int y, int width, int height) {
		// draw GeneralPath (polygon)
		int x1Points[] = {x, x+width/2, x+width, x+width/2};
		int y1Points[] = {y+height/2, y+height, y+height/2, y};
		GeneralPath polygon = 
		        new GeneralPath(GeneralPath.WIND_EVEN_ODD,
		                        x1Points.length);
		polygon.moveTo(x1Points[0], y1Points[0]);

		for(int index = 1; index < x1Points.length; index++) {
		        polygon.lineTo(x1Points[index], y1Points[index]);
		}

		polygon.closePath();
		generator.fill(polygon);
	}
	
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
	
//INTERN CLASS Couple
	/**
	 * Couples of vertices, in order to handle several edges rendering between two vertices.
	 */
	private class Couple {
	//ATTRIBUTES
		Set<Vertex> vertices;
		
	//CONSTRUCTOR
		Couple(Vertex from, Vertex to) {
			vertices = new HashSet<Vertex>(2);
			vertices.add(from);
			vertices.add(to);
		}
		
	//OTHER METHODS
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("(");
			boolean first = true;
			for(Vertex v : vertices) {
				if(!first) {
					sb.append(", ");
				} else {
					first = false;
				}
				sb.append(v);
			}
			sb.append(")");
			return sb.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((vertices == null) ? 0 : vertices.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof Couple))
				return false;
			Couple other = (Couple) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (vertices == null) {
				if (other.vertices != null)
					return false;
			} else if (!vertices.equals(other.vertices))
				return false;
			return vertices.containsAll(((Couple) obj).vertices);
		}

		private SVGExporter getOuterType() {
			return SVGExporter.this;
		}
	}
}