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
	
	/**
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
		parser.addAll(null, "o", String);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNullValues4() {
		parser.addAll(null, String);
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
	
	**/
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	/**@Test
	public void groupInitInvalidGroupTest3() {
		parser.addAll("2P1-3 oA-F", "Integer Integer");
		parser.parse("--oA=12");
		parser.parse("--oE=56");
		
		assertEquals(parser.getInteger("oA"), 12);
		assertEquals(parser.getInteger("oE"), 56);
	}
	
	@Test
	public void groupInitInvalidShortcutGroupTest() {
		parser.addAll("2P1-3 oA-F", "2op option&* ","Integer Integer");
		parser.parse("--oA=12");
		parser.parse("--oE=56");
		
		assertEquals(parser.getInteger("oA"), 12);
		assertEquals(parser.getInteger("oE"), 56);
	}**/
	
}
