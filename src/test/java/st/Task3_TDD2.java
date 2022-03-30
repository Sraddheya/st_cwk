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
	
	@Test
	public void test() {
		parser.addAll("opt1 opt2 opt3 opt4", "o1, o2, o3, o4", "String Integer Boolean Character");
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
	}
	
	@Test
	public void test2() {
		parser.addAll("opt1-4", "o1-4", "String Integer Boolean Character");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.optionExists("opt2"));
		assertTrue(parser.optionExists("opt3"));
		assertTrue(parser.optionExists("opt4"));
	}

}
