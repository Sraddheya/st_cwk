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
	
	//[Bug #1 - Easy, 1PT]----------------------------------------------------------------------
	@Test
	public void testNullShortcut() {
		parser.addOption(new Option("option", Type.STRING), "");
		assertFalse(parser.shortcutExists(""));
	}
	
	//[Bug #2 - Easy, 1PT]--------------------------------------------------------------------
	@Test 
	public void testBooleanFalse1() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=False");
		assertFalse(parser.getBoolean("option"));
		parser.parse("--option=false");
		assertFalse(parser.getBoolean("option"));
	}
	
	@Test 
	public void testBooleanFalse2() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=0");
		assertFalse(parser.getBoolean("option"));
	}
	
	@Test
	public void testBooleanFalse3() {
		parser.addOption(new Option("option", Type.BOOLEAN), "o");
		parser.parse("--option=");
		assertFalse(parser.getBoolean("option"));
		parser.parse("-o=");
		assertFalse(parser.getBoolean("option"));
	}

	//[Bug #3 - Medium, 2PTS] ----------------------------------------------------------------------
	@Test
	public void testGetInteger1() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=something");
		assertEquals(parser.getInteger("option"), 1);
	}
	
	@Test
	public void testGetInteger2() {
		parser.addOption(new Option("option", Type.BOOLEAN));
		parser.parse("--option=false");
		assertEquals(parser.getInteger("option"), 0);
	}
	
	//[Bug #4 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test
	public void testShortcutLength() {
		parser.addOption(new Option("option", Type.STRING), "oooooooooooooooooooooooooooooooooooooooooooooooooooo");
		assertTrue(parser.shortcutExists("oooooooooooooooooooooooooooooooooooooooooooooooooooo"));
	}
	
	//[Bug #5 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test //(expected=ArithmeticException.class)
	public void testNegativeInteger() {
		parser.addOption(new Option("option", Type.INTEGER));
		parser.parse("--option=-1");
		assertEquals(parser.getInteger("option"), -1);
	}
	
	//[Bug #6 - Easy, 1PT]-----------------------------------------------------------------------
	@Test 
	public void testEqualOptions() {
		Option opt1 = new Option("opt1", Type.STRING);
		Option opt2 = new Option("opt2", Type.STRING);
		assertFalse(opt1.equals(opt2));
	}
		
	//Bug #7 - Hard, 3PTS]-------------------------------------------------------------------------
	@Test 
	public void testAllNumericValues() {
		parser.addOption(new Option("option", Type.STRING));
		parser.parse("--option=1234567890");
		assertEquals(parser.getInteger("option"), 1234567890);
	}
	
	//[Bug #8 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test
	public void testNewShortcut() {
		parser.addOption(new Option("option", Type.INTEGER), "o");
		parser.addOption(new Option("option", Type.INTEGER), "oo");
		assertTrue(parser.shortcutExists("o"));
		assertTrue(parser.shortcutExists("oo"));
	}
	
	//[Bug #9 - Easy, 1PT]--------------------------------------------------------------------------
	@Test
	public void testBlankSpaceParse() {
		assertEquals(parser.parse(" "), 0);
	}
	
	//[Bug #10 - Easy, 1PT]-------------------------------------------------------------------------
	@Test
	public void testNoChar() {
		parser.addOption(new Option("option", Type.CHARACTER));
		assertEquals(parser.getCharacter("option"), '\0');
	}
	
	//[Bug #11 - Hard, 3PTS]------------------------------------------------------------------------
	@Test (expected=IllegalArgumentException.class)
	public void testSpecialCharInOption() {
		parser.addOption(new Option("option#", Type.STRING));
	}
	
	//[Bug #12 - Hard, 3PTS]-----------------------------------------------------------------------
	@Test
	public void testShortcutInReplace() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("-o=old");
		parser.replace("-o", "old", "new");
		assertEquals(parser.getString("option"), "new");
	}
	
	//[Bug #13 - Medium, 2PTS]------------------------------------------------------------------------
	@Test
	public void testQuotesEquals() {
		parser.addOption(new Option("option", Type.STRING));
		parser.parse("--option='value=\"abc\"'");
		assertEquals(parser.getString("option"), "value=\"abc\"");
	}
	
	//[Bug #14 -  Hard, 3PTS]------------------------------------------------------------------------
	@Test
	public void testNewLineChar() {
		parser.addOption(new Option("option", Type.STRING));
		parser.parse("--option=\\n");
		assertEquals(parser.getString("option"), "\\n");
	}
	
	//[Bug #15 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test
	public void testLargeInteger() {
		parser.addOption(new Option("option", Type.STRING));
		parser.parse("--option=9999999999");
		assertEquals(parser.getInteger("option"), 0);
	}
	
	//[Bug #16 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test (expected=NullPointerException.class)
	public void testNullParse() {
		parser.getString(null);
	}
	
	//[Bug #17 - Hard, 3PTS]-------------------------------------------------------------------------
	@Test
	public void testOptionLength() {
		parser.addOption(new Option("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo", Type.STRING));
		assertTrue(parser.optionExists("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo"));
	}
	
	@Test //[Bug #18 - Easy, 1PT]--------------------------------------------------------------------
	public void testSpaceInReplace() {
		parser.addOption(new Option("opt1", Type.STRING));
		parser.addOption(new Option("opt2", Type.STRING));
		parser.parse("--opt1=OldText1 --opt2=OldText2");
		parser.replace("opt1     opt2", "Old", "New");
		assertNotEquals(parser.getString("opt1"), "OldText1");
		assertNotEquals(parser.getString("opt2"), "OldText2");
	}
	
	//[Bug #19 - Medium, 2PTS]-----------------------------------------------------------------------
	@Test
	public void testDoubleQuoteDash() {
		parser.addOption(new Option("option", Type.STRING), "o");
		parser.parse("--option=\"hello-o=world\"");
		assertEquals(parser.getString("option"), "hello-o=world");
	}
	
	//[Bug #20 - Hard, 3PTS]-------------------------------------------------------------------------
	@Test
	public void testValueWithSpace() {
		parser.addOption(new Option("option", Type.STRING));
		parser.parse("--option=hello world");
		assertEquals(parser.getString("option"), "hello");
	}
	
	//TASK 2-------------------------------------------------------------------------------------------------------------------
	
	private OptionMap oMap;
	
	/**
	@Before
	public void setUpOption() {
		oMap = new OptionMap();
	}**/
	
	@Test (expected=RuntimeException.class)
	public void testGetOption() {
		oMap = new OptionMap();
		oMap.getOption("option");
	}
	
	@Test (expected=RuntimeException.class)
	public void testGetShortcut() {
		oMap = new OptionMap();
		oMap.getShortcut("option");
	}
	
	@Test
	public void testGetOption1() {
		Option option = new Option("--op1", Type.STRING);
		oMap = new OptionMap();
		oMap.store(option, "");
		assertEquals(oMap.getOptionByNameOrShortcut("--op1"), option);
	}
	
	@Test
	public void testGetOption2() {
		Option option = new Option("-op1", Type.STRING);
		oMap = new OptionMap();
		oMap.store(option, "");
		assertEquals(oMap.getOptionByNameOrShortcut("-op1"), option);
	}
	
	@Test
	public void testGetOption3() {
		oMap = new OptionMap();
		Option option = new Option("op1", Type.STRING);
		oMap.store(option, "");
		assertEquals(oMap.getOptionByNameOrShortcut("op1"), option);
	}
	
	@Test (expected=RuntimeException.class)
	public void testGetOption4() {
		oMap = new OptionMap();
		oMap.getOptionByNameOrShortcut("op1");
	}
	
	@Test
	public void testToStringOptionMap() {
		oMap = new OptionMap();
		Option option = new Option("op1", Type.STRING);
		oMap.store(option, "o1");
		String output = "Options Map: \nop1\nShortcuts Map:\no1";
		assertEquals(oMap.toString(), output);
	}
	
}
