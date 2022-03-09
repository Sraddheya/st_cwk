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
	
	/**SPECIFICATION 1.3.2
	 * Name AND shortcut of options should only contain digits, letters and underscores. 
	 * Digits cannot appear as the first character. A runtime (IllegalArgumentException) exception is thrown otherwise.
	 */

	@Test (expected=IllegalArgumentException.class)
	public void optionNamingChars() {
		//Names
		parser.addOption(new Option("1output", Type.STRING));
		parser.addOption(new Option("*output", Type.STRING));
		//Shortcuts
		parser.addOption(new Option("output", Type.STRING), "1o");
		parser.addOption(new Option("output", Type.STRING), "*o");
	}
	
	/**SPECIFICATION 1.3.3
	 * Option names and shortcuts are case-sensitive.
	 */
	
	@Test
	public void optionNameCases() {
		parser.addOption(new Option("output", Type.STRING));
		assertFalse(parser.optionExists("OutPut"));
		parser.addOption(new Option("OutPut", Type.STRING));
		assertTrue(parser.optionExists("OutPut"));
	}
	@Test
	public void optionShortcutCases() {
		parser.addOption(new Option("output", Type.STRING), "out");
		assertFalse(parser.shortcutExists("Out"));
		parser.setShortcut("output", "Out");
		assertTrue(parser.shortcutExists("Out"));
	}
}
