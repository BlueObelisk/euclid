package org.xmlcml.args;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nu.xom.Builder;
import nu.xom.Element;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.files.QuickscrapeNorma;
import org.xmlcml.files.QuickscrapeNormaList;
import org.xmlcml.xml.XMLUtil;

public class DefaultArgProcessor {

	
	private static final Logger LOG = Logger.getLogger(DefaultArgProcessor.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public static final String MINUS = "-";
	public static final String[] DEFAULT_EXTENSIONS = {"html", "xml", "pdf"};
	public final static String H = "-h";
	public final static String HELP = "--help";
	private static Pattern INTEGER_RANGE = Pattern.compile("(.*)\\{(\\d+),(\\d+)\\}(.*)");

	private static String RESOURCE_NAME_TOP = "/org/xmlcml/args";
	private static String ARGS_RESOURCE = RESOURCE_NAME_TOP+"/"+"args.xml";
	
	private static final Pattern INTEGER_RANGE_PATTERN = Pattern.compile("(\\d+):(\\d+)");
	public static Pattern GENERAL_PATTERN = Pattern.compile("\\{([^\\}]*)\\}");
	
	/** creates a list of tokens that are found in an allowed list.
	 * 
	 * @param allowed
	 * @param tokens
	 * @return list of allowed tokens
	 */
	protected static List<String> getChosenList(List<String> allowed, List<String> tokens) {
		List<String> chosenTokens = new ArrayList<String>();
		for (String method : tokens) {
			if (allowed.contains(method)) {
				chosenTokens.add(method);
			} else {
				LOG.error("Unknown token: "+method);
			}
		}
		return chosenTokens;
	}

	protected String output;
	protected List<String> extensionList = null;
	private boolean recursive = false;
	protected List<String> inputList;
	public List<ArgumentOption> argumentOptionList;
	public List<ArgumentOption> chosenArgumentOptionList;
	protected QuickscrapeNormaList quickscrapeNormaList;
	protected QuickscrapeNorma currentQuickscrapeNorma;
	
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
				throw new RuntimeException("Cannot read/find input resource stream: "+resourceName);
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
		List<String> extensions = argIterator.createTokenListUpToNextMinus(option);
		if (extensions.size() != 1) {
			throw new RuntimeException("Currently requires exactly one extension, found: "+extensions);
		}
		setExtensions(extensions);
	}


	public void parseQuickscrapeNorma(ArgumentOption option, ArgIterator argIterator) {
		quickscrapeNormaList = new QuickscrapeNormaList();
		List<String> qDirectoryNames = argIterator.createTokenListUpToNextMinus(option);
		for (String qDirectoryName : qDirectoryNames) {
			QuickscrapeNorma quickscrapeNorma = new QuickscrapeNorma(qDirectoryName);
			quickscrapeNormaList.add(quickscrapeNorma);
		}
	}

	public void printHelp(ArgumentOption divOption, ArgIterator argIterator) {
		printHelp();
	}

	public void parseInput(ArgumentOption option, ArgIterator argIterator) {
		List<String> inputs = argIterator.createTokenListUpToNextMinus(option);
		if (inputs.size() == 0) {
			inputList = new ArrayList<String>();
			LOG.error("Must give at least one input");
		} else {
			inputList = new ArrayList<String>();
			for (String input : inputs) {
				inputList.addAll(expandWildcards(input));
			}
		}
	}
	

	public void parseOutput(ArgumentOption divOption, ArgIterator argIterator) {
		checkHasNext(argIterator);
		output = argIterator.next();
	}

	public void parseRecursive(ArgumentOption divOption, ArgIterator argIterator) {
		List<String> booleans = argIterator.createTokenListUpToNextMinus(divOption);
		recursive = /*booleans.size() == 0 ? true : */ new Boolean(booleans.get(0));
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

	public String getSingleInput() {
		ensureInputList();
		return (inputList.size() != 1) ? null : inputList.get(0);
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

	public QuickscrapeNormaList getQuickscrapeNormaList() {
		ensureQuickscrapeNormaList();
		return quickscrapeNormaList;
	}

	protected void ensureQuickscrapeNormaList() {
		if (quickscrapeNormaList == null) {
			quickscrapeNormaList = new QuickscrapeNormaList();
		}
	}
	

	// --------------------------------
	
	public void parseArgs(String[] commandLineArgs) {
		if (commandLineArgs == null || commandLineArgs.length == 0) {
			printHelp();
		} else {
			String[] totalArgs = addDefaultsAndParsedArgs(commandLineArgs);
			ArgIterator argIterator = new ArgIterator(totalArgs);
			LOG.debug("args with defaults is: "+new ArrayList<String>(Arrays.asList(totalArgs)));
			while (argIterator.hasNext()) {
				String arg = argIterator.next();
				LOG.trace("arg> "+arg);
				try {
					runParseMethod(this.getClass(), argumentOptionList, argIterator, arg);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("cannot process argument: "+arg+" ("+ExceptionUtils.getRootCauseMessage(e)+")");
				}
			}
			finalizeArgs();
		}
	}

	private void finalizeArgs() {
		processArgumentDependencies();
		finalizeInputList();
	}

	private void processArgumentDependencies() {
		for (ArgumentOption argumentOption : chosenArgumentOptionList) {
			argumentOption.processDependencies(chosenArgumentOptionList);
		}
	}

	private void finalizeInputList() {
		List<String> inputList0 = new ArrayList<String>();
		ensureInputList();
		for (String input : inputList) {
			File file = new File(input);
			if (file.isDirectory()) {
				addDirectoryFiles(inputList0, file);
			} else {
				inputList0.add(input);
			}
		}
		inputList = inputList0;
	}

	private void addDirectoryFiles(List<String> inputList0, File file) {
		String[] extensions = getExtensions().toArray(new String[0]);
		List<File> files = new ArrayList<File>(
				FileUtils.listFiles(file, extensions, recursive));
		for (File file0 : files) {
			inputList0.add(file0.toString());
		}
	}

	private String[] addDefaultsAndParsedArgs(String[] commandLineArgs) {
		String[] defaultArgs = createDefaultArgumentStrings();
		List<String> totalArgList = new ArrayList<String>(Arrays.asList(createDefaultArgumentStrings()));
		List<String> commandArgList = Arrays.asList(commandLineArgs);
		totalArgList.addAll(commandArgList);
		String[] totalArgs = totalArgList.toArray(new String[0]);
		return totalArgs;
	}

	private String[] createDefaultArgumentStrings() {
		StringBuilder sb = new StringBuilder();
		for (ArgumentOption option : argumentOptionList) {
			String defalt = String.valueOf(option.getDefault());
//			LOG.debug(option+" // "+defalt);
			if (defalt != null && defalt.toString().trim().length() > 0) {
				sb.append(option.getBrief()+" "+option.getDefault()+" ");
			}
		}
		String s = sb.toString().trim();
		return s.length() == 0 ? new String[0] : s.split("\\s+");
	}

	public List<String> getExtensions() {
		ensureExtensionList();
		return extensionList;
	}

	private void ensureExtensionList() {
		if (extensionList == null) {
			extensionList = new ArrayList<String>();
		}
	}
	
	public void runRunMethodsOnChosenArgOptions() {
		for (ArgumentOption option : chosenArgumentOptionList) {
			String runMethodName = option.getRunMethodName();
			if (runMethodName != null) {
				try {
					runRunMethod(option);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("cannot process argument: "+option.getVerbose()+" ("+ExceptionUtils.getRootCauseMessage(e)+")");
				}
			}
		}
	}
	
	public void runOutputMethodsOnChosenArgOptions() {
		for (ArgumentOption option : chosenArgumentOptionList) {
			String outputMethodName = option.getOutputMethodName();
			if (outputMethodName != null) {
				try {
					runOutputMethod(option);
				} catch (Exception e) {
					throw new RuntimeException("cannot process argument: "+option.getVerbose()+" ("+ExceptionUtils.getRootCauseMessage(e)+")");
				}
			}
		}
	}



	protected void runParseMethod(Class<?> thisClass, List<ArgumentOption> optionList, ArgIterator argIterator, String arg) throws Exception {
		ensureChosenArgumentList();
		boolean processed = false;
		if (!arg.startsWith(MINUS)) {
			LOG.error("Parsing failed at: ("+arg+"), expected \"-\" trying to recover");
		} else {
			for (ArgumentOption option : optionList) {
				Method parseMethod = null;
				if (option.matches(arg)) {
					try {
						String parseMethodName = option.getParseMethod();
						if (parseMethodName == null) {
							throw new RuntimeException("arg: "+arg+" MUST have a parseMethod");
						}
						parseMethod = this.getClass().getMethod(parseMethodName, option.getClass(), argIterator.getClass());
					} catch (NoSuchMethodException nsme) {
						LOG.debug("methods for "+this.getClass());
						for (Method meth : thisClass.getDeclaredMethods()) {
							LOG.debug(meth);
						}
						throw new RuntimeException(option.getParseMethod()+"; "+this.getClass()+"; "+option.getClass()+"; \nContact Norma developers: ", nsme);
					}
					parseMethod.setAccessible(true);
					parseMethod.invoke(this, option, argIterator);
					processed = true;
					chosenArgumentOptionList.add(option);
					break;
				}
			}
			if (!processed) {
				LOG.error("Unknown arg: ("+arg+"), trying to recover");
			}
		}
	}

	protected void runRunMethod(ArgumentOption option) throws Exception {
		String runMethodName = option.getRunMethodName();
		if (runMethodName != null) {
			Method runMethod = null;
			try {
				runMethod = this.getClass().getMethod(runMethodName, option.getClass()); 
			} catch (NoSuchMethodException nsme) {
				throw new RuntimeException(runMethodName+"; "+this.getClass()+"; "+option.getClass()+"; \nContact Norma developers: ", nsme);
			}
			runMethod.setAccessible(true);
			runMethod.invoke(this, option);
		}
	}

	protected void runOutputMethod(ArgumentOption option) throws Exception {
		String outputMethodName = option.getOutputMethodName();
		if (outputMethodName != null) {
			Method outputMethod = null;
			try {
				outputMethod = this.getClass().getMethod(outputMethodName, option.getClass()); 
			} catch (NoSuchMethodException nsme) {
				throw new RuntimeException(outputMethodName+"; "+this.getClass()+"; "+option.getClass()+"; \nContact Norma developers: ", nsme);
			}
			outputMethod.setAccessible(true);
			outputMethod.invoke(this, option);
		}
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

	public void runAndOutput() {
		ensureQuickscrapeNormaList();
		for (int i = 0; i < quickscrapeNormaList.size(); i++) {
			currentQuickscrapeNorma = quickscrapeNormaList.get(i);
			runRunMethodsOnChosenArgOptions();
			runOutputMethodsOnChosenArgOptions();
		}
	}

//	/** runs commands after assembling input.
//	 * 
//	 * normally called after parseArgs().
//	 * 
//	 * Override in subclasses
//	 * 
//	 */
//	public void run() {
//		LOG.error("Override run(); in subclasses");
//	}

}
