package org.xmlcml.args;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Element;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.xml.XMLUtil;

/** simple option for controlling arguments.
 * 
 * @author pm286
 *
 */
public class ArgumentOption {
	private static final String BRIEF = "brief";
	private static final String LONG = "long";
	private static final String HELP = "help";
	private static final String ARGS = "args";
	private static final String CLASS_TYPE = "class";
	private static final String DEFAULT = "default";
	private static final String MIN = "min";
	private static final String MAX = "max";
	private static final String METHOD_NAME = "methodName";

	private static final Logger LOG = Logger.getLogger(ArgumentOption.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	private String brief;
	private String lng;
	private String help;
	private Class<?> classType;
	private Object defalt;
	private Integer minCount;
	private Integer maxCount;
	private Double minValue = null;
	private Double maxValue = null;
	private Pattern pattern = null;
	private List<String> enums = null;
	
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
	
	private String methodName;
	private Class<? extends DefaultArgProcessor> argProcessorClass;
	
	public ArgumentOption(Class<? extends DefaultArgProcessor> argProcessorClass) {
		this.argProcessorClass = argProcessorClass;
	}
	
	public ArgumentOption(Class<? extends DefaultArgProcessor> argProcessorClass,
			String brief, 
			String lng, 
			String args,
			String help,
			Class<?> classType,
			Object defalt,
			int minCount,
			int maxCount,
			String methodName
			) {
		this(argProcessorClass);
		setBrief(brief);
		setLong(lng);
		setHelp(help);
		setArgs(args);
		setClassType(classType);
		setDefault(defalt);
		setMinCount(minCount);
		setMaxCount(maxCount);
		setMethodName(methodName);
	}

	public static ArgumentOption createOption(Class<? extends DefaultArgProcessor> argProcessor, Element element) {
		ArgumentOption argumentOption = new ArgumentOption(argProcessor);
		argumentOption.addBrief(element);
		argumentOption.addLong(element);
		argumentOption.addHelp(element);
		argumentOption.addArgs(element);
		argumentOption.addClassType(element);
		argumentOption.addDefault(element);
		argumentOption.addMinCount(element);
		argumentOption.addMaxCount(element);
		argumentOption.addMethodName(element);
		return argumentOption;
	}

	
	private void addMethodName(Element element) {
		setMethodName(element.getAttributeValue(METHOD_NAME));
		checkNotNull("methodName", methodName);
	}

	private void checkNotNull(String name, Object argumentValue) {
		if (argumentValue == null) {
			throw new RuntimeException(name+" for "+this+" should not be null");
		}
	}

	private void addMaxCount(Element element) {
		setMaxCount(createInteger(element.getAttributeValue(MAX)));
		checkNotNull("maxCount", maxCount);
	}

	private Integer createInteger(String ss) {
		Integer ii = null;
		if (ss == null) {
		} else if (ss.equals("Integer.MAX_VALUE")) {
			ii = Integer.MAX_VALUE;
		} else if (ss.equals("Integer.MIN_VALUE")) {
			ii = Integer.MIN_VALUE;
		} else {
			try {
				ii = new Integer(ss);
			} catch (Exception e) {
				LOG.error("Bad integer: "+ss);
			}
		}
		LOG.trace("integer "+ii);
		return ii;
	}

	private void addMinCount(Element element) {
		setMinCount(createInteger(element.getAttributeValue(MIN)));
		checkNotNull("minCount", minCount);
	}

	private void addDefault(Element element) {
		setDefault(element.getAttributeValue(DEFAULT));
		checkNotNull("default", defalt);
	}

	private void addClassType(Element element) {
		setClassType(element.getAttributeValue(CLASS_TYPE));
		checkNotNull("classType", classType);
	}

	private void setClassType(String className) {
		if (className != null) {
			try {
				classType = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Cannot create class for: "+className);
			}
		}
	}

	private void addArgs(Element element) {
		setArgs(element.getAttributeValue(ARGS));
		checkNotNull("args", args);
	}

	private void addHelp(Element element) {
		List<Element> helpChildren = XMLUtil.getQueryElements(element, "*[local-name()='help']");
		if (helpChildren.size() != 1) {
			throw new RuntimeException("No help found for: "+this);
		}
		setHelp(helpChildren.get(0).getValue());
		checkNotNull("help", help);
	}

	private void addLong(Element element) {
		setLong(element.getAttributeValue(LONG));
		checkNotNull("long", lng);
	}

	private void addBrief(Element element) {
		setBrief(element.getAttributeValue(BRIEF));
		checkNotNull("brief", brief);
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

	public Class<?> getClassType() {
		return classType;
	}

	public void setClassType(Class<?> classType) {
		this.classType = classType;
	}

	public Object getDefault() {
		return defalt;
	}

	public void setDefault(Object defalt) {
		this.defalt = defalt;
	}

	public Integer getMinCount() {
		return minCount;
	}

	public void setMinCount(Integer minCount) {
		this.minCount = minCount;
	}

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		if (methodName != null) {
			try {
				Method method = argProcessorClass.getMethod(methodName, ArgumentOption.class, ArgIterator.class);
				this.methodName = methodName;
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Non-existent method "+argProcessorClass+"; "+methodName+" - please mail", e);
			}
		}
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
		} else if (inputs.size() > maxCount) {
			LOG.error("Must have at least "+minCount+" args; found: "+inputs.size());
		} else if (classType == null) {
			LOG.error("null type translates to String");
			stringValues = inputs;
		} else if (classType.equals(String.class)) {
			stringValues = inputs;
			stringValue = (inputs.size() == 0) ? defaultString : inputs.get(0);
		} else if (classType.equals(Double.class)) {
			doubleValues = new ArrayList<Double>();
			for (String input : inputs) {
				doubleValues.add(new Double(input));
			}
			doubleValue = (doubleValues.size() == 0) ? defaultDouble : doubleValues.get(0);
		} else if (classType.equals(Boolean.class)) {
			booleanValue = inputs.size() == 1 ? new Boolean(inputs.get(0)) : defaultBoolean;
		} else if (classType.equals(Integer.class)) {
			integerValues = new ArrayList<Integer>();
			for (String input : inputs) {
				integerValues.add(new Integer(input));
			}
			integerValue = (integerValues.size() == 0) ? defaultInteger : integerValues.get(0);
		} else if (classType.equals(StringPair.class)) {
			stringPairValues = new ArrayList<StringPair>();
			for (String input : inputs) {
				String[] fields = input.trim().split(",");
				if (fields.length != 2) {
					throw new RuntimeException("Cannot parse "+input+" as comma-separated pair (foo,bar)");
				}
				stringPairValues.add(new StringPair(fields[0], fields[1]));
			}
			// NYI
//			stringPairValueValue = (stringPairValues.size() == 0) ? defaultStringPairValue : stringPairValues.get(0);
		} else {
			LOG.error("currently cannot support type: "+classType);
		}
		return this;
	}

	public ArgumentOption ensureDefaults() {
		if (classType == null) {
			// no defaults
		} else if (defalt == null) {
			// no defaults
		} else if (classType.equals(String.class)) {
			defaultStrings = new ArrayList<String>();
			defaultStrings.add((String)defalt);
			if (minCount == 1 && maxCount == 1) {
				defaultString = (String)defalt;
			}
		} else if (classType.equals(Integer.class)) {
			Integer defaultInteger = null;
			try {
				defaultInteger = new Integer(String.valueOf(defalt));
			} catch (Exception e) {
				throw new RuntimeException("default should be of type Integer");
			}
			if (minCount != 1 || maxCount != 1) {
				defaultIntegers = new ArrayList<Integer>();
				defaultIntegers.add(defaultInteger);
			}
		} else if (classType.equals(Double.class) && defalt instanceof Double) {
			Double defaultDouble = null;
			try {
				defaultDouble = new Double(String.valueOf(defalt));
			} catch (Exception e) {
				throw new RuntimeException("default should be of type Double");
			}
			if (minCount != 1 || maxCount != 1) {
				defaultDoubles = new ArrayList<Double>();
				defaultDoubles.add(defaultDouble);
			}
		} else if (classType.equals(Boolean.class) && defalt instanceof Boolean) {
			defaultBoolean = false;
			try {
				defaultBoolean = new Boolean(String.valueOf(defalt));
			} catch (Exception e) {
				throw new RuntimeException("default should be of type Boolean");
			}
		} else {
			LOG.error("Incompatible type and default: "+classType+"; "+defalt.getClass());
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

	@Override 
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String maxCountS = (maxCount == Integer.MAX_VALUE) ? "" : String.valueOf(maxCount);
 		sb.append(brief+" or "+lng+"; {"+minCount+","+maxCountS+"}; "+methodName+"\n");
		if (classType.equals(String.class)) {
			sb.append("STRING: "+defaultString+" / "+defaultStrings+"; "+stringValue+"; "+stringValues);
		} else if (classType.equals(Integer.class)) {
			sb.append("INTEGER: "+defaultInteger+" / "+defaultIntegers+"; "+integerValue+"; "+integerValues);
		} else if (classType.equals(Double.class)) {
			sb.append("DOUBLE: "+defaultDouble+" / "+defaultDoubles+"; "+doubleValue+"; "+doubleValues);
		} else if (classType.equals(Boolean.class)) {
			sb.append("BOOLEAN: "+defaultBoolean+"; "+booleanValue);
		} else if (classType.equals(StringPair.class)) {
			sb.append("STRINGPAIRS: ; "+stringPairValues);
		} else if (classType.equals(Object.class)) {
			sb.append("OBJECT ; "+stringValue);
		}
		return sb.toString();
	}

	/** checks argument count; 
	 * 
	 * @param list
	 * @return null indicates correct; non-null is explanatory message.
	 */
	public String checkArgumentCount(List<String> list) {
		String message = null;
		if (minCount == null || maxCount == null) {
			// no checking
		} else if (minCount == maxCount && list.size() != maxCount) {
			message = "require exactly "+minCount+" arguments for "+lng;
		} else if (minCount > maxCount) {
			message = "bad "+lng+" in args.xml file; minCount "+minCount+" should not be greater than maxCount: "+maxCount;
		} else if (minCount > list.size()) {
			message = "Need at least "+minCount+" arguments for "+lng+ "; found "+list.size();
		} else if (maxCount < list.size()) {
			message = "Too many arguments ("+list.size()+") for "+lng+" ; maximum is :"+maxCount;
		}
		return message;
	}

	/** checks argument values; 
	 * 
	 * @param list
	 * @return null indicates correct; non-null is explanatory message.
	 */
	public String checkArgumentValues(List<String> list) {
		String message = null;
		for (String s : list) {
			if (s == null) {
				message = "Cannot have null values in "+lng;
				break;
			}
			message = checkNumericValue(s);
			if (message != null) break;
			message = checkPatternValue(s);
			if (message != null) break;
			message = checkEnumValue(s);
			if (message != null) break;
		}
		return message;
	}

	private String checkPatternValue(String s) {
		String message = null;
		if (pattern != null) {
			Matcher matcher = pattern.matcher(s);
			message = "Argument for "+lng +" ("+s+") does not match "+pattern;
		}
		return message;
	}

	private String checkEnumValue(String s) {
		String message = null;
		if (enums != null) {
			message = "arg ("+s+") in "+lng+" does not match allowed values "+enums;
			for (String enumx : enums) {
				if (enumx.equals(s)) {
					message = null;
					break;
				}
			}
		}
		return message;
	}

	private String checkNumericValue(String s) {
		String message = null;
		Double d = null;
		if (classType != null && classType.isAssignableFrom(Number.class)) {
			try {
				d = new Double(s);
			} catch (NumberFormatException nfe) {
				message = "Not a number: "+nfe+"; in "+lng;
			}
			if (minValue != null && d != null) {
				if (d < minValue) {
					message = "value ("+d+") below minimum for "+lng;
				}
			}
			if (maxValue != null && d != null) {
				if (d > maxValue) {
					message = "value ("+d+") above maximum for "+lng;
				}
			}
		}
		return message;
	}

}
