package org.xmlcml.args;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/** simple option for controlling arguments.
 * 
 * @author pm286
 *
 */
public class ArgumentOption {

	
	private static final Logger LOG = Logger.getLogger(ArgumentOption.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	
	private String brief;
	private String lng;
	private String help;
	private Class<?> type;
	private Object defalt;
	private int minCount;
	private int maxCount;
	
	private List<String> defaultStrings;
	private List<Integer> defaultIntegers;
	private List<Double> defaultDoubles;

	private List<String> stringValues;
	private List<Double> doubleValues;
	private List<Integer> integerValues;

	private Double defaultDouble;
	private String defaultString;
	private Integer defaultInteger;
	private Boolean defaultBoolean;
	
	private String  stringValue;
	private Integer integerValue;
	private Double  doubleValue;
	private Boolean booleanValue;
	private String args;
	private List<StringPair> stringPairValues;
	
	public ArgumentOption() {
	}
	
	public ArgumentOption(
			String brief, 
			String lng, 
			String args,
			String help,
			Class<?> type,
			Object defalt,
			int minCount,
			int maxCount
			) {
		setBrief(brief);
		setLong(lng);
		setHelp(help);
		setArgs(args);
		setType(type);
		setDefault(defalt);
		setMinCount(minCount);
		setMaxCount(maxCount);
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getLong() {
		return lng;
	}

	public void setLong(String lng) {
		this.lng = lng;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getHelp() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n"+brief);
		sb.append(" or "+lng+" ");
	    if (args.trim().length() > 0) {
	    	sb.append(" "+args);
	    }
		sb.append("\n");
		sb.append(help);
		return sb.toString();
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Object getDefault() {
		return defalt;
	}

	public void setDefault(Object defalt) {
		this.defalt = defalt;
	}

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public ArgumentOption processArgs(List<String> inputs) {
		ensureDefaults();
		stringValue = defaultString;
		stringValues = defaultStrings;
		doubleValue = defaultDouble;
		doubleValues = defaultDoubles;
		doubleValue = defaultDouble;
		doubleValues = defaultDoubles;
		if (inputs.size() < minCount) {
			LOG.error("Must have at least "+minCount+" args; found: "+inputs.size());
		} else if (inputs.size() > minCount) {
			LOG.error("Must have at least "+minCount+" args; found: "+inputs.size());
		} else if (type == null) {
			LOG.error("null type translates to String");
			stringValues = inputs;
		} else if (type.equals(String.class)) {
			stringValues = inputs;
		} else if (type.equals(Double.class)) {
			doubleValues = new ArrayList<Double>();
			for (String input : inputs) {
				doubleValues.add(new Double(input));
			}
		} else if (type.equals(Boolean.class)) {
			booleanValue = inputs.size() == 1 ? new Boolean(inputs.get(0)) : defaultBoolean;
		} else if (type.equals(Integer.class)) {
			integerValues = new ArrayList<Integer>();
			for (String input : inputs) {
				integerValues.add(new Integer(input));
			}
		} else if (type.equals(StringPair.class)) {
			stringPairValues = new ArrayList<StringPair>();
			for (String input : inputs) {
				String[] fields = input.trim().split(",");
				if (fields.length != 2) {
					throw new RuntimeException("Cannot parse "+input+" as comma-separated pair (foo,bar)");
				}
				stringPairValues.add(new StringPair(fields[0], fields[1]));
			}
		} else {
			LOG.error("currently cannot support type: "+type);
		}
		return this;
	}

	public ArgumentOption ensureDefaults() {
		if (type == null) {
			// no defaults
		} else if (defalt == null) {
			// no defaults
		} else if (type.equals(String.class) && defalt instanceof String) {
			defaultStrings = new ArrayList<String>();
			defaultStrings.add((String)defalt);
			if (minCount == 1 && maxCount == 1) {
				defaultString = (String)defalt;
			}
		} else if (type.equals(Integer.class) && defalt instanceof Integer) {
			Integer defaultInteger = null;
			try {
				defaultInteger = (Integer)defalt;
			} catch (Exception e) {
				throw new RuntimeException("default should be of type Integer");
			}
			if (minCount != 1 || maxCount != 1) {
				defaultIntegers = new ArrayList<Integer>();
				defaultIntegers.add(defaultInteger);
			}
		} else if (type.equals(Double.class) && defalt instanceof Double) {
			Double defaultDouble = null;
			try {
				defaultDouble = (Double)defalt;
			} catch (Exception e) {
				throw new RuntimeException("default should be of type Double");
			}
			if (minCount != 1 || maxCount != 1) {
				defaultDoubles = new ArrayList<Double>();
				defaultDoubles.add(defaultDouble);
			}
		} else if (type.equals(Boolean.class) && defalt instanceof Boolean) {
			defaultBoolean = false;
			try {
				defaultBoolean = (Boolean) defalt;
			} catch (Exception e) {
				throw new RuntimeException("default should be of type Boolean");
			}
		} else {
			LOG.error("Incompatible type and default: "+type+"; "+defalt.getClass());
		}
		return this;
	}

	public String getDefaultString() {
		return defaultString;
	}
	
	public Integer getDefaultInteger() {
		return defaultInteger;
	}
	
	public Double getDefaultDouble() {
		return defaultDouble;
	}
	
	public Boolean getDefaultBoolean() {
		return defaultBoolean;
	}
	
	public List<String> getDefaultStrings() {
		return defaultStrings;
	}
	
	public List<Integer> getDefaultIntegers() {
		return defaultIntegers;
	}
	
	public List<Double> getDefaultDoubles() {
		return defaultDoubles;
	}

	public boolean matches(String arg) {
		return (brief.equals(arg) || lng.equals(arg));
	}
	
	public List<Double> getDoubleValues() {
		return doubleValues;
	}

	public List<Integer> getIntegerValues() {
		return integerValues;
	}

	public List<String> getStringValues() {
		return stringValues;
	}
	
	public String getStringValue() {
		return stringValue;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public List<StringPair> getStringPairValues() {
		return stringPairValues;
	}

	
}