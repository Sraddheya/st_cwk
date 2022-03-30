package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task3_TDD2 {
	
	private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	//Extra
	@Test
	public void testSingleGroupCombination() {
		parser.addAll("opt1 opt2-4", "o1 o2-4", "String Integer");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.optionExists("opt2"));
		assertTrue(parser.optionExists("opt3"));
		assertTrue(parser.optionExists("opt4"));
		assertTrue(parser.shortcutExists("o1"));
		assertTrue(parser.shortcutExists("o2"));
		assertTrue(parser.shortcutExists("o3"));
		assertTrue(parser.shortcutExists("o4"));
		assertEquals(parser.getString("opt1"), "");
		assertEquals(parser.getInteger("opt2"), 0);
		assertEquals(parser.getInteger("opt3"), 0);
		assertEquals(parser.getInteger("opt4"), 0);
	}

}
