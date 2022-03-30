package st;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private OptionMap optionMap;
	
	public Parser() {
		optionMap = new OptionMap();
	}
	
	public void addOption(Option option, String shortcut) {
		optionMap.store(option, shortcut);
	}
	
	public void addOption(Option option) {
		optionMap.store(option, "");
	}
	
	public boolean optionExists(String key) {
		return optionMap.optionExists(key);
	}
	
	public boolean shortcutExists(String key) {
		return optionMap.shortcutExists(key);
	}
	
	public boolean optionOrShortcutExists(String key) {
		return optionMap.optionOrShortcutExists(key);
	}
	
	public int getInteger(String optionName) {
		String value = getString(optionName);
		Type type = getType(optionName);
		int result;
		switch (type) {
			case STRING:
			case INTEGER:
				try {
					result = Integer.parseInt(value);
				} catch (Exception e) {
			        try {
			            new BigInteger(value);
			        } catch (Exception e1) {
			        }
			        result = 0;
			    }
				break;
			case BOOLEAN:
				result = getBoolean(optionName) ? 1 : 0;
				break;
			case CHARACTER:
				result = (int) getCharacter(optionName);
				break;
			default:
				result = 0;
		}
		return result;
	}
	
	public boolean getBoolean(String optionName) {
		String value = getString(optionName);
		return !(value.toLowerCase().equals("false") || value.equals("0") || value.equals(""));
	}
	
	public String getString(String optionName) {
		return optionMap.getValue(optionName);
	}
	
	public char getCharacter(String optionName) {
		String value = getString(optionName);
		return value.equals("") ? '\0' :  value.charAt(0);
	}
	
	public void setShortcut(String optionName, String shortcutName) {
		optionMap.setShortcut(optionName, shortcutName);
	}
	
	public void replace(String variables, String pattern, String value) {
			
		variables = variables.replaceAll("\\s+", " ");
		
		String[] varsArray = variables.split(" ");
		
		for (int i = 0; i < varsArray.length; ++i) {
			String varName = varsArray[i];
			String var = (getString(varName));
			var = var.replace(pattern, value);
			if(varName.startsWith("--")) {
				String varNameNoDash = varName.substring(2);
				if (optionMap.optionExists(varNameNoDash)) {
					optionMap.setValueWithOptionName(varNameNoDash, var);
				}
			} else if (varName.startsWith("-")) {
				String varNameNoDash = varName.substring(1);
				if (optionMap.shortcutExists(varNameNoDash)) {
					optionMap.setValueWithOptionShortcut(varNameNoDash, var);
				} 
			} else {
				if (optionMap.optionExists(varName)) {
					optionMap.setValueWithOptionName(varName, var);
				}
				if (optionMap.shortcutExists(varName)) {
					optionMap.setValueWithOptionShortcut(varName, var);
				} 
			}

		}
	}
	
	private List<CustomPair> findMatches(String text, String regex) {
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(text);
	    // Check all occurrences
	    List<CustomPair> pairs = new ArrayList<CustomPair>();
	    while (matcher.find()) {
	    	CustomPair pair = new CustomPair(matcher.start(), matcher.end());
	    	pairs.add(pair);
	    }
	    return pairs;
	}
	
	
	public int parse(String commandLineOptions) {
		if (commandLineOptions == null) {
			return -1;
		}
		int length = commandLineOptions.length();
		if (length == 0) {
			return -2;
		}	
		
		List<CustomPair> singleQuotePairs = findMatches(commandLineOptions, "(?<=\')(.*?)(?=\')");
		List<CustomPair> doubleQuote = findMatches(commandLineOptions, "(?<=\")(.*?)(?=\")");
		List<CustomPair> assignPairs = findMatches(commandLineOptions, "(?<=\\=)(.*?)(?=[\\s]|$)");
		
		
		for (CustomPair pair : singleQuotePairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
	    	
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);
		}
		
		for (CustomPair pair : doubleQuote) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\'", "{S_QUOTE}").
					  replaceAll(" ", "{SPACE}").
					  replaceAll("-", "{DASH}").
					  replaceAll("=", "{EQUALS}");
			
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}
		
		for (CustomPair pair : assignPairs) {
			String cmd = commandLineOptions.substring(pair.getX(), pair.getY());
			cmd = cmd.replaceAll("\"", "{D_QUOTE}").
					  replaceAll("\'", "{S_QUOTE}").
					  replaceAll("-", "{DASH}");
	    	commandLineOptions = commandLineOptions.replace(commandLineOptions.substring(pair.getX(),pair.getY()), cmd);	
		}

		commandLineOptions = commandLineOptions.replaceAll("--", "-+").replaceAll("\\s+", " ");


		String[] elements = commandLineOptions.split("-");
		
		
		for (int i = 0; i < elements.length; ++i) {
			String entry = elements[i];
			
			if(entry.isBlank()) {
				continue;
			}

			String[] entrySplit = entry.split("[\\s=]", 2);
			
			boolean isKeyOption = entry.startsWith("+");
			String key = entrySplit[0];
			key = isKeyOption ? key.substring(1) : key;
			String value = "";
			
			if(entrySplit.length > 1 && !entrySplit[1].isBlank()) {
				String valueWithNoise = entrySplit[1].trim();
				value = valueWithNoise.split(" ")[0];
			}
			
			// Explicitly convert boolean.
			if (getType(key) == Type.BOOLEAN && (value.toLowerCase().equals("false") || value.equals("0"))) {
				value = "";
			}
			
			value = value.replace("{S_QUOTE}", "\'").
						  replace("{D_QUOTE}", "\"").
						  replace("{SPACE}", " ").
						  replace("{DASH}", "-").
						  replace("{EQUALS}", "=");
			
			
			boolean isUnescapedValueInQuotes = (value.startsWith("\'") && value.endsWith("\'")) ||
					(value.startsWith("\"") && value.endsWith("\""));
			
			value = value.length() > 1 && isUnescapedValueInQuotes ? value.substring(1, value.length() - 1) : value;
			
			if(isKeyOption) {
				optionMap.setValueWithOptionName(key, value);
			} else {
				optionMap.setValueWithOptionShortcut(key, value);
				
			}			
		}

		return 0;
		
	}

	
	private Type getType(String option) {
		Type type = optionMap.getType(option);
		return type;
	}
	
	@Override
	public String toString() {
		return optionMap.toString();
	}

	
	private class CustomPair {
		
		CustomPair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
	    private int x;
	    private int y;
	    
	    public int getX() {
	    	return this.x;
	    }
	    
	    public int getY() {
	    	return this.y;
	    }
	}
	
	//HELPERS-------------------------------------------------------------------------------------------------
	
	private Pattern pattern = Pattern.compile("(([A-Za-z0-9_])+(([A-Z]-[A-Z]+)|[a-z]-[a-z]+|[0-9]-[0-9]+))");
	
	private Type stringToType(String type) {
		if (type.equals("String")){
			return Type.STRING;
		} else if (type.equals("Integer")) {
			return Type.INTEGER;
		} else if (type.equals("Boolean")) {
			return Type.BOOLEAN;
		} else if (type.equals("Character")) {
			return Type.CHARACTER;
		} else {
			return Type.NOTYPE;
		}
	}
	
	public boolean isValidForm(String str) {
	    if (str == null) {
	        return false; 
	    }
	    return pattern.matcher(str).matches();
	}

	
	//BASIC ADDALL-------------------------------------------------------------------------------------------
	/**
	public void addAll(String options, String shortcuts, String types) {
		String[] optionsList = options.split("\\s+");
		String[] shortcutsList = shortcuts.split("\\s+");
		String[] typesList = types.split("\\s+");
		
		addAllHelper(optionsList, shortcutsList, typesList);
	}
	
	public void addAll(String options, String types) {
		String[] optionsList = options.split("\\s+");
		String[] shortcutsList = new String[0];
		String[] typesList = types.split("\\s+");
		
		addAllHelper(optionsList, shortcutsList, typesList);
	}
	
	private void addAllHelper(String[] options, String[] shortcuts, String[] types) {
		int j;
		
		for (int i = 0; i < options.length; ++i) {
			if (types.length - 1 < i) {
				j = types.length - 1;
			} else {
				j = i;
			}
			
			Type type = stringToType(types[j]);
			Option op = new Option(options[i], type);
			
			if (shortcuts.length - 1 < i) {
				optionMap.store(op, "");
			} else {
				optionMap.store(op, shortcuts[i]);
			}
		}
	}
	
	private List<Option> ungroupInt(String name, int start, int end, String typeStr){
		List<Option> newOps = new ArrayList<Option>();
		Type type = stringToType(typeStr);
		String newName;
		
		for (int i = start; i<= end; i++) {
			newName = name + (char) i;
			Option op = new Option(newName, type);
			newOps.add(op);
		}
		
		return newOps;
	}
	
	private boolean isSameRange(String startStr, String endStr) {
		
		if (endStr.length()!=1) {
			return false;
		}
		
		char start = startStr.charAt(0);
		char end = endStr.charAt(0);
		
		if (Character.isDigit(start) && Character.isDigit(end)) {
			return true;
		} else if (Character.isUpperCase(start) && Character.isUpperCase(end) && endStr.length()==1) {
			return true;
		} else if (Character.isLowerCase(start) && Character.isLowerCase(end) && endStr.length()==1) {
			return true;
		}
		return false;
	}
	**/
	
	public void addAll(String options, String shortcuts, String types) {
		String[] oldOps = options.split("\\s+");
		String[] oldShorts= shortcuts.split("\\s+");
		String[] oldTypes = types.split("\\s+");
		
		List<Option> newOps = getAllOptions(oldOps, oldTypes);
		
		for (int i = 0; i < newOps.size(); i++) {
			System.out.println(newOps.get(i).getName() + " " + newOps.get(i).getType() + " " + newOps.get(i).getValue());
		}
		
		List<String> newShorts = getAllShorts(oldShorts);
		
		for (int i = 0; i < newShorts.size(); i++) {
			System.out.println(newShorts.get(i));
		}

		//Storing
		for (int k = 0; k < newOps.size(); k++) {
			
			if (newShorts.size() > k) {
				System.out.println(newOps.get(k).getName() + " " + newOps.get(k).getType() + " " + newOps.get(k).getValue() + " " + newShorts.get(k));
				optionMap.store(newOps.get(k), newShorts.get(k));
			} else {
				System.out.println(newOps.get(k).getName() + " " + newOps.get(k).getType() + " " + newOps.get(k).getValue() + "NOSHORTCUT");
				optionMap.store(newOps.get(k), "");
			}
		}
		
		System.out.println();
	}
	
	public void addAll(String options, String types) {
		String[] oldOps = options.split("\\s+");
		String[] oldTypes = types.split("\\s+");
		
		List<Option> newOps = getAllOptions(oldOps, oldTypes);
		
		for (int i = 0; i < newOps.size(); i++) {
			System.out.println(newOps.get(i).getName() + " " + newOps.get(i).getType() + " " + newOps.get(i).getValue());
		}

		//Storing
		for (int k = 0; k < newOps.size(); k++) {
			optionMap.store(newOps.get(k), "");
			System.out.println(newOps.get(k).getName() + " " + newOps.get(k).getType() + " " + newOps.get(k).getValue());
		}
		
		System.out.println();
	}
	
	private List<Option> getAllOptions(String[] oldOps, String[] oldTypes){
		List<Option> newOps = new ArrayList<Option>();
		
		int typeIndex;

		for (int i = 0; i < oldOps.length; ++i) {
			
			if (oldTypes.length - 1 < i) {
				typeIndex = oldTypes.length - 1;
			} else {
				typeIndex = i;
			}
			
			Type type = stringToType(oldTypes[typeIndex]);
			
			if (oldOps[i].contains("-")) {
				newOps.addAll(ungroupOption(oldOps[i], type));
			} else {
				newOps.add(new Option(oldOps[i], type));
			}
		}
		
		return newOps;
	}
	
	private List<String> getAllShorts(String[] oldShorts){
		List<String> newShorts = new ArrayList<String>();
		
		for (int j = 0; j < oldShorts.length; j++) {
			if (oldShorts[j].contains("-")) {
				newShorts.addAll(ungroupShort(oldShorts[j]));
			} else {
				newShorts.add(oldShorts[j]);
			}
		}
		
		return newShorts;
	}
	
	private List<Option> ungroupOption(String options, Type type) {
		List<Option> newOps = new ArrayList<Option>();
		
		String[] arr = options.split("-");
		int nameLen = arr[0].length()-1;
	
		String name = arr[0].substring(0, nameLen);
		String startStr = arr[0].substring(nameLen, nameLen+1);
		String endStr = arr[1];
		
		if (isValidForm(options)) {
			char start = startStr.charAt(0);
			char end = endStr.charAt(0);

			String newName;
			
			for (int i = start; i<= end; i++) {
				newName = name + (char) i;
				Option op = new Option(newName, type);
				newOps.add(op);
			}
		}
		
		return newOps;
	}
	
	private List<String> ungroupShort(String shortcuts) {
		List<String> newShorts = new ArrayList<String>();
		
		String[] arr = shortcuts.split("-");
		int nameLen = arr[0].length()-1;
	
		String name = arr[0].substring(0, nameLen);
		String startStr = arr[0].substring(nameLen, nameLen+1);
		String endStr = arr[1];
		
		if (isValidForm(shortcuts)) {
			char start = startStr.charAt(0);
			char end = endStr.charAt(0);

			String newName;
			
			for (int i = start; i<= end; i++) {
				newName = name + (char) i;
				newShorts.add(newName);
			}
		}
		
		return newShorts;
	}

	
	
}
