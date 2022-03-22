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
	
	/**
	//AddAll-------------------------------------------------------------------------------------------
	@Test
	public void testOptionExists() {
		parser.addAll("opt1 opt2 opt3 opt4", "o1 o2 o3 o4", "String Ineteger Boolean Character");
		assertTrue(parser.optionOrShortcutExists("opt1"));
		assertTrue(parser.optionOrShortcutExists("opt2"));
		assertTrue(parser.optionOrShortcutExists("opt3"));
		assertTrue(parser.optionOrShortcutExists("opt4"));
	}
	
	@Test
	public void testShortcutExists() {
		parser.addAll("opt1 opt2 opt3 opt4", "o1 o2 o3 o4", "String Ineteger Boolean Character");
		assertTrue(parser.optionOrShortcutExists("o1"));
		assertTrue(parser.optionOrShortcutExists("o2"));
		assertTrue(parser.optionOrShortcutExists("o3"));
		assertTrue(parser.optionOrShortcutExists("o4"));
	}
	
	@Test
	public void testCorrectType() {
		parser.addAll("opt1 opt2 opt3 opt4", "o1 o2 o3 o4", "String Ineteger Boolean Character");
		assertEquals(parser.getString("opt1"), "");
		assertEquals(parser.getInteger("opt2"), 0);
		assertEquals(parser.getBoolean("opt3"), false);
		assertEquals(parser.getCharacter("opt4"), '\0');
	}
	
	@Test
	public void testNoSpaceOption() {
		parser.addAll("opt1opt2", "String String");
		assertFalse(parser.optionOrShortcutExists("opt1"));
		assertFalse(parser.optionOrShortcutExists("opt2"));
		assertTrue(parser.optionOrShortcutExists("opt1opt2"));
	}
	
	@Test
	public void testNoSpaceShortcut() {
		parser.addAll("opt1 opt2", "o1o2", "String String");
		assertTrue(parser.optionOrShortcutExists("opt1"));
		assertTrue(parser.optionOrShortcutExists("opt2"));
		assertTrue(parser.optionOrShortcutExists("o1o2"));
		assertFalse(parser.optionOrShortcutExists("o2"));
	}
	
	@Test
	public void testExtraSpaceOption() {
		parser.addAll("opt1     opt2", "String String");
		assertTrue(parser.optionOrShortcutExists("opt1"));
		assertTrue(parser.optionOrShortcutExists("opt2"));
	}
	
	@Test
	public void testExtraSpaceShortcut() {
		parser.addAll("opt1 opt2", "o1     o2", "String String");
		assertTrue(parser.optionOrShortcutExists("o1"));
		assertTrue(parser.optionOrShortcutExists("o2"));
	}
	
	@Test
	public void testMoreOptionsThanShortcuts() {
		parser.addAll("opt1 opt2", "o1", "String String");
		assertTrue(parser.optionOrShortcutExists("opt1"));
		assertTrue(parser.optionOrShortcutExists("opt2"));
		assertTrue(parser.optionOrShortcutExists("o1"));
	}
	
	@Test
	public void testLessOptionsThanShortcuts() {
		parser.addAll("opt1", "o1 o2", "String");
		assertTrue(parser.optionOrShortcutExists("opt1"));
		assertTrue(parser.optionOrShortcutExists("o1"));
		assertFalse(parser.optionOrShortcutExists("opt2"));
		assertFalse(parser.optionOrShortcutExists("o2"));
	}
	
	@Test
	public void testMoreOptionsThanTypes() {
		parser.addAll("opt1 opt2 opt3", "String Integer");
		assertEquals(parser.getString("opt1"), "");
		assertEquals(parser.getInteger("opt2"), 0);
		assertEquals(parser.getInteger("opt3"), 0);
	}
	
	@Test
	public void testLessOptionsThanTypes() {
		parser.addAll("opt1", "String Integer String");
		assertTrue(parser.optionOrShortcutExists("opt1"));
		assertEquals(parser.getString("opt1"), "");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void specialCharInOptionName() {
		parser.addAll("o!$%^&*#@", "String");
		assertFalse(parser.optionExists("o!$%^&*#@"));
	}
	
	@Test
	public void normalCharInOptionName1() {
		parser.addAll("abcdefghijklmnopqrstuvwxyz_", "String");
		assertTrue(parser.optionExists("abcdefghijklmnopqrstuvwxyz_"));
	}
	
	@Test
	public void normalCharInOptionName2() {
		parser.addAll("ABCDEFGHIJKLMNOPQRSTUVWXYZ_", "String");
		assertTrue(parser.optionExists("ABCDEFGHIJKLMNOPQRSTUVWXYZ_"));
	}
	
	@Test
	public void normalCharInOptionName3() {
		parser.addAll("0123456789", "String");
		assertTrue(parser.optionExists("0123456789"));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void optionDigitFirstLetter() {
		parser.addAll("0opt", "String");
		assertFalse(parser.optionExists("0opt"));
	}
	
	@Test
	public void optionCaseSensitive() {
		parser.addAll("Opt opt", "String String");
		assertTrue(parser.optionExists("Opt"));
		assertTrue(parser.optionExists("opt"));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void specialCharInShortcutName() {
		parser.addAll("opt", "o!$%^&*#@", "String");
		assertFalse(parser.shortcutExists("o!$%^&*#@"));
	}
	
	@Test
	public void normalCharInShortcutName1() {
		parser.addAll("opt", "abcdefghijklmnopqrstuvwxyz_", "String");
		assertTrue(parser.shortcutExists("abcdefghijklmnopqrstuvwxyz_"));
	}
	
	@Test
	public void normalCharInShortcutName2() {
		parser.addAll("ABCDEFGHIJKLMNOPQRSTUVWXYZ_", "String");
		assertTrue(parser.shortcutExists("ABCDEFGHIJKLMNOPQRSTUVWXYZ_"));
	}
	
	@Test
	public void normalCharInShortcutName3() {
		parser.addAll("0123456789", "String");
		assertTrue(parser.optionExists("0123456789"));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void shortcutDigitFirstLetter() {
		parser.addAll("opt", "0o", "String");
		assertFalse(parser.shortcutExists("0o"));
	}
	
	@Test
	public void shortcutCaseSensitive() {
		parser.addAll("opt1 opt2", "o O", "String String");
		assertTrue(parser.shortcutExists("o"));
		assertTrue(parser.shortcutExists("O"));
	}
	
	//Group Initialisation---------------------------------------------------
	@Test
	public void groupRangeIncreasing() {
		parser.addAll("opt129-11", "String");
		assertTrue(parser.optionExists("opt129"));
		assertTrue(parser.optionExists("opt1210"));
		assertTrue(parser.optionExists("opt1211"));
		assertFalse(parser.optionExists("opt130"));
		assertFalse(parser.optionExists("opt131"));
	}
	
	@Test
	public void groupRangeDecreasing() {
		parser.addAll("opt129-6", "String");
		assertTrue(parser.optionExists("opt129"));
		assertTrue(parser.optionExists("opt127"));
		assertTrue(parser.optionExists("opt126"));
	}
	
	@Test
	public void mutipleGroupsOneShortcut() {
		parser.addAll("opt1-2 opt3-4", "o1-4", "String String");
		assertTrue(parser.optionExists("opt1"));
		assertTrue(parser.optionExists("opt2"));
		assertTrue(parser.optionExists("opt3"));
		assertTrue(parser.optionExists("opt4"));
		assertTrue(parser.shortcutExists("o1"));
		assertTrue(parser.shortcutExists("o2"));
		assertTrue(parser.shortcutExists("o3"));
		assertTrue(parser.shortcutExists("o4"));
	}
	
	@Test
	public void mutipleGroupsOneType() {
		parser.addAll("opt1-2 opt3-4", "String");
		assertEquals(parser.getString("opt1"), "");
		assertEquals(parser.getString("opt2"), "");
		assertEquals(parser.getString("opt3"), "");
		assertEquals(parser.getString("opt4"), "");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void invalidType() {
		parser.addAll("opt1-2", "o1-2", "something");
		assertFalse(parser.optionExists("opt1"));
	}
	**/
}
