package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task2_2_FunctionalTest {
	
private Parser parser;
	
	@Before
	public void setUp() {
		parser = new Parser();
	}
	
	//PARSER CLASS------------------------------------------------------------------
	@Test
	public void testOptionOrShortcutExists() {
		parser.addOption(new Option("option", Type.STRING), "opt");
		assertTrue(parser.optionOrShortcutExists("option"));
		assertTrue(parser.optionOrShortcutExists("opt"));
	}
	
	/**
	@Test (expected=Exception e1)
	public void testBigInteger() {
		parser.addOption(new Option("option", Type.STRING));
		parser.parse("--option=9999999999");
		assertEquals(parser.getInteger("option"), 0);
	}**/
	
	@Test
	public void testGetIntegerFromCharacter() {
		parser.addOption(new Option("option", Type.CHARACTER));
		assertEquals(parser.getInteger("option"), 102);
	}
	
	@Test
	public void testGetIntegerFromNoType() {
		parser.addOption(new Option("option", Type.NOTYPE));
		assertEquals(parser.getInteger("option"), 0);
	}
	
	@Test
	public void testGetCharacter() {
		parser.addOption(new Option("option", Type.CHARACTER));
		assertEquals(parser.getInteger("option"), '\0');
	}
	
	@Test
	public void testSetShortcut() {
		parser.addOption(new Option("option", Type.CHARACTER), "o");
		assertTrue(parser.shortcutExists("o"));
		parser.setShortcut("option", "opt");;
		assertTrue(parser.shortcutExists("opt"));
		assertFalse(parser.shortcutExists("o"));
	}
	
	public void testReplace1() {
		parser.addOption(new Option("opt1", Type.STRING));
		parser.addOption(new Option("opt2", Type.STRING));
		parser.parse("--opt1=OldText1 --opt2=OldText2");
		parser.replace("--opt1 --opt2", "Old", "New");
		assertNotEquals(parser.getString("opt1"), "OldText1");
		assertNotEquals(parser.getString("opt2"), "OldText2");
	}
	
	public void testReplace2() {
		parser.addOption(new Option("opt1", Type.STRING), "o1");
		parser.addOption(new Option("opt2", Type.STRING), "o2");
		parser.parse("--opt1=OldText1 --opt2=OldText2");
		parser.replace("-o1 -o2", "Old", "New");
		assertNotEquals(parser.getString("o1"), "OldText1");
		assertNotEquals(parser.getString("o2"), "OldText2");
	}
	
	public void testReplace3() {
		parser.addOption(new Option("opt1", Type.STRING), "o1");
		parser.addOption(new Option("opt2", Type.STRING), "o2");
		parser.parse("--opt1=OldText1 --opt2=OldText2");
		parser.replace("o1 o2", "Old", "New");
		assertNotEquals(parser.getString("o1"), "OldText1");
		assertNotEquals(parser.getString("o2"), "OldText2");
	}
	
	public void testParse1() {
		assertEquals(parser.parse(null), -1);
	}
	
	public void testParse2() {
		assertEquals(parser.parse(""), -2);
	}
	
	/**
	public void testToString() {
		parser.toString()
	}**/
	
	//OPTION CLASS----------------------------------------------------------------
	@Test
	public void testSetName() {
		Option op = new Option("op", Type.STRING);
		assertEquals(op.getName(), "op");
		op.setName("opNew");
		assertEquals(op.getName(), "opNew");
	}
	
	@Test
	public void testToString() {
		Option op = new Option("op", Type.STRING);
		String output = "Option[name:op, value:, type:STRING]";
		assertEquals(op.toString(), output);
	}
	
	//OPTIONMAP-------------------------------------------------------------------
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
