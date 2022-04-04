package st;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task3_Parser {

	private OptionMap optionMap;
	private List<String> oldOps = new ArrayList<String>();
	private List<String> oldShorts = new ArrayList<String>();
	private List<String> oldTypes = new ArrayList<String>();
	
	public Task3_Parser() {
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
	//private Pattern patternForm = Pattern.compile("(([A-Za-z0-9_])+(([A-Z]-[A-Z]+)|[a-z]-[a-z]+|[0-9]-[0-9]+))");
	private Pattern patternGroup = Pattern.compile("(([A-Za-z0-9_])+(([A-Z]-[A-Z])|[a-z]-[a-z]|[0-9]-[0-9]+))");
	private Pattern patternSingle = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
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
	
	private boolean isValidGroup(String str) {
	    if (str == null) {
	        return false; 
	    }
	    return patternGroup.matcher(str).matches();
	}
	
	private boolean isValidSingle(String str) {
	    if (str == null) {
	        return false; 
	    }
	    return patternSingle.matcher(str).matches();
	}

	private boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false; 
	    }
	    return patternNumeric.matcher(strNum).matches();
	}
	
	public void addAll(String options, String shortcuts, String types) {
		if (options==null || options.isBlank() || options.isEmpty() 
			|| shortcuts==null
			|| types==null || types.isBlank() || types.isEmpty()) {
    		throw new IllegalArgumentException("Illegal argument provided in method. Null and empty values cannot be passed.");
    	} 

		oldOps.addAll(Arrays.asList(options.split("\\s+")));
		oldShorts.addAll(Arrays.asList(shortcuts.split("\\s+")));
		oldTypes.addAll(Arrays.asList(types.split("\\s+")));
		
		List<Option> newOps = getAllOptions();
		List<String> newShorts = getAllShorts();

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

	}
	
	public void addAll(String options, String types) {
		if (options==null || options.isBlank() || options.isEmpty() 
			|| types==null || types.isBlank() || types.isEmpty()) {
    		throw new IllegalArgumentException("Illegal argument provided in method. Null values cannot be passed.");
    	} 

		oldOps = Arrays.asList(options.split("\\s+"));
		oldTypes = Arrays.asList(types.split("\\s+"));
		
		List<Option> newOps = getAllOptions();
		
		//Storing
		for (int k = 0; k < newOps.size(); k++) {
			optionMap.store(newOps.get(k), "");
			System.out.println(newOps.get(k).getName() + " " + newOps.get(k).getType() + " " + newOps.get(k).getValue() + "NOSHORTCUT");
		}
	}
	
	private List<Option> getAllOptions(){
		List<Option> newOps = new ArrayList<Option>();
		
		int typeIndex = 0;
		int lenOps = oldOps.size();
		int i = 0;
		
		while (i < lenOps) {
			System.out.println(i);
			
			if (i < oldTypes.size()) {
				typeIndex = i;
			}
			
			Type type = stringToType(oldTypes.get(typeIndex));
			
			if (oldOps.get(i).contains("-")) {
				if (isValidGroup(oldOps.get(i))) {
					for (String op : ungroup(oldOps.get(i))) {
					    newOps.add(new Option(op, type));
					    i++;
					}
				} else {
					if (i < oldShorts.size() && oldShorts!=null) {
						System.out.println(oldShorts.get(i));
						oldShorts.remove(i);
					}
					if (i < typeIndex) {
						oldTypes.remove(i);
					}
					lenOps--;
				}
			} else {
				if (isValidSingle(oldOps.get(i))) {
					newOps.add(new Option(oldOps.get(i), type));
					i++;
				} else {
					if (i < oldShorts.size() && oldShorts!=null) {
						oldShorts.remove(i);
					}
					if (i < typeIndex) {
						oldTypes.remove(i);
					}
					lenOps--;
				}
			}
			
		}
		
		/**
		for (int i = 0; i < oldOps.size(); ++i) {
			
			if (i < oldTypes.size()) {
				typeIndex = i;
			}
			
			Type type = stringToType(oldTypes.get(typeIndex));
			
			if (oldOps.get(i).contains("-")) {
				if (isValidGroup(oldOps.get(i))) {
					for (String op : ungroup(oldOps.get(i))) {
					    newOps.add(new Option(op, type));
					}
				} else {
					oldShorts.remove(i);
					oldTypes.remove(i);
				}
			} else {
				if (isValidSingle(oldOps.get(i))) {
					newOps.add(new Option(oldOps.get(i), type));
				} else {
					oldShorts.remove(i);
					oldTypes.remove(i);
				}
				
			}
		}**/
		
		return newOps;
	}
	
	private List<String> getAllShorts(){
		List<String> newShorts = new ArrayList<String>();
		
		for (int i = 0; i < oldShorts.size(); i++) {
			
			if (oldShorts.get(i).contains("-")) {
				if (isValidGroup(oldShorts.get(i))) {
					for (String sc : ungroup(oldShorts.get(i))) {
					    newShorts.add(sc);
					}
				}
			} else {
				if (isValidSingle(oldShorts.get(i))) {
					newShorts.add(oldShorts.get(i));
				}
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
	
	private int ungroupInvalid(String group) {
		int len = 0;
		
		String[] arr = group.split("-");
		int nameLen = arr[0].length()-1;
		
		if (isNumeric(arr[1])) {
			int start = Integer.parseInt(arr[0].substring(nameLen, nameLen+1));
			int end = Integer.parseInt(arr[1]);
			
			len = Math.max(start, end) - Math.min(start, end);
			
		} else {
			char start = arr[0].charAt(nameLen);
			char end = arr[1].charAt(0);
			
			len = Math.max(start, end) - Math.min(start, end);
			
		}
		
		return len;
	}
	
}
