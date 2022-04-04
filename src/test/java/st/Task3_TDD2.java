package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task3_TDD2 {
	
private Task3_Parser parser;
	
	@Before
	public void setUp() {
		parser = new Task3_Parser();
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullValues1() {
		parser.addAll("opt", null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullValues2() {
		parser.addAll("opt", null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullValues3() {
		parser.addAll(null, "o", "String");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullValues4() {
		parser.addAll(null, "String");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullValues5() {
		parser.addAll(null, null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullValues6() {
		parser.addAll(null, null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyValues1() {
		parser.addAll("opt", "", "");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyValues2() {
		parser.addAll("opt", "");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyValues3() {
		parser.addAll("", "o", "String");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyValues4() {
		parser.addAll("", "String");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyValues5() {
		parser.addAll("", "", "");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testEmptyValues6() {
		parser.addAll("", "");
	}
	
	@Test
	public void testEmptyValues7() {
		parser.addAll("opt", "", "String");
		assertTrue(parser.optionExists("opt"));
		assertFalse(parser.shortcutExists(""));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testWhitespace1() {
		parser.addAll("       ", "o","String");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testWhitespace2() {
		parser.addAll("opt", "       ");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testWhitespace3() {
		parser.addAll("opt", "     ", "    ");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testWhitespace4() {
		parser.addAll("      ", "String");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testWhitespace5() {
		parser.addAll("     ", "    ", "    ");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testWhitespace6() {
		parser.addAll("   ", "   ");
	}
	
	@Test
	public void testWhitespace7() {
		parser.addAll("opt", "   ", "String");
		assertTrue(parser.optionExists("opt"));
		assertFalse(parser.shortcutExists("   "));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInvalidType() {
		parser.addAll("opt1", "o", "abc");
	}
	
	@Test
	public void testInvalidGroup1() {
		parser.addAll("oA-AB", "String");
		assertFalse(parser.optionExists("oA"));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInvalidGroup2() {
		parser.addAll("1oA-B", "String");
		assertFalse(parser.optionExists("1oA"));
	}
	
	@Test
	public void testInvalidGroup3() {
		parser.addAll("o*A-B", "String");
		assertFalse(parser.optionExists("o*A"));
	}
	
	@Test
	public void testInvalidGroup4() {
		parser.addAll("opt*A-B opt", "oA-B o", "String");
		assertFalse(parser.optionExists("o*A"));
		parser.parse("-o=test");
		assertEquals(parser.getString("opt"), "test");
	}
	
	@Test
	public void testInvalidGroup5() {
		parser.addAll("o1-3", "o1-2a b c", "String");
		parser.parse("-b=testb");
		assertEquals(parser.getString("o1"), "testb");
		parser.parse("-c=testc");
		assertEquals(parser.getString("o2"), "testc");
		assertEquals(parser.getString("o3"), "");
	}
	
	@Test
	public void testInvalidGroup6() {
		parser.addAll("opt1 optionA-9 optA-C", "b c d e f", "String Integer Boolean");
		assertTrue(parser.optionExists("optC"));
		assertTrue(parser.optionExists("opt1"));
		assertFalse(parser.shortcutExists("c"));
		assertEquals(parser.getBoolean("optC"), false);
	}
}
