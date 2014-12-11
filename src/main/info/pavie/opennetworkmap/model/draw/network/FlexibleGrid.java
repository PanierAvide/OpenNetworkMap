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

import info.pavie.opennetworkmap.model.draw.Spatializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A flexible grid is a two-dimensions list where you can store objects.
 * Objects are added in function of other elements' place in grid.
 * @param <E> The class of objects you want to store
 */
public class FlexibleGrid<E extends Spatializable> {
//CONSTANTS
	/*
	 * Directions
	 */
	private static final int TO_TOP = 1;
	private static final int TO_BOTTOM = 2;
	private static final int TO_LEFT = 3;
	private static final int TO_RIGHT = 4;
	private static final int TO_TOP_RIGHT = 5;
	private static final int TO_TOP_LEFT = 6;
	private static final int TO_BOTTOM_RIGHT = 7;
	private static final int TO_BOTTOM_LEFT = 8;

//ATTRIBUTES
	/** The grid rows **/
	private List<Row> rows;
	/** The objects positions **/
	private Map<E,int[]> positions;

//CONSTRUCTOR
	/**
	 * Class constructor, initializes a grid with one element
	 * @param first The first element
	 */
	public FlexibleGrid(E first) {
		if(first == null) {
			throw new NullPointerException("First element can't be null");
		}
		
		rows = new ArrayList<Row>();
		rows.add(new Row());
		rows.get(0).cells.set(0, first);
	}

//ACCESSORS
	/**
	 * @param row The row ID
	 * @param col The column ID
	 * @return The wanted element, or null if no element defined
	 */
	public E get(int row, int col) {
		return rows.get(row).cells.get(col);
	}
	
	/**
	 * @return The grid width
	 */
	public int getWidth() {
		return rows.get(0).cells.size();
	}
	
	/**
	 * @return The grid height
	 */
	public int getHeight() {
		return rows.size();
	}
	
	/**
	 * @return The objects positions in grid, as [ row, col ] for each one.
	 */
	public Map<E,int[]> getPositions() {
		if(positions == null) {
			positions = new HashMap<E,int[]>();
			
			for(int row=0; row < rows.size(); row++) {
				for(int col=0; col < rows.get(row).cells.size(); col++) {
					if(get(row, col) != null) {
						int[] coords = { row, col };
						positions.put(get(row, col), coords);
					}
				}
			}
		}
		
		return positions;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = rows.size() - 1; i >= 0; i--) {
			for(E o : rows.get(i).cells) {
				if(o != null) {
					sb.append(o);
				}
				sb.append("\t");
			}
			sb.append('\n');
		}
		
		return sb.toString();
	}

//MODIFIERS
	/**
	 * Adds the given object next to the "from" object in the appropriate direction
	 * @param o The object to add
	 * @param from The already contained object
	 */
	public void add(E o, E from) {
		positions = null;
		add(o, getDirection(from, o), from);
	}

//OTHER METHODS
	/**
	 * Adds the given object next to the "from" object in the given direction
	 * @param o The object to add
	 * @param direction The direction, for example add "o" TO_RIGHT of "from"
	 * @param from The already contained object
	 */
	private void add(E o, int direction, E from) {
		//Find vertex from
		boolean fromFound = false;
		int[] fromCoords = null;
		try {
			fromCoords = findObject(from);
			fromFound = true;
		} catch(ObjectNotFoundException e) {;}
		
		//Check if vertex v isn't already added
		boolean oFound = false;
		try {
			findObject(o);
			oFound = true;
		} catch(ObjectNotFoundException e) {;}
		
		//Try to add v
		if(fromFound && !oFound) {
			int[] oCoords = calculateCoordinates(fromCoords, direction);
			
			//Adjust canvas size if necessary
			if(oCoords[0] < 0) {
				addRow(0);
				oCoords[0]++;
				fromCoords[0]++;
			}
			else if(oCoords[0] >= rows.size()) {
				addRow(rows.size());
			}
			
			if(oCoords[1] < 0) {
				addColumn(0);
				oCoords[1]++;
				fromCoords[1]++;
			}
			else if(oCoords[1] >= rows.get(oCoords[0]).cells.size()) {
				addColumn(rows.get(oCoords[0]).cells.size());
			}
			
			//Is destination cell empty ?
			if(rows.get(oCoords[0]).cells.get(oCoords[1]) == null) {
				//Add vertex
				rows.get(oCoords[0]).cells.set(oCoords[1], o);
			}
			//If not empty, we have to edit canvas to handle it
			else {
				E present = rows.get(oCoords[0]).cells.get(oCoords[1]);
				switch(direction) {
					case TO_TOP:
						if(o.getLatitude() > present.getLatitude()) {
							add(o, present);
						} else {
							addRow(oCoords[0]);
							rows.get(oCoords[0]).cells.set(oCoords[1], o);
						}
						break;
					case TO_BOTTOM:
						if(o.getLatitude() < present.getLatitude()) {
							add(o, present);
						} else {
							addRow(oCoords[0]+1);
							rows.get(oCoords[0]+1).cells.set(oCoords[1], o);
						}
						break;
					case TO_LEFT:
						if(o.getLongitude() < present.getLongitude()) {
							add(o, present);
						} else {
							addColumn(oCoords[1]+1);
							rows.get(oCoords[0]).cells.set(oCoords[1]+1, o);
						}
						break;
					case TO_RIGHT:
						if(o.getLongitude() > present.getLongitude()) {
							add(o, present);
						} else {
							addColumn(oCoords[1]);
							rows.get(oCoords[0]).cells.set(oCoords[1], o);
						}
						break;
					case TO_TOP_RIGHT:
						if(o.getLatitude() > present.getLatitude()
								&& o.getLongitude() > present.getLongitude()) {
							add(o, present);
						} else {
							addRow(oCoords[0]);
							addColumn(oCoords[1]);
							rows.get(oCoords[0]).cells.set(oCoords[1], o);
						}
						break;
					case TO_TOP_LEFT:
						if(o.getLatitude() > present.getLatitude()
								&& o.getLongitude() < present.getLongitude()) {
							add(o, present);
						} else {
							addRow(oCoords[0]);
							addColumn(oCoords[1]+1);
							rows.get(oCoords[0]).cells.set(oCoords[1]+1, o);
						}
						break;
					case TO_BOTTOM_RIGHT:
						if(o.getLatitude() < present.getLatitude()
								&& o.getLongitude() > present.getLongitude()) {
							add(o, present);
						} else {
							addRow(oCoords[0]+1);
							addColumn(oCoords[1]);
							rows.get(oCoords[0]+1).cells.set(oCoords[1], o);
						}
						break;
					case TO_BOTTOM_LEFT:
						if(o.getLatitude() < present.getLatitude()
								&& o.getLongitude() < present.getLongitude()) {
							add(o, present);
						} else {
							addRow(oCoords[0]+1);
							addColumn(oCoords[1]+1);
							rows.get(oCoords[0]+1).cells.set(oCoords[1]+1, o);
						}
						break;
					default:
						throw new RuntimeException("Invalid direction");
				}
			}
		}
	}
	
	/**
	 * Get the direction of an object "to" compared to another object "from"
	 * @param from The start object
	 * @param to The end object
	 * @return The direction (see TO_* class constants)
	 */
	private int getDirection(E from, E to) {
		byte result;
		double angleRad = Math.atan2(to.getLatitude()-from.getLatitude(), to.getLongitude()-from.getLongitude());
		double angle = Math.toDegrees(angleRad); /* Radian angle starts at east */
		angle = Math.abs(angle-180);
		angle = (angle < 90) ? angle + 270 : angle - 90; /* Angle from 0 (north) to 360° clockwise */		
//		System.out.println(c1+" "+c2+" "+angle);
		
		if(angle >= 22 && angle < 67) {
			result = TO_TOP_RIGHT;
		}
		else if(angle >= 67 && angle < 112) {
			result = TO_RIGHT;
		}
		else if(angle >= 112 && angle < 157) {
			result = TO_BOTTOM_RIGHT;
		}
		else if(angle >= 157 && angle < 202) {
			result = TO_BOTTOM;
		}
		else if(angle >= 202 && angle < 247) {
			result = TO_BOTTOM_LEFT;
		}
		else if(angle >= 247 && angle < 292) {
			result = TO_LEFT;
		}
		else if(angle >= 292 && angle < 337) {
			result = TO_TOP_LEFT;
		}
		else {
			result = TO_TOP;
		}
		
		return result;
	}
	
	/**
	 * Searches given object in grid
	 * @param o The object
	 * @return The coordinates [ row, col ]
	 * @throws ObjectNotFoundException If vertex not found
	 */
	private int[] findObject(E o) throws ObjectNotFoundException {
		boolean found = false;
		int rowId = 0, cellId = -1;
		while(!found && rowId < rows.size()) {
			cellId = rows.get(rowId).cells.indexOf(o);
			if(cellId >= 0) {
				found = true;
			} else {
				rowId++;
			}
		}
		
		if(!found) { throw new ObjectNotFoundException(); }
		int[] result = { rowId, cellId };
		return result;
	}
	
	/**
	 * Calculates coordinates in function of base coordinates and given direction
	 * @param coords The base coordinates
	 * @param direction The direction (see TO_* constants)
	 * @return The coordinates
	 */
	private int[] calculateCoordinates(int[] coords, int direction) {
		int row = coords[0], col = coords[1];
		switch(direction) {
			case TO_TOP:
				row++;
				break;
			case TO_BOTTOM:
				row--;
				break;
			case TO_LEFT:
				col--;
				break;
			case TO_RIGHT:
				col++;
				break;
			case TO_TOP_RIGHT:
				row++;
				col++;
				break;
			case TO_TOP_LEFT:
				row++;
				col--;
				break;
			case TO_BOTTOM_RIGHT:
				row--;
				col++;
				break;
			case TO_BOTTOM_LEFT:
				row--;
				col--;
				break;
			default:
				throw new RuntimeException("Invalid direction");
		}
		int[] result = { row, col };
		return result;
	}
	
	/**
	 * Adds a column at given index.
	 * Current and next elements are shifted to right.
	 * @param index The index where the column will be inserted.
	 */
	private void addColumn(int index) {
		for(Row r : rows) {
			r.addCell(index);
		}
	}
	
	/**
	 * Adds a row at given index.
	 * Current and next rows are shifted to right in list.
	 * @param index The index where the row will be inserted
	 */
	private void addRow(int index) {
		rows.add(index, new Row(rows.get(0).cells.size()));
	}

//INNER CLASS ObjectNotFoundException
	private static class ObjectNotFoundException extends Exception {
		private static final long serialVersionUID = -6551241499149294834L;
	}
	
//INNER CLASS Row
	/**
	 * A row has several cells. It allows to have a clear and flexible structure in grid.
	 */
	private class Row {
	//ATTRIBUTES
		private List<E> cells;
	
	//CONSTRUCTOR
		/**
		 * Creates a row with one empty cell.
		 */
		private Row() {
			cells = new ArrayList<E>();
			cells.add(null);
		}
		
		/**
		 * Creates a row with "size" empty cells.
		 * @param size The size of this row
		 */
		private Row(int size) {
			this();
			for(int i=1; i < size; i++) {
				cells.add(null);
			}
		}
		
	//MODIFIERS
		/**
		 * Adds a cell at given index.
		 * Current and next elements are shifted to right.
		 * @param index The index where the cell will be inserted.
		 */
		private void addCell(int index) {
			cells.add(index, null);
		}
	}
}
