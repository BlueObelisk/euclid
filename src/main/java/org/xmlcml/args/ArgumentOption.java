package org.xmlcml.args;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.euclid.IntRange;
import org.xmlcml.euclid.RealRange;
import org.xmlcml.xml.XMLUtil;

/** simple option for controlling arguments.
 * 
 * @author pm286
 *
 */
public class ArgumentOption {
	private static final String BRIEF = "brief";
	private static final String LONG = "long";
	private static final String NAME = "name";
	private static final String HELP = "help";
	private static final String ARGS = "args";
	private static final String CLASS_TYPE = "class";
	private static final String DEFAULT = "default";
	private static final String FORBIDDEN = "forbidden";
	private static final String REQUIRED = "required";
	private static final String COUNT_RANGE = "countRange";
	private static final String VALUE_RANGE = "valueRange";
	private static final String PARSE_METHOD = "parseMethod";
	private static final String RUN_METHOD = "runMethod";
	private static final String OUTPUT_METHOD = "outputMethod";
	private static final String PATTERN = "pattern";
	
	private static final Pattern INT_RANGE = Pattern.compile("\\{(\\d+),(\\d*|\\*)\\}");
	private static final Pattern DOUBLE_RANGE = Pattern.compile("\\{(\\-?\\+?\\d+(\\.\\d*)?),(\\-?\\+?\\d+(\\.\\d*)?)\\}");

	private static final Logger LOG = Logger.getLogger(ArgumentOption.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	private static Set<String> MANDATORY_ATTRIBUTES;
	static {
		MANDATORY_ATTRIBUTES = new HashSet<String>();
		MANDATORY_ATTRIBUTES.add(NAME);
		MANDATORY_ATTRIBUTES.add(LONG);
		MANDATORY_ATTRIBUTES.add(ARGS);
		MANDATORY_ATTRIBUTES.add(PARSE_METHOD);
	}
	private static Set<String> MANDATORY_CHILDREN;
	static {
		MANDATORY_CHILDREN = new HashSet<String>();
		MANDATORY_CHILDREN.add(HELP);
	}
	private static Map<String, String> OPTIONAL_ATTRIBUTES;
	static {
		OPTIONAL_ATTRIBUTES = new HashMap<String, String>();
		OPTIONAL_ATTRIBUTES.put(CLASS_TYPE, "java.lang.String"); // class defaults to String
		OPTIONAL_ATTRIBUTES.put(DEFAULT, "");                    // default defaults to ""
	}
	
	private String name;
	private String brief;
	private String verbose;
	private String help;
	private Class<?> classType;
	private Object defalt;
	private IntRange countRange;
	private String countRangeString;
	private RealRange valueRange = null;
	private String valueRangeString;
	private String patternString = null;
	private Pattern pattern = null;
	private String forbiddenString = null;
	private List<String> forbiddenArguments = null;
	private String requiredString = null;
	private List<String> requiredArguments = null;
	
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
	
	private String parseMethodName;
	private String runMethodName;
	private String outputMethodName;
	
	private Class<? extends DefaultArgProcessor> argProcessorClass;
	
	public ArgumentOption(Class<? extends DefaultArgProcessor> argProcessorClass) {
		this.argProcessorClass = argProcessorClass;
	}
	
	/** factory method option for ArgumentOptions
	 * 
	 * @param argProcessor
	 * @param element
	 * @return
	 */
	public static ArgumentOption createOption(Class<? extends DefaultArgProcessor> argProcessor, Element element) {
		
		ArgumentOption argumentOption = new ArgumentOption(argProcessor);
		Set<String> mandatorySet = new HashSet<String>(MANDATORY_ATTRIBUTES);
		Map<String, String> optionalAttributes = new HashMap<String, String>(OPTIONAL_ATTRIBUTES);
		for (int i = 0; i < element.getAttributeCount(); i++) {
			Attribute attribute = element.getAttribute(i);
			String name = attribute.getLocalName();
			String value = attribute.getValue();
			argumentOption.setValue(name, value);
			mandatorySet.remove(name);
			optionalAttributes.put(name, null);
		}
		LOG.trace("B/D "+argumentOption.brief+"/"+argumentOption.defalt+" // "+argumentOption);
		if (mandatorySet.size() > 0) {
			throw new RuntimeException("The following attributes for "+argumentOption.name+" are mandatory: "+mandatorySet);
		}
		// setDefaults;
		for (String name : optionalAttributes.keySet()) {
			String value = optionalAttributes.get(name);
			if (value != null) {
				argumentOption.setValue(name, value);
			}
		}
		// help
		List<Element> helpNodes = XMLUtil.getQueryElements(element, "./*[local-name()='"+HELP+"']");
		if (helpNodes.size() != 1) {
			throw new RuntimeException("No help given for "+argumentOption.name);
		} else {
			argumentOption.setHelp(helpNodes.get(0).getValue());
		}
		return argumentOption;
	}

	private void setValue(String namex, String value) {
		if (BRIEF.equals(namex)) {
			this.setBrief(value);
		} else if (LONG.equals(namex)) {
			this.setLong(value);
		} else if (NAME.equals(namex)) {
			this.setName(value);
		} else if (HELP.equals(namex)) {
			this.setHelp(value);
		} else if (ARGS.equals(namex)) {
			this.setArgs(value);
		} else if (CLASS_TYPE.equals(namex)) {
			this.setClassType(value);
		} else if (DEFAULT.equals(namex)) {
			this.setDefault(value);
		} else if (COUNT_RANGE.equals(namex)) {
			this.setCountRange(value);
		} else if (FORBIDDEN.equals(namex)) {
			this.setForbiddenString(value);
		} else if (REQUIRED.equals(namex)) {
			this.setRequiredString(value);
		} else if (OUTPUT_METHOD.equals(namex)) {
			this.setOutputMethod(value);
		} else if (PARSE_METHOD.equals(namex)) {
			this.setParseMethod(value);
		} else if (PATTERN.equals(namex)) {
			this.setPatternString(value);
		} else if (RUN_METHOD.equals(namex)) {
			this.setRunMethod(value);
		} else if (VALUE_RANGE.equals(namex)) {
			this.setValueRange(value);
		} else {
			throw new RuntimeException("Unknown attribute on <arg name='"+name+"'>: "+namex+"='"+value+"'");
		}
	}

	

	private void setCountRange(String value) {
		countRangeString = value;
		setCountRange(createIntRange(countRangeString));
	}

	private void setCountRange(IntRange intRange) {
		this.countRange = intRange;
	}

	private void setValueRange(String value) {
		valueRangeString = value;
		setValueRange(createDoubleRange(valueRangeString));
	}

	private void setValueRange(RealRange realRange) {
		this.valueRange = realRange;
	}

	private RealRange createDoubleRange(String valueRangeString2) {
		return RealRange.createRange(valueRangeString);
	}

	private IntRange createIntRange(String ss) {
		IntRange intRange = null;
		Matcher matcher = INT_RANGE.matcher(ss);
		if (matcher.matches()) {
			String min = matcher.group(1);
			int minInt = new Integer(min);
			String max = matcher.group(2);
			int maxInt = (max.equals("*")) ? Integer.MAX_VALUE : new Integer(max);
			if (minInt > maxInt) {
				throw new RuntimeException("Minimum must be less-than/equals: "+ss);
			}
			intRange = new IntRange(minInt, maxInt);
		} else {
			throw new RuntimeException("count range must be of form {min,max}; was "+ss);
		}
		return intRange;
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

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVerbose() {
		return verbose;
	}

	public void setLong(String verbose) {
		this.verbose = verbose;
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
		sb.append(" or "+verbose+" ");
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

	public String getParseMethod() {
		return parseMethodName;
	}

	public void setParseMethod(String parseMethodName) {
		if (parseMethodName != null) {
			try {
				Method method = argProcessorClass.getMethod(parseMethodName, ArgumentOption.class, ArgIterator.class);
				this.parseMethodName = parseMethodName;
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Non-existent method "+argProcessorClass+"; "+parseMethodName+" (edit ArgProcessor)", e);
			}
		}
	}

	public String getRunMethodName() {
		return runMethodName;
	}

	public void setRunMethod(String runMethodName) {
		if (runMethodName != null) {
			try {
				Method method = argProcessorClass.getMethod(runMethodName, ArgumentOption.class);
				LOG.trace("RUN METHOD "+method);
				this.runMethodName = runMethodName;
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Non-existent method "+argProcessorClass+"; "+runMethodName+" (edit ArgProcessor)", e);
			}
		}
	}

	public String getOutputMethodName() {
		return outputMethodName;
	}

	public void setOutputMethod(String outputMethodName) {
		if (outputMethodName != null) {
			try {
				Method method = argProcessorClass.getMethod(outputMethodName, ArgumentOption.class);
				LOG.trace("OUTPUT METHOD "+method);
				this.outputMethodName = outputMethodName;
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("Non-existent method "+argProcessorClass+"; "+outputMethodName+" (edit ArgProcessor)", e);
			}
		}
	}

	public String getPatternString() {
		return patternString;
	}

	public void setPatternString(String patternString) {
		if (patternString == null) {
			LOG.error("null pattern");
		} else {
			Pattern pattern = Pattern.compile(patternString);
		}
	}
	
	private String getForbiddenString() {
		return forbiddenString;
	}

	public String getRequiredString() {
		return requiredString;
	}

	public void setRequiredString(String required) {
		this.requiredString = required;
	}

	public void setForbiddenString(String forbidden) {
		this.forbiddenString = forbidden;
	}


	public ArgumentOption processArgs(List<String> inputs) {
		ensureDefaults();
		stringValue = defaultString;
		stringValues = defaultStrings;
		doubleValue = defaultDouble;
		doubleValues = defaultDoubles;
		doubleValue = defaultDouble;
		doubleValues = defaultDoubles;
		if (!countRange.includes(inputs.size())) {
			throw new RuntimeException("Bad number of arguments: "+inputs.size()+" incompatible with "+countRangeString);
		}
		if (classType == null) {
			classType = String.class;
		}
		if (classType.equals(String.class)) {
			stringValues = inputs;
			stringValue = (inputs.size() == 0) ? defaultString : inputs.get(0);
		} else if (classType.equals(Double.class)) {
			doubleValues = new ArrayList<Double>();
			for (String input : inputs) {
				doubleValues.add(new Double(input));
			}
			doubleValue = (doubleValues.size() == 0) ? defaultDouble : doubleValues.get(0);
			for (Number number : doubleValues) {
				if (!valueRange.includes((double)number)) {
					throw new RuntimeException("bad numeric value: "+number+" should conform to "+valueRangeString);
				}
			}
		} else if (classType.equals(Boolean.class)) {
			booleanValue = inputs.size() == 1 ? new Boolean(inputs.get(0)) : defaultBoolean;
		} else if (classType.equals(Integer.class)) {
			integerValues = new ArrayList<Integer>();
			for (String input : inputs) {
				integerValues.add(new Integer(input));
			}
			integerValue = (integerValues.size() == 0) ? defaultInteger : integerValues.get(0);
			for (Number number : integerValues) {
				if (!valueRange.includes((double)number)) {
					throw new RuntimeException("bad numeric value: "+number+" should conform to "+valueRangeString);
				}
			}
		} else if (classType.equals(StringPair.class)) {
			checkStringPairs(inputs);
		} else {
			LOG.error("currently cannot support type: "+classType);
		}
		return this;
	}

	private void checkStringPairs(List<String> inputs) {
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
	}

	public ArgumentOption ensureDefaults() {
		if (classType == null) {
			// no defaults
		} else if (defalt == null) {
			// no defaults
		} else if (classType.equals(String.class)) {
			defaultStrings = new ArrayList<String>();
			defaultStrings.add((String)defalt);
			if (countRange.isEqualTo(new IntRange(1,1))) {
				defaultString = (String)defalt;
			}
		} else if (classType.equals(Integer.class)) {
			Integer defaultInteger = null;
			try {
				defaultInteger = new Integer(String.valueOf(defalt));
			} catch (Exception e) {
				throw new RuntimeException("default should be of type Integer");
			}
			// FIXME no defaults
		} else if (classType.equals(Double.class) && defalt instanceof Double) {
			Double defaultDouble = null;
			try {
				defaultDouble = new Double(String.valueOf(defalt));
			} catch (Exception e) {
				throw new RuntimeException("default should be of type Double");
			}
			// FIXME no defaults
		} else if (classType.equals(Boolean.class) && defalt instanceof String) {
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
		return (brief.equals(arg) || verbose.equals(arg));
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
 		sb.append(brief+" or "+verbose+"; "+countRange+"; "+parseMethodName+"; ");
		if (classType == null) {
			sb.append("NULL CLASS: "+defaultString+" / "+defaultStrings+"; "+stringValue+"; "+stringValues);
		} else if (classType.equals(String.class)) {
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
		if (countRange != null) {
			if (!countRange.includes(list.size())) {
				message = "argument count ("+list.size()+") is not compatible with "+countRangeString;
			}
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
				message = "Cannot have null values in "+verbose;
				break;
			}
			message = checkBooleanValue(s);
			if (message != null) break;
			message = checkNumericValue(s);
			if (message != null) break;
			message = checkPatternValue(s);
			if (message != null) break;
		}
		return message;
	}

	private String checkBooleanValue(String s) {
		String message = null;
		if (classType != null && classType.isAssignableFrom(Boolean.class)) {
			try {
				new Boolean(s);
			} catch (Exception e) {
				message = "Argument for "+verbose +" ("+s+") should be true or false";
			}
		}
		return message;
	}

	private String checkPatternValue(String s) {
		String message = null;
		if (pattern != null) {
			Matcher matcher = pattern.matcher(s);
			message = "Argument for "+verbose +" ("+s+") does not match "+pattern;
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
				message = "Not a number: "+nfe+"; in "+verbose;
			}
			if (!valueRange.includes(d)) {
				message = "value: "+d+" incompatible with: "+valueRange;
			}
		}
		return message;
	}

	public void processDependencies(List<ArgumentOption> argumentOptionList) {
		processForbidden(argumentOptionList);
		processRequired(argumentOptionList);
	}

	private void processRequired(List<ArgumentOption> argumentOptionList) {
		this.getRequiredArguments();
		if (requiredArguments != null) {
			for (String requiredArgument : requiredArguments) {
				LOG.trace(this.getVerbose()+" REQUIRED "+requiredArgument);
				if (!argumentOccursInOptions(requiredArgument, argumentOptionList)) {
					throw new RuntimeException("Cannot find required option: "+requiredArgument);
				}
			}
		}
	}

	private static boolean argumentOccursInOptions(String requiredArgument, List<ArgumentOption> argumentOptionList) {
		for (ArgumentOption argumentOption : argumentOptionList) {
			String optionVerbose = argumentOption.getVerbose();
			if (requiredArgument.equals(optionVerbose)) {
				return true;
			}
		}
		return false;
	}

	private void processForbidden(List<ArgumentOption> argumentOptionList) {
		for (ArgumentOption argumentOption : argumentOptionList) {
			if (isForbidden(argumentOption)) {
				throw new RuntimeException("Must not have both "+this.verbose+" and "+argumentOption.getVerbose());
			}
		}
	}

	private boolean isForbidden(ArgumentOption argumentOption) {
		String argument = argumentOption.getVerbose();
		if (argument != null) {
			List<String> forbiddenArguments = this.getForbiddenArguments();
			for (String forbiddenArgument : forbiddenArguments) {
				if (argument.equals(forbiddenArgument)) {
					return true;
				}
			}
		}
		return false;
	}

	private List<String> getForbiddenArguments() {
		forbiddenArguments = getWhitespaceSeparatedArguments(this.getForbiddenString());
		return forbiddenArguments;
	}

	private List<String> getRequiredArguments() {
		requiredArguments = getWhitespaceSeparatedArguments(this.getRequiredString());
		return requiredArguments;
	}

	private static List<String> getWhitespaceSeparatedArguments(String strings) {
		return (strings == null) ? new ArrayList<String>() :
			new ArrayList<String>(Arrays.asList(strings.split("\\s+")));
	}

}
