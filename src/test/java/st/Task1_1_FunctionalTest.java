package st;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Task1_1_FunctionalTest {
	
    private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	
	//SPECIFICATION 1.3------------------------------------------
	
	/**SPECIFICATION 1.3.1
	 * Adding an option with the same name as an existing option will lead into overwriting the old type only
       (as name is the same) in the already existing option object held in the options Map held internally
       in the OptionMap class. 
     * The old Option object is preserved in this case, so previously defined shortcuts will be able to access it
     * The old Option object is preserved in this case, so its old value is also preserved.
	 */
	
	
	/**@Test
	public void addOption() {
		parser.addOption(new Option("output", Type.STRING), "o");
		assertEquals(parser.getOption("output"), Type.STRING);
		parser.addOption(new Option("output", Type.INTEGER), "p");
		assertEquals(parser.getType("output"), Type.INTEGER);
	}**/
	
	/**
	@Test (expected=IllegalArgumentException.class)
	public void optionNamingChars() {
		//Names
		parser.addOption(new Option("1output", Type.STRING));
		parser.addOption(new Option("*output", Type.STRING));
		//Shortcuts
		parser.addOption(new Option("output", Type.STRING), "1o");
		parser.addOption(new Option("output", Type.STRING), "*o");
	}
	
	@Test //[Bug #1 - Easy, 1PT]
	public void sameOptionDifferentShortcut() {
		parser.addOption(new Option("input", Type.INTEGER), "");
	}
	
	@Test //[Bug #1 - Easy, 1PT]
	public void optionNameCases() {
		parser.addOption(new Option("output", Type.STRING));
		assertFalse(parser.optionExists("OutPut"));
		parser.addOption(new Option("OutPut", Type.STRING));
		assertTrue(parser.optionExists("OutPut"));
	}
	
	@Test //[Bug #1 - Easy, 1PT]
	public void optionShortcutCases() {
		parser.addOption(new Option("output", Type.STRING), "out");
		assertFalse(parser.shortcutExists("Out"));
		parser.setShortcut("output", "Out");
		assertTrue(parser.shortcutExists("Out"));
	}
	
	@Test //[Bug #1 - Easy, 1PT] //(expected=IllegalArgumentException.class)
	public void optionShortcutSameName() {
		parser.addOption(new Option("output", Type.STRING), "o");
		parser.addOption(new Option("o", Type.INTEGER));
		parser.parse("--o hello");
		//assertEquals(parser.getString("output"), "hello");
	}**/

	
	/** [Bug #2 - Easy, 1PT]--------------------------------------------------------------------
	@Test 
	public void booleanValues1() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=False");
		assertFalse(parser.getBoolean("option"));
		parser.parse("--option=false");
		assertFalse(parser.getBoolean("option"));
	}
	
	@Test 
	public void booleanValues2() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=0");
		assertFalse(parser.getBoolean("option"));
	}
	
	@Test
	public void booleanValues3() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		parser.parse("--option=");
		assertFalse(parser.getBoolean("option"));
		parser.parse("-o=");
		assertFalse(parser.getBoolean("option"));
	}
	
	@Test
	public void booleanValues4() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=True");
		assertTrue(parser.getBoolean("option"));
		parser.parse("--option=true");
		assertTrue(parser.getBoolean("option"));
	}
	
	@Test
	public void booleanValues5() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=1");
		assertTrue(parser.getBoolean("option"));
	}
	
	@Test
	public void booleanValues6() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=anything");
		assertTrue(parser.getBoolean("option"));
	}
	
	@Test
	public void booleanValues7() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=10");
		assertTrue(parser.getBoolean("option"));
	}**/
	
	/** [Bug #3 - Medium, 2PTS] ----------------------------------------------------------------------
	@Test
	public void getIntegerTest1() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=something");
		assertEquals(parser.getInteger("option"), 1);
		}
	
	@Test
	public void getIntegerTest2() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=false");
		assertEquals(parser.getInteger("option"), 0);
		}**/
	
	/** [Bug #4 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test
	public void shortcutLength() {
		parser.addOption(new Option("option", Type.STRING), "oooooooooooooooooooooooooooooooooooooooooooooooooooo");
		assertTrue(parser.optionOrShortcutExists("oooooooooooooooooooooooooooooooooooooooooooooooooooo"));
	}**/
	
	/** [Bug #5 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test
	public void negativeInteger() {
		parser.addOption(new Option("input", Type.INTEGER));
		parser.parse("--input=-1");
		assertEquals(parser.getInteger("input"), -1);
	}**/
	
	/** [Bug #8 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test 
	public void optionExists() {
		parser.addOption(new Option("option", Type.INTEGER));
		parser.addOption(new Option("option", Type.INTEGER));
	}**/
	
	/** [Bug #9 - Easy, 1PT]--------------------------------------------------------------------------
	@Test
	public void blankSpace() {
		parser.parse(" ");
	}**/
	
	/** [Bug #10 - Easy, 1PT]-------------------------------------------------------------------------
	@Test
	public void nullChar() {
		parser.addOption(new Option("option", Type.CHARACTER));
		parser.parse("--option=\0");
		assertEquals(parser.getCharacter("option"), null);
	}
	
	@Test
	public void whiteSpaceChar() {
		parser.addOption(new Option("option", Type.CHARACTER));
		parser.parse("--option= ");
		assertEquals(parser.getInteger("option"), null);
		}**/
	
	/** [Bug #11 - Hard, 3PTS]------------------------------------------------------------------------
	@Test //
	public void specialCharInOptionName1() {
		parser.addOption(new Option("option!", Type.STRING));
		assertFalse(parser.optionExists("option!"));
	}
	
	@Test
	public void specialCharInOptionName2() {
		parser.addOption(new Option("option$", Type.STRING));
		assertFalse(parser.optionExists("option$"));
	}
	
	@Test
	public void specialCharInOptionName3() {
		parser.addOption(new Option("option%", Type.STRING));
		assertFalse(parser.optionExists("option%"));
	}
	
	@Test
	public void specialCharInOptionName4() {
		parser.addOption(new Option("option^", Type.STRING));
		assertFalse(parser.optionExists("option^"));
	}
	
	@Test
	public void specialCharInOptionName5() {
		parser.addOption(new Option("option&", Type.STRING));
		assertFalse(parser.optionExists("option&"));
	}
	
	@Test
	public void specialCharInOptionName6() {
		parser.addOption(new Option("option#", Type.STRING));
		assertFalse(parser.optionExists("option#"));
	}
	
	@Test
	public void specialCharInOptionName7() {
		parser.addOption(new Option("option@", Type.STRING));
		assertFalse(parser.optionExists("option@"));
	}**/
	
	/**
	@Test (expected=IllegalArgumentException.class)
	public void specialCharInOptionName8() {
		parser.addOption(new Option("0option", Type.STRING));
		assertFalse(parser.optionExists("0option"));
	}**/
	
	/**
	public void optionNaming1() {
		parser.addOption(new Option("option", Type.STRING));
		assertFalse(parser.optionExists("option*"));
	}
	
	public void optionNaming2() {
		parser.addOption(new Option("option", Type.STRING));
		assertFalse(parser.optionExists("option*"));
	}**/
	
	/** [Bug #12 - Hard, 3PTS]-----------------------------------------------------------------------
	@Test
	public void replaceTest1() {
		parser.addOption(new Option("option", Type.STRING));
		parser.parse("--option=old");
		parser.replace("--option", "old", "new");
		assertEquals(parser.getString("option"), "new");
	}
	
	@Test
	public void replaceTest2() {
		parser.addOption(new Option("option", Type.STRING));
		parser.parse("--option=old");
		parser.replace("option", "old", "new");
		assertEquals(parser.getString("option"), "new");
	}
	
	@Test
	public void replaceTest3() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("-o=old");
		parser.replace("-o", "old", "new");
		assertEquals(parser.getString("option"), "new");
	}
	
	@Test
	public void replaceTest5() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("-o=old");
		parser.replace("o", "old", "new");
		assertEquals(parser.getString("option"), "new");
	}**/
	
	/**[Bug #14 -  Hard, 3PTS]------------------------------------------------------------------------
	@Test 
	public void newLineChar() {
		parser.addOption(new Option("option", Type.CHARACTER));
		parser.parse("--option=\\n");
		assertEquals(parser.getInteger("option"), "\\n");
		}**/
	
	/**[Bug #15 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test
	public void largeInteger() {
		parser.addOption(new Option("option", Type.STRING));
		parser.parse("--option=9999999999");
		assertEquals(parser.getInteger("option"), 9999999999L);
		}**/
	
	/** [Bug #16 - Medium, 2PTS]
	@Test
	public void nullOption() {
		parser.getString(null);
	}**/
	
	/**
	@Test //[Bug #17 - Hard, 3PTS]
	public void optionLength() {
		parser.addOption(new Option("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", Type.STRING));
		assertTrue(parser.optionOrShortcutExists("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"));
	}**/
	
	/**
	@Test //[Bug #18 - Easy, 1PT]
	public void charSpace() {
		parser.addOption(new Option("opt1", Type.STRING));
		parser.addOption(new Option("opt2", Type.STRING));
		parser.parse("opt1=OldText1 --opt2=OldText2");
		parser.replace("opt1     opt2", "Old", "New");
		assertEquals(parser.getString("opt1"), "OldText1");
		assertEquals(parser.getString("opt2"), "OldText2");
	}**/
	
	/**
	@Test //[Bug #20 - Hard, 3PTS]
	public void inputWithSpace() {
		parser.addOption(new Option("input", Type.STRING));
		parser.parse("--input=hello world");
		assertEquals(parser.getString("input"), "hello world");
	}**/
	
	/**
	@Test
	public void caseSensitiveNames() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.addOption(new Option("Option", Type.STRING), "O");
		assertTrue(parser.optionOrShortcutExists("option"));
		assertTrue(parser.optionOrShortcutExists("Option"));
		assertTrue(parser.optionOrShortcutExists("o"));
		assertTrue(parser.optionOrShortcutExists("O"));
	}
	
	@Test
	public void shortcutOptionSameName() {
		parser.addOption(new Option("o", Type.STRING));
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--o=hello");
		parser.parse("-o=hello");
		assertEquals(parser.getString("o"), "hello");
		assertEquals(parser.getString("option"), "hello");
	}**/
	
}
