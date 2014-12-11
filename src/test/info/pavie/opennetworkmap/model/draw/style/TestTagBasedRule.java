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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.osbcp.cssparser.CSSParser;
import com.osbcp.cssparser.Rule;

/**
 * Test class for {@link TagBasedRule}
 * @author Adrien PAVIE
 */
public class TestTagBasedRule {
//ATTRIBUTES
	private List<Rule> r1, r2;
	private List<TagBasedRule> tbr1, tbr2;
	
//SETUP
	@Before
	public void setUp() throws Exception {
		r1 = CSSParser.parse(
				"grid { node-margins: 70; }"
				);
		tbr1 = TagBasedRule.parseRules(r1);
		
		r2 = CSSParser.parse(
				"node[type=first] { shape: lozenge; width: 15; }"
				);
		tbr2 = TagBasedRule.parseRules(r2);
	}

//TESTS
// getSelectorTags()
	@Test
	public void testGetSelectorTagsSimple() {
		assertEquals(1, tbr1.get(0).getSelectorTags().size());
		assertEquals("yes", tbr1.get(0).getSelectorTags().get("grid"));
	}
	
	@Test
	public void testGetSelectorTagsOneRuleTwoProps() {
		assertEquals(2, tbr2.get(0).getSelectorTags().size());
		assertEquals("yes", tbr2.get(0).getSelectorTags().get("node"));
		assertEquals("first", tbr2.get(0).getSelectorTags().get("type"));
	}

// getProperties()
	@Test
	public void testGetPropertiesSimple() {
		assertEquals(1, tbr1.get(0).getProperties().size());
		assertEquals("node-margins", tbr1.get(0).getProperties().get(0).getProperty());
		assertEquals("70", tbr1.get(0).getProperties().get(0).getValue());
	}
	
	@Test
	public void testGetPropertiesOneRuleTwoProps() {
		assertEquals(2, tbr2.get(0).getProperties().size());
		assertEquals("shape", tbr2.get(0).getProperties().get(0).getProperty());
		assertEquals("lozenge", tbr2.get(0).getProperties().get(0).getValue());
		assertEquals("width", tbr2.get(0).getProperties().get(1).getProperty());
		assertEquals("15", tbr2.get(0).getProperties().get(1).getValue());
	}

// parseRules()
	@Test
	public void testParseRulesSimple() {
		assertEquals(1, tbr1.size());
	}
	
	@Test
	public void testParseRulesOneRuleTwoProps() {
		assertEquals(1, tbr2.size());
	}
}
