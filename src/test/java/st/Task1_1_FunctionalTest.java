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

	/**
	@Test //[Bug #2 - Easy, 1PT]
	public void booleanValues() {
		parser.addOption(new Option("output", Type.BOOLEAN), "o");
		parser.parse("--output=0");
		assertFalse(parser.getBoolean("output"));
		parser.parse("-o=0");
		assertFalse(parser.getBoolean("output"));
		parser.parse("--output=false");
		assertFalse(parser.getBoolean("output"));
		parser.parse("-o=false");
		assertFalse(parser.getBoolean("output"));
		parser.parse("--output");
		assertFalse(parser.getBoolean("output"));
		parser.parse("-o");
		assertFalse(parser.getBoolean("output"));
		parser.parse("--output=anything");
		assertTrue(parser.getBoolean("output"));
		parser.parse("-o=anything");
		assertTrue(parser.getBoolean("output"));
	}
	
	@Test //[Bug #2 - Easy, 1PT]
	public void booleanValues() {
		parser.addOption(new Option("output", Type.BOOLEAN), "o");
		parser.parse("--output=False");
		assertFalse(parser.getBoolean("output"));
		parser.parse("--output=false");
		assertFalse(parser.getBoolean("output"));
	}**/
	
	/**
	@Test //[Bug #3 - Medium, 2PTS]
	public void getIntegerTest() {
		parser.addOption(new Option("input", Type.BOOLEAN));
		parser.parse("--input=something");
		assertEquals(parser.getInteger("input"), 1);
		}**/
	
	/**
	@Test //[Bug #4 - Medium, 2PTS]
	public void shortcutLength() {
		parser.addOption(new Option("input", Type.STRING), "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
		assertTrue(parser.optionOrShortcutExists("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"));
	}**/
	
	/**
	@Test //[Bug #5 - Medium, 2PTS]
	public void negativeInteger() {
		parser.addOption(new Option("input", Type.INTEGER));
		parser.parse("--input=-1");
		assertEquals(parser.getInteger("input"), -1);
	}**/
	
	/**
	@Test //[Bug #8 - Medium, 2PTS]
	public void sameOptionDifferentShortcut() {
		parser.addOption(new Option("input", Type.INTEGER), "i");
		assertTrue(parser.optionOrShortcutExists("i"));
		parser.addOption(new Option("input", Type.INTEGER), "in");
		assertFalse(parser.optionOrShortcutExists("i"));
		assertTrue(parser.optionOrShortcutExists("in"));
	}**/
	
	/**
	@Test //[Bug #10 - Easy, 1PT]
	public void nullChar() {
		parser.addOption(new Option("input", Type.CHARACTER));
		parser.parse("--input=\0");
		assertEquals(parser.getCharacter("input"), null);
	}*/
	
	/**
	@Test //[Bug #11 - Hard, 3PTS]
	public void optionNaming() {
		parser.addOption(new Option("input%", Type.STRING));
		assertFalse(parser.optionExists("input%"));
	}**/
	
	/**
	@Test //[Bug #12 - Hard, 3PTS]
	public void test() {
		parser.addOption(new Option("input", Type.STRING), "i");
		parser.addOption(new Option("option", Type.STRING), "input");
		parser.parse("--input=something -input=something");
		parser.replace("-input", "something", "nothing");
		assertEquals(parser.getString("--input"), "nothing");
	}**/
	
	/**
	@Test //[Bug #15 - Medium, 2PTS]
	public void largeInteger() {
		parser.addOption(new Option("input", Type.STRING));
		parser.parse("--input=9999999999");
		assertEquals(parser.getInteger("input"), 9999999999L);
		}**/
	
	/**
	@Test //[Bug #17 - Hard, 3PTS]
	public void optionLength() {
		parser.addOption(new Option("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii", Type.STRING));
		assertTrue(parser.optionOrShortcutExists("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"));
	}**/
	
	/**
	@Test //[Bug #20 - Hard, 3PTS]
	public void inputWithSpace() {
		parser.addOption(new Option("input", Type.STRING));
		parser.parse("--input=hello world");
		assertEquals(parser.getString("input"), "hello world");
	}**/
	
	@Test //[Bug #15 - Medium, 2PTS]
	public void newLine() {
		parser.addOption(new Option("input", Type.STRING));
		parser.parse("--input=\\n");
		assertEquals(parser.getInteger("input"), "\\n");
		}
}
