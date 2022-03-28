package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task3_TDD1 {
	
	private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}

	
	//AddAll SPEC 1 AND 4-------------------------------------------------------------------------------------------
	@Test
	public void testOptionExists() {
		parser.addAll("opt1 opt2 opt3 opt4", "String Integer Boolean Character");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.optionExists("opt2"));
		assertTrue(parser.optionExists("opt3"));
		assertTrue(parser.optionExists("opt4"));
	}
	/**
	@Test
	public void testShortcutExists() {
		parser.addAll("opt1 opt2 opt3 opt4", "o1 o2 o3 o4", "String Integer Boolean Character");
		assertTrue(parser.shortcutExists("o1"));
		assertTrue(parser.shortcutExists("o2"));
		assertTrue(parser.shortcutExists("o3"));
		assertTrue(parser.shortcutExists("o4"));
	}
	
	@Test
	public void testCorrectType() {
		parser.addAll("opt1 opt2 opt3 opt4", "o1 o2 o3 o4", "String Integer Boolean Character");
		assertEquals(parser.getString("opt1"), "");
		assertEquals(parser.getInteger("opt2"), 0);
		assertEquals(parser.getBoolean("opt3"), false);
		assertEquals(parser.getCharacter("opt4"), '\0');
	}
	
	//AddAll SPEC 2 AND 3-------------------------------------------------------------------------------------------
	@Test
	public void testOptionSpace() {
		parser.addAll("opt1    opt2", "o1    o2", "String     String");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.optionExists("opt2"));
	}
	
	@Test
	public void testShortcutSpace() {
		parser.addAll("opt1    opt2", "o1    o2", "String     String");
		assertTrue(parser.shortcutExists("o1"));
		assertTrue(parser.shortcutExists("o2"));
	}
	
	@Test
	public void testTypeSpace() {
		parser.addAll("opt1 opt2 opt3 opt4", "o1 o2 o3 o4", "String      Integer      Boolean      Character");
		assertEquals(parser.getString("opt1"), "");
		assertEquals(parser.getInteger("opt2"), 0);
		assertEquals(parser.getBoolean("opt3"), false);
		assertEquals(parser.getCharacter("opt4"), '\0');
	}
	
	//AddAll SPEC 5-------------------------------------------------------------------------------------------
	@Test
	public void testMoreOptionsThanShortcuts() {
		parser.addAll("opt1 opt2", "o1", "String String");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.optionExists("opt2"));
		assertTrue(parser.shortcutExists("o1"));
	}
	
	//AddAll SPEC 6-------------------------------------------------------------------------------------------
	@Test
	public void testLessOptionsThanShortcuts() {
		parser.addAll("opt1", "o1 o2", "String");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.shortcutExists("o1"));
		assertFalse(parser.shortcutExists("o2"));
	}
	
	@Test
	public void testLessOptionsThanTypes() {
		parser.addAll("opt1", "o1", "String Integer Boolean");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.shortcutExists("o1"));
		assertEquals(parser.getString("opt1"), "");
	}
	
	//AddAll SPEC 7-------------------------------------------------------------------------------------------
	@Test
	public void testMoreOptionsThanTypes() {
		parser.addAll("opt1 opt2 opt3", "o1 o2 o3", "String Integer");
		assertEquals(parser.getString("opt1"), "");
		assertEquals(parser.getInteger("opt2"), 0);
		assertEquals(parser.getInteger("opt3"), 0);
	}
	
	//GroupInit SPEC 1 AND 3 AND 7-------------------------------------------------------------------------------------------
	@Test
	public void testBasicGroupUpperAlpha() {
		parser.addAll("optA-C", "oA-C", "String");
		assertTrue(parser.optionExists("optA"));
		assertTrue(parser.optionExists("optB"));
		assertTrue(parser.optionExists("optC"));
		assertTrue(parser.shortcutExists("oA"));
		assertTrue(parser.shortcutExists("oB"));
		assertTrue(parser.shortcutExists("oC"));
		assertEquals(parser.getString("optA"), "");
		assertEquals(parser.getString("optB"), "");
		assertEquals(parser.getString("optC"), "");
	}
	
	@Test
	public void testBasicGroupLowerAlpha() {
		parser.addAll("opta-c", "oa-c", "String");
		assertTrue(parser.optionExists("opta"));
		assertTrue(parser.optionExists("optb"));
		assertTrue(parser.optionExists("optc"));
		assertTrue(parser.shortcutExists("oa"));
		assertTrue(parser.shortcutExists("ob"));
		assertTrue(parser.shortcutExists("oc"));
		assertEquals(parser.getString("opta"), "");
		assertEquals(parser.getString("optb"), "");
		assertEquals(parser.getString("optc"), "");
	}
	
	@Test
	public void testBasicGroupNum() {
		parser.addAll("opt1-3", "o1-3", "String");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.optionExists("opt2"));
		assertTrue(parser.optionExists("opt3"));
		assertTrue(parser.shortcutExists("o1"));
		assertTrue(parser.shortcutExists("o2"));
		assertTrue(parser.shortcutExists("o3"));
		assertEquals(parser.getString("opt1"), "");
		assertEquals(parser.getString("opt2"), "");
		assertEquals(parser.getString("opt3"), "");
	}
	
	//GroupInit SPEC 2 AND 5-------------------------------------------------------------------------------------------
	@Test
	public void testUnderscore() {
		parser.addAll("opt_1-2", "o_1-2", "String");
		assertTrue(parser.optionExists("opt_1"));
		assertTrue(parser.optionExists("opt_2"));
		assertTrue(parser.shortcutExists("o_1"));
		assertTrue(parser.shortcutExists("o_2"));
	}
	
	@Test
	public void testAllLowerOption() {
		parser.addAll("abcdefghijklmnopqrstuvwxyz1-2", "o1-2", "String");
		assertTrue(parser.optionExists("abcdefghijklmnopqrstuvwxyz1"));
		assertTrue(parser.optionExists("abcdefghijklmnopqrstuvwxyz2"));
		assertTrue(parser.shortcutExists("o1"));
		assertTrue(parser.shortcutExists("o2"));
	}
	
	@Test
	public void testAllUpperOption() {}
	
	@Test
	public void testAllNumericOption() {}
	
	@Test
	public void testSpecialCharOption() {}
	
	@Test
	public void testAllLowerShortcut() {}
	
	@Test
	public void testAllUpperShortcut() {}
	
	@Test
	public void testAllNumericShortcut() {}
	
	@Test
	public void testSpecialCharShortcut() {}
	
	@Test
	public void testRange() {}
	 
	//GroupInit SPEC 4-------------------------------------------------------------------------------------------
	@Test (expected=IllegalArgumentException.class)
	public void testMixRange1() {
		parser.addAll("opt1-C", "o1-C", "String");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testMixRange2() {
		parser.addAll("opt1-c", "o1-c", "String");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testMixRange3() {
		parser.addAll("optA-c", "oA-c", "String");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testMixRange4() {
		parser.addAll("opt1A-3", "o1A-3", "String");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testMixRange5() {
		parser.addAll("optA1-C", "oA1-C", "String");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testMixRange6() {
		parser.addAll("optaA-c", "oaA-c", "String");
	}
	
	//GroupInit SPEC 6-------------------------------------------------------------------------------------------
	
	//GroupInit SPEC 8-------------------------------------------------------------------------------------------
	@Test
	public void groupRangeIncreasing() {
		parser.addAll("opt129-11", "String");
		assertTrue(parser.optionExists("opt129"));
		assertTrue(parser.optionExists("opt1210"));
		assertTrue(parser.optionExists("opt1211"));
		assertFalse(parser.optionExists("opt130"));
		assertFalse(parser.optionExists("opt131"));
	}
	
	//GroupInit SPEC 10-------------------------------------------------------------------------------------------
	@Test
	public void groupRangeDecreasingNum() {
		parser.addAll("opt129-6", "String");
		assertTrue(parser.optionExists("opt129"));
		assertTrue(parser.optionExists("opt127"));
		assertTrue(parser.optionExists("opt126"));
	}
	
	@Test
	public void groupRangeDecreasingUpperAlpha() {
		parser.addAll("optABZ-X", "String");
		assertTrue(parser.optionExists("optABZ"));
		assertTrue(parser.optionExists("optABY"));
		assertTrue(parser.optionExists("optABX"));
	}
	
	@Test
	public void groupRangeDecreasingLowerAlpha() {
		parser.addAll("optabz-x", "String");
		assertTrue(parser.optionExists("optabz"));
		assertTrue(parser.optionExists("optaby"));
		assertTrue(parser.optionExists("optabx"));
	}
	
	//GroupInit SPEC 11-------------------------------------------------------------------------------------------
	@Test
	public void testMultipleGroupsSameShortcut() {
		parser.addAll("opt1-2 opt3-4", "o1-4", "String");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.optionExists("opt2"));
		assertTrue(parser.optionExists("opt3"));
		assertTrue(parser.optionExists("opt4"));
		assertTrue(parser.shortcutExists("o1"));
		assertTrue(parser.shortcutExists("o2"));
		assertTrue(parser.shortcutExists("o3"));
		assertTrue(parser.shortcutExists("o4"));
	}
	
	//GroupInit SPEC 12-------------------------------------------------------------------------------------------
	@Test (expected=IllegalArgumentException.class)
	public void testInvalidType() {
		parser.addAll("opt1-3", "o1-3", "NoType String");
	}
	**/
}
