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

package info.pavie.opennetworkmap.model.draw.style;

import info.pavie.opennetworkmap.model.network.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.osbcp.cssparser.PropertyValue;
import com.osbcp.cssparser.Rule;
import com.osbcp.cssparser.Selector;

/**
 * This class represents a style rule.
 * This rule applies if necessary tags are contained in a network {@link Component}.
 * If the rule applies, some properties can be associated to the given object.
 */
public class TagBasedRule {
//ATTRIBUTES
	/** The necessary tags (OSM Key => OSM Value, or null for all values)**/
	private Map<String,String> onTags;
	/** The style properties **/
	private List<PropertyValue> properties;

//CONSTRUCTOR
	/**
	 * Class constructor
	 * @param s The CSS selector
	 * @param p The CSS properties
	 * @throws ParseException If an error occurs during parsing
	 */
	private TagBasedRule(Selector s, List<PropertyValue> p) throws ParseException {
		properties = p;
		onTags = new HashMap<String,String>(0);
		parseSelector(s);
	}

//ACCESSORS
	/**
	 * @return The necessary tags to apply this rule
	 */
	public Map<String,String> getSelectorTags() {
		return onTags;
	}
	
	/**
	 * @return The applyable properties
	 */
	public List<PropertyValue> getProperties() {
		return properties;
	}

//OTHER METHODS
	/**
	 * Parse the tags in the given selector and sets the onTags map
	 * @param s The selector
	 * @throws ParseException If parsed selector is invalid
	 */
	private void parseSelector(Selector s) throws ParseException {
		String v = s.toString();
		String regex = "(\\w+|\\*)(\\[\\w+(:\\w+)*(=\\w+(:\\w+)*)?\\])*";
		
		//Test if selector is recognized
		if(!v.matches(regex)) {
			throw new ParseException("Parsed selector '"+v+"' is invalid");
		}
		
		String[] textTags = v.split("\\[");
		String tmp;
		for(int i=0; i < textTags.length; i++) {
			tmp = textTags[i];
			if(i > 0) { tmp = tmp.substring(0, tmp.length()-1); } //Remove ending "]"
			
			//Geometry rules
			if(i == 0) {
				switch(tmp) {
					case "*":
						break;
					case "grid":
						onTags.put("grid", "yes");
						break;
					case "link":
						onTags.put("link", "yes");
						break;
					case "node":
						onTags.put("node", "yes");
						break;
					case "meta":
						onTags.put("meta", "yes");
						break;
					default:
						throw new ParseException("Geometry selector: "+tmp+" is invalid");
				}
			}
			//Tags rules
			else {
				String[] keyValue = tmp.split("=");
				onTags.put(keyValue[0], (keyValue.length == 2) ? keyValue[1] : null);
			}
		}
	}
	
	/**
	 * This method parses TagBasedRules from CSS rules 
	 * @param rules The CSS rules
	 * @return The tag-based rules
	 */
	public static List<TagBasedRule> parseRules(List<Rule> rules) {
		List<TagBasedRule> result = new ArrayList<TagBasedRule>(rules.size());
		
		for(Rule r : rules) {
			for(Selector s : r.getSelectors()) {
				try {
					result.add(new TagBasedRule(s, new ArrayList<PropertyValue>(r.getPropertyValues())));
				} catch (ParseException e) {
					System.err.println(e.getMessage());
				}
			}
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		String res = "Rule\n";
		for(String key : onTags.keySet()) {
			res += "  "+key+" => "+onTags.get(key)+"\n";
		}
		res += " Properties\n";
		for(PropertyValue pv : properties) {
			res += "  "+pv.getProperty()+" => "+pv.getValue()+"\n";
		}
		return res;
	}
}
