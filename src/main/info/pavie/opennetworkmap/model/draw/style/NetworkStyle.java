package info.pavie.opennetworkmap.model.draw.style;

import info.pavie.opennetworkmap.model.network.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.osbcp.cssparser.PropertyValue;

/**
 * The network style is a list of {@link TagBasedRule} objects.
 * @author Adrien PAVIE
 */
public class NetworkStyle {
//ATTRIBUTES
	/** The list of rules to define network style **/
	private List<TagBasedRule> style;

//CONSTRUCTOR
	/**
	 * Class constructor
	 * @param s The list of style rules
	 */
	public NetworkStyle(List<TagBasedRule> s) {
		style = s;
	}
	
	/**
	 * Default class constructor, create an empty style
	 */
	public NetworkStyle() {
		this(new ArrayList<TagBasedRule>());
	}

//ACCESSORS
	/**
	 * @return The style rules for network in general
	 */
	public Map<String,String> getGridStyle() {
		Map<String,String> result = new HashMap<String,String>(0);
		
		for(TagBasedRule tbr : style) {
			//Set style
			if(tbr.getSelectorTags().get("grid") != null 
					&& tbr.getSelectorTags().get("grid").equals("yes")) {
				for(PropertyValue p : tbr.getProperties()) {
					result.put(p.getProperty(), p.getValue());
				}
			}
		}
		
		return result;
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
		Map<String,String> result = new HashMap<String,String>(0);
		
		for(TagBasedRule tbr : style) {
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
					result.put(p.getProperty(), p.getValue());
				}
			}
		}
		
		//Replace fill color value by color tag if defined
		if(c.getTags().containsKey("colour")) {
			result.put("fill", c.getTags().get("colour"));
		}
		
		return result;
	}

//OTHER METHODS
	/**
	 * Is the value valid compared to the rule value ?
	 * @param rule The rule value (null for any value)
	 * @param actual The actual value
	 * @return True if actual value accepted, false else
	 */
	private boolean isTagValueValid(String rule, String actual) {
		return rule == null || rule.equals(actual);
	}
}
