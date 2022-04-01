package st;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Task2_2_FunctionalTest {
	
private Parser parser;
private OptionMap oMap;
	
	@Before
	public void setUp() {
		parser = new Parser();
		oMap = new OptionMap();
	}
	
	//PARSER CLASS------------------------------------------------------------------
	@Test
	public void testOptionOrShortcutExists() {
		parser.addOption(new Option("option", Type.STRING), "opt");
		assertTrue(parser.optionOrShortcutExists("option"));
		assertTrue(parser.optionOrShortcutExists("opt"));
	}
	
	@Test
	public void testGetIntegerFromCharacter() {
		parser.addOption(new Option("option", Type.CHARACTER));
		parser.parse("--option=a");
		assertEquals(parser.getInteger("option"), 97);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testGetIntegerFromNoType() {
		parser.addOption(new Option("option", Type.NOTYPE));
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
		parser.parse("-o=a");
		assertEquals(parser.getCharacter("option"), 'a');
		parser.setShortcut("option", "opt");;
		assertTrue(parser.shortcutExists("opt"));
		assertTrue(parser.shortcutExists("o"));
		parser.parse("-o=b");
		parser.parse("-opt=c");
		assertEquals(parser.getCharacter("option"), 'c');
	}
	
	@Test
	public void testReplace1() {
		parser.addOption(new Option("opt1", Type.STRING), "o1");
		parser.addOption(new Option("opt2", Type.STRING), "o2");
		parser.parse("--opt1=OldText1 --opt2=OldText2");
		parser.replace("--opt1 --opt2", "Old", "New");
		assertNotEquals(parser.getString("opt1"), "OldText1");
		assertNotEquals(parser.getString("opt2"), "OldText2");
	}
	
	@Test
	public void testReplace2() {
		parser.addOption(new Option("opt1", Type.STRING), "o1");
		parser.addOption(new Option("opt2", Type.STRING), "o2");
		parser.parse("--opt1=OldText1 --opt2=OldText2");
		parser.replace("-o1 -o2", "Old", "New");
		assertNotEquals(parser.getString("o1"), "OldText1");
		assertNotEquals(parser.getString("o2"), "OldText2");
	}
	
	@Test
	public void testReplace3() {
		parser.addOption(new Option("opt1", Type.STRING), "o1");
		parser.addOption(new Option("opt2", Type.STRING), "o2");
		parser.parse("--opt1=OldText1 --opt2=OldText2");
		parser.replace("o1 o2", "Old", "New");
		assertNotEquals(parser.getString("o1"), "OldText1");
		assertNotEquals(parser.getString("o2"), "OldText2");
	}
	
	@Test
	public void testParse1() {
		assertEquals(parser.parse(null), -1);
	}

	@Test
	public void testParse2() {
		assertEquals(parser.parse(""), -2);
	}
	
	@Test
	public void testToStringParser() {
		parser.addOption(new Option("opt1", Type.STRING), "o1");
		String output = "Options Map: \n{opt1=Option[name:opt1, value:, type:STRING]}\nShortcuts Map:\n{o1=Option[name:opt1, value:, type:STRING]}";
		assertEquals(parser.toString(), output);
	}
	
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
	@Test (expected=RuntimeException.class)
	public void testGetShortcut() {
		oMap.getShortcut("option");
	}
	
	@Test (expected=RuntimeException.class)
	public void testGetOption1() {
		oMap.getOption("option");
	}
	
	@Test
	public void testGetOption2() {
		Option option = new Option("op1", Type.STRING);
		oMap.store(option, "");
		assertEquals(oMap.getOptionByNameOrShortcut("--op1"), option);
	}
	
	@Test
	public void testGetOption3() {
		Option option = new Option("option", Type.STRING);
		oMap.store(option, "op1");
		assertEquals(oMap.getOptionByNameOrShortcut("-op1"), option);
	}
	
	@Test
	public void testGetOption4() {
		Option option = new Option("op1", Type.STRING);
		oMap.store(option, "");
		assertEquals(oMap.getOptionByNameOrShortcut("op1"), option);
	}
	
	@Test (expected=RuntimeException.class)
	public void testGetOption5() {
		oMap.getOptionByNameOrShortcut("op1");
	}
	
	@Test
	public void testToStringOptionMap() {
		Option option = new Option("opt1", Type.STRING);
		oMap.store(option, "o1");
		String output = "Options Map: \n{opt1=Option[name:opt1, value:, type:STRING]}\nShortcuts Map:\n{o1=Option[name:opt1, value:, type:STRING]}";
		assertEquals(oMap.toString(), output);
	}
	
}
