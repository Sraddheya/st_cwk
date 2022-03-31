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
	
	//TASK 3-------------------------------------------------------------------------------------------------
	
	private Pattern patternForm = Pattern.compile("(([A-Za-z0-9_])+(([A-Z]-[A-Z]+)|[a-z]-[a-z]+|[0-9]-[0-9]+))");
	private Pattern patternNumeric = Pattern.compile("-?\\d+(\\.\\d+)?");
	
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
	
	private boolean isValidForm(String str) {
	    if (str == null) {
	        return false; 
	    }
	    return patternForm.matcher(str).matches();
	}

	private boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false; 
	    }
	    return patternNumeric.matcher(strNum).matches();
	}
	
	public void addAll(String options, String shortcuts, String types) {
		String[] oldOps = options.split("\\s+");
		String[] oldShorts= shortcuts.split("\\s+");
		String[] oldTypes = types.split("\\s+");
		
		List<Option> newOps = getAllOptions(oldOps, oldTypes);
		List<String> newShorts = getAllShorts(oldShorts);
		

		//Storing
		for (int k = 0; k < newOps.size(); k++) {
			
			if (newShorts.size() > k) {
				optionMap.store(newOps.get(k), newShorts.get(k));
				System.out.println(newOps.get(k).getName() + " " + newOps.get(k).getType() + " " + newOps.get(k).getValue() + " " + newShorts.get(k));
			} else {
				optionMap.store(newOps.get(k), "");
				System.out.println(newOps.get(k).getName() + " " + newOps.get(k).getType() + " " + newOps.get(k).getValue() + "NOSHORTCUT");
			}
		}
		
		System.out.println();
	}
	
	public void addAll(String options, String types) {
		String[] oldOps = options.split("\\s+");
		String[] oldTypes = types.split("\\s+");
		
		List<Option> newOps = getAllOptions(oldOps, oldTypes);
		
		//Storing
		for (int k = 0; k < newOps.size(); k++) {
			optionMap.store(newOps.get(k), "");
			System.out.println(newOps.get(k).getName() + " " + newOps.get(k).getType() + " " + newOps.get(k).getValue() + "NOSHORTCUT");
		}
		
		System.out.println();
	}
	
	private List<Option> getAllOptions(String[] oldOps, String[] oldTypes){
		List<Option> newOps = new ArrayList<Option>();
		
		int typeIndex = 0;

		for (int i = 0; i < oldOps.length; ++i) {
			
			if (i < oldTypes.length) {
				typeIndex = i;
			}
			
			Type type = stringToType(oldTypes[typeIndex]);
			
			if (oldOps[i].contains("-")) {
				if (isValidForm(oldOps[i])) {
					for (String op : ungroup(oldOps[i])) {
					    newOps.add(new Option(op, type));
					}
				}
			} else {
				newOps.add(new Option(oldOps[i], type));
				
			}
		}
		
		return newOps;
	}
	
	private List<String> getAllShorts(String[] oldShorts){
		List<String> newShorts = new ArrayList<String>();
		
		for (int i = 0; i < oldShorts.length; i++) {
			
			if (oldShorts[i].contains("-")) {
				if (isValidForm(oldShorts[i])) {
					for (String sc : ungroup(oldShorts[i])) {
					    newShorts.add(sc);
					}
				}
			} else {
				newShorts.add(oldShorts[i]);
				
			}
		}
		
		return newShorts;
	}
	
	private List<String> ungroup(String group) {
		List<String> single = new ArrayList<String>();
		
		String[] arr = group.split("-");
		int nameLen = arr[0].length()-1;
	
		String name = arr[0].substring(0, nameLen);
		String newName;
		
		if (isNumeric(arr[1])) {
			int start = Integer.parseInt(arr[0].substring(nameLen, nameLen+1));
			int end = Integer.parseInt(arr[1]);
			
			for (int i = Math.min(start, end); i<= Math.max(start, end); i++) {
				newName = name + Integer.toString(i);
				single.add(newName);
			}
			
		} else {
			char start = arr[0].charAt(nameLen);
			char end = arr[1].charAt(0);
			
			for (char i = (char) Math.min(start, end); i<= Math.max(start, end); i++) {
				newName = name + (char) i;
				single.add(newName);
			}
			
		}
		
		return single;
	}
	
}
