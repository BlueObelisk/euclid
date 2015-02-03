package org.xmlcml.args;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Builder;
import nu.xom.Element;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.files.QuickscrapeDirectory;
import org.xmlcml.xml.XMLUtil;

public class DefaultArgProcessor {

	
	private static final Logger LOG = Logger.getLogger(DefaultArgProcessor.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public static final String MINUS = "-";
	public static final String[] DEFAULT_EXTENSIONS = {"html", "xml"};
	public final static String H = "-h";
	public final static String HELP = "--help";
	private static Pattern INTEGER_RANGE = Pattern.compile("(.*)\\{(\\d+),(\\d+)\\}(.*)");

	private static String RESOURCE_NAME_TOP = "/org/xmlcml/args";
	private static String ARGS_RESOURCE = RESOURCE_NAME_TOP+"/"+"args.xml";
	
	private static final Pattern INTEGER_RANGE_PATTERN = Pattern.compile("(\\d+):(\\d+)");
	public static Pattern GENERAL_PATTERN = Pattern.compile("\\{([^\\}]*)\\}");
	
	protected String output;
	protected List<String> extensionList = Arrays.asList(DEFAULT_EXTENSIONS);
	private boolean recursive = false;
	protected List<String> inputList;
	public List<ArgumentOption> argumentOptionList;
	public List<ArgumentOption> chosenArgumentOptionList;
	protected ArrayList<QuickscrapeDirectory> quickscrapeDirectoryList;
	
	protected List<ArgumentOption> getArgumentOptionList() {
		return argumentOptionList;
	}

	public DefaultArgProcessor() {
		readArgumentOptions(ARGS_RESOURCE);
	}
	
	public void readArgumentOptions(String resourceName) {
		ensureArgumentOptionList();
		try {
			InputStream is = this.getClass().getResourceAsStream(resourceName);
			if (is == null) {
				throw new RuntimeException("Cannot read: "+resourceName);
			}
			Element argElement = new Builder().build(is).getRootElement();
			List<Element> elementList = XMLUtil.getQueryElements(argElement, "/*/*[local-name()='arg']");
			for (Element element : elementList) {
				ArgumentOption argOption = ArgumentOption.createOption(this.getClass(), element);
				argumentOptionList.add(argOption);
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot read args file "+resourceName, e);
		}
	}
	
	private void ensureArgumentOptionList() {
		if (this.argumentOptionList == null) {
			this.argumentOptionList = new ArrayList<ArgumentOption>();
		}
	}

	public void expandWildcardsExhaustively() {
		while (expandWildcardsOnce());
	}
	
	public boolean expandWildcardsOnce() {
		boolean change = false;
		ensureInputList();
		List<String> newInputList = new ArrayList<String>();
		for (String input : inputList) {
			List<String> expanded = expandWildcardsOnce(input);
			newInputList.addAll(expanded);
			change |= (expanded.size() > 1 || !expanded.get(0).equals(input));
		}
		inputList = newInputList;
		return change;
	}


	/** expand expressions/wildcards in input.
	 * 
	 * @param input
	 * @return
	 */
	private List<String> expandWildcardsOnce(String input) {
		Matcher matcher = GENERAL_PATTERN.matcher(input);
		List<String> inputs = new ArrayList<String>(); 
		if (matcher.find()) {
			String content = matcher.group(1);
			String pre = input.substring(0, matcher.start());
			String post = input.substring(matcher.end());
			inputs = expandIntegerMatch(content, pre, post);
			if (inputs.size() == 0) {
				inputs = expandStrings(content, pre, post);
			} 
			if (inputs.size() == 0) {
				LOG.error("Cannot expand "+content);
			}
		} else {
			inputs.add(input);
		}
		return inputs;
	}

	private List<String> expandIntegerMatch(String content, String pre, String post) {
		List<String> stringList = new ArrayList<String>();
		Matcher matcher = INTEGER_RANGE_PATTERN.matcher(content);
		if (matcher.find()) {
			int start = Integer.parseInt(matcher.group(1));
			int end = Integer.parseInt(matcher.group(2));
			for (int i = start; i <= end; i++) {
				String s = pre + i + post;
				stringList.add(s);
			}
		}
		return stringList;
	}

	private List<String> expandStrings(String content, String pre, String post) {
		List<String> newStringList = new ArrayList<String>();
		List<String> vars = Arrays.asList(content.split("\\|"));
		for (String var : vars) {
			newStringList.add(pre + var + post);
		}
		
		return newStringList;
	}

	protected void checkHasNext(ArgIterator argIterator) {
		if (!argIterator.hasNext()) {
			throw new RuntimeException("ran off end; expected more arguments");
		}
	}

	private void checkCanAssign(Object obj, Method method) {
	    if (!method.getDeclaringClass().isAssignableFrom(obj.getClass())) {
	        throw new IllegalArgumentException(
	            "Cannot call method '" + method + "' of class '" + method.getDeclaringClass().getName()
	            + "' using object '" + obj + "' of class '" + obj.getClass().getName() + "' because"
	            + " object '" + obj + "' is not an instance of '" + method.getDeclaringClass().getName() + "'");
	    }
	}

	// ============ METHODS ===============

	public void parseExtensions(ArgumentOption option, ArgIterator argIterator) {
		setExtensions(argIterator.createTokenListUpToNextMinus());
	}

	public void parseQuickscrapeDirectory(ArgumentOption option, ArgIterator argIterator) {
		quickscrapeDirectoryList = new ArrayList<QuickscrapeDirectory>();
		List<String> qDirectoryNames = argIterator.createTokenListUpToNextMinus();
		for (String qDirectoryName : qDirectoryNames) {
			QuickscrapeDirectory quickscrapeDiectory = new QuickscrapeDirectory(qDirectoryName);
			LOG.debug("FC "+qDirectoryName);
			quickscrapeDirectoryList.add(quickscrapeDiectory);
		}
	}

	public void printHelp(ArgumentOption divOption, ArgIterator argIterator) {
		printHelp();
	}

	public void parseInput(ArgumentOption divOption, ArgIterator argIterator) {
		List<String> inputs = argIterator.createTokenListUpToNextMinus();
		if (inputs.size() == 0) {
			inputList = new ArrayList<String>();
			LOG.error("Must give at least one input");
		} else {
			inputList = inputs;
			LOG.debug("Inputs: "+inputList);
			if (inputList.size() == 1) {
				inputList = expandWildcards(inputs.get(0));
			}
		}
	}
	

	public void parseOutput(ArgumentOption divOption, ArgIterator argIterator) {
		checkHasNext(argIterator);
		output = argIterator.next();
	}

	public void parseRecursive(ArgumentOption divOption, ArgIterator argIterator) {
		recursive = true;
	}

	// =====================================
	
	/** expand expressions/wildcards in input.
	 * 
	 * @param input
	 * @return
	 */
	private List<String> expandWildcards(String input) {
		Matcher matcher = INTEGER_RANGE.matcher(input);
		List<String> inputs = new ArrayList<String>();
		if (matcher.matches()) {
			int start = Integer.parseInt(matcher.group(2));
			int end = Integer.parseInt(matcher.group(3));
			if (start <= end) {
				for (int i = start; i <= end; i++) {
					String input0 = matcher.group(1)+i+matcher.group(4);
					inputs.add(input0);
				}
			}
		} else {
			inputs.add(input);
		}
		LOG.trace("inputs: "+inputs);
		return inputs;
	}

	// =====================================
	public void setExtensions(List<String> extensions) {
		this.extensionList = extensions;
	}


	public List<String> getInputList() {
		ensureInputList();
		return inputList;
	}

	private void ensureInputList() {
		if (inputList == null) {
			inputList = new ArrayList<String>();
		}
	}

	public String getOutput() {
		return output;
	}

	public boolean isRecursive() {
		return recursive;
	}

	public List<QuickscrapeDirectory> getQuickscrapeDirectoryList() {
		ensureQuickscrapeDirectoryList();
		return quickscrapeDirectoryList;
	}

	private void ensureQuickscrapeDirectoryList() {
		if (quickscrapeDirectoryList == null) {
			quickscrapeDirectoryList = new ArrayList<QuickscrapeDirectory>();
		}
	}
	

	// --------------------------------
	
	public boolean parseArgs(String[] commandLineArgs) {
		ArgIterator argIterator = new ArgIterator(commandLineArgs);
		boolean processed = false;
		while (argIterator.hasNext()) {
			String arg = argIterator.next();
			try {
				processed = runReflectedMethod(this.getClass(), argumentOptionList, argIterator, arg);
			} catch (Exception e) {
				throw new RuntimeException("cannot process argument: "+arg, e);
			}
		}
		return processed;
	}
	
	public List<String> getExtensions() {
		return extensionList;
	}


	protected boolean runReflectedMethod(Class<?> thisClass, List<ArgumentOption> optionList, ArgIterator argIterator, String arg) throws Exception {
		ensureChosenArgumentList();
		boolean processed = false;
		if (!arg.startsWith(MINUS)) {
			LOG.error("Parsing failed at: ("+arg+"), expected \"-\" trying to recover");
		} else {
			for (ArgumentOption option : optionList) {
				Method method = null;
				if (option.matches(arg)) {
					try {
						String methodName = option.getMethodName();
						if (methodName == null) {
							throw new RuntimeException("arg: "+arg+" MUST have a methodName");
						}
						method = this.getClass().getMethod(methodName, option.getClass(), argIterator.getClass());
					} catch (NoSuchMethodException nsme) {
						LOG.debug("methods for "+this.getClass());
						for (Method meth : thisClass.getDeclaredMethods()) {
							LOG.debug(meth);
						}
						throw new RuntimeException(option.getMethodName()+"; "+this.getClass()+"; "+option.getClass()+"; \nContact Norma developers: ", nsme);
					}
					LOG.debug("Using method: "+method);
					method.setAccessible(true);
					method.invoke(this, option, argIterator);
					processed = true;
					chosenArgumentOptionList.add(option);
					break;
				}
			}
			if (!processed) {
				LOG.error("Unknown arg: ("+arg+"), trying to recover");
			}
		}
		return processed;
	}

	private void ensureChosenArgumentList() {
		if (chosenArgumentOptionList == null) {
			chosenArgumentOptionList = new ArrayList<ArgumentOption>();
		}
	}

	protected void printHelp() {
		for (ArgumentOption option : argumentOptionList) {
			System.err.println(option.getHelp());
		}
	}
	
	public List<ArgumentOption> getChosenArgumentList() {
		ensureChosenArgumentList();
		return chosenArgumentOptionList;
	}
	
	public String createDebugString() {
		StringBuilder sb = new StringBuilder();
		getChosenArgumentList();
		for (ArgumentOption argumentOption : chosenArgumentOptionList) {
			sb.append(argumentOption.toString()+"\n");
		}
		return sb.toString();
	}

	protected void expandDefaults() {
		LOG.error("Defaults not yet implemented; run explicit args");
	}

}
