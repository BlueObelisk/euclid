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
import org.xmlcml.files.FileContainer;
import org.xmlcml.xml.XMLUtil;

public class DefaultArgProcessor {

	
	private static final Logger LOG = Logger
			.getLogger(DefaultArgProcessor.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public static final String MINUS = "-";
	public static final String[] DEFAULT_EXTENSIONS = {"html", "xml"};
	public final static String H = "-h";
	public final static String HELP = "--help";

//	public final static ArgumentOption INPUT_OPTION = new ArgumentOption(
//			DefaultArgProcessor.class,
//			"-i",
//			"--input",
//			"file(s)_and/or_url(s)",
//			"\nINPUT:\nInput stream (Files, directories, URLs), Norma tries to guess reasonable actions. \n"
//					+ "also expands some simple wildcards. The argument can either be a single object, or a list. Within objects\n"
//					+ "the content of curly brackets {...} is expanded as wildcards (cannot recurse). There can be multiple {...}\n"
//					+ "within an object and all are expanded (but be sensible - this could generate the known universe and crash the\n"
//					+ "system. (If this is misused it will be withdrawn). Objects (URLs, files) can be mixed but it's probably a\n"
//					+ "poor idea.\n"
//					+ "\n"
//					+ "The logic is: \n"
//					+ "(a) if an object starts with 'www' or 'http:' or 'https;' it's assumed to be a URL\n"
//					+ "(b) if it is a directory, then the contents (filtered by extension) are added to the list as files\n"
//					+ "(c) if it's a file it's added to the list\n"
//					+ "the wildcards in files and URLs are then expanded and the results added to the list\n"
//					+ "\n"
//					+ "Current wildcards:\n"
//					+ "  {n1:n2} n1,n2 integers: generate n1 ... n2 inclusive\n"
//					+ "  {foo,bar,plugh} list of strings\n"
//					+"",
//					String.class,
//					(String) null,
//			1, Integer.MAX_VALUE,
//			"processInput"
//			);
//
//			
//	public final static ArgumentOption OUTPUT_OPTION = new ArgumentOption(
//			DefaultArgProcessor.class,
//			"-o",
//			"--output",
//			"file_or_directory",
//			"\nOUTPUT\n Output is to local filestore ATM. If there is only one input\n"
//			+ "after wildcard expansion then a filename can be given. Else the argument must be a writeable directory; Norma\n"
//			+ "will do her best to create filenames derived from the input names. Directory structures will not be preserved\n"
//			+ "See also --recursive and --extensions",
//			String.class,
//			(String) null,
//			0, 1,
//			"processOutput"
//			);
//	
//	public final static ArgumentOption RECURSIVE_OPTION = new ArgumentOption(
//			DefaultArgProcessor.class,
//			"-r",
//			"--recursive",
//			"",
//			"\nRECURSIVE input directories\n "
//			+ "If the input is a directory then by default only the first level is searched\n"
//			+ "if the --recursive flag is set then all files in the directory tree may be input\n"
//			+ "See also --extensions",
//			Boolean.class,
//			(Boolean)false,
//			0, 0,
//			"processRecursive"
//			);
//	
//	public final static ArgumentOption EXTENSION_OPTION = new ArgumentOption(
//			DefaultArgProcessor.class,
//			"-e",
//			"--extensions",
//			"ext1 [ext2...]",
//			"\nEXTENSIONS \n "
//				+ "When a directory or directories are searched then all files are input by default\n"
//				+ "It is possible to limit the search by using only certain extensions(which "
//				+ "See also --recursive",
//			String.class,
//			(String) null,
//			1, Integer.MAX_VALUE,
//			"processExtensions"
//			);
//
//	public final static ArgumentOption HELP_OPTION = new ArgumentOption(
//			DefaultArgProcessor.class,
//			"-h",
//			"--help",
//			"",
//			"\nHELP \n "
//				+ "outputs help for all options, including superclass DefaultArgProcessor",
//			String.class,
//			(String) null,
//			0, 0,
//			"processHelp"
//			);
//
//	public final static List<ArgumentOption> DEFAULT_OPTION_LIST = Arrays.asList(
//			new ArgumentOption[] {
//				INPUT_OPTION,
//				OUTPUT_OPTION,
//				RECURSIVE_OPTION,
//				EXTENSION_OPTION,
//				HELP_OPTION
//			}
//	);
	
	private static String RESOURCE_NAME_TOP = "/org/xmlcml/args";
	private static String ARGS_RESOURCE = RESOURCE_NAME_TOP+"/"+"args.xml";
	
	private static final Pattern INTEGER_RANGE_PATTERN = Pattern.compile("(\\d+):(\\d+)");
	public static Pattern GENERAL_PATTERN = Pattern.compile("\\{([^\\}]*)\\}");
	
	protected String output;
	private List<String> extensions = Arrays.asList(DEFAULT_EXTENSIONS);
	private boolean recursive = false;
	protected List<String> inputList;
	public List<ArgumentOption> argumentOptionList;
	private ArrayList<FileContainer> fileContainerList;
	
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

	public void processExtensions(ArgumentOption option, ArgIterator argIterator) {
		setExtensions(argIterator.createTokenListUpToNextMinus());
	}

	public void processFileContainer(ArgumentOption option, ArgIterator argIterator) {
		fileContainerList = new ArrayList<FileContainer>();
		List<String> fileContainerNames = argIterator.createTokenListUpToNextMinus();
		for (String fileContainerName : fileContainerNames) {
			FileContainer fileContainer = new FileContainer(fileContainerName);
			fileContainerList.add(fileContainer);
		}
	}

	public void processHelp(ArgumentOption divOption, ArgIterator argIterator) {
		processHelp();
	}

	public void processInput(ArgumentOption divOption, ArgIterator argIterator) {
		List<String> inputs = argIterator.createTokenListUpToNextMinus();
		if (inputs.size() == 0) {
			inputList = new ArrayList<String>();
			LOG.error("Must give at least one input");
		} else {
			inputList = inputs;
		}
	}

	public void processOutput(ArgumentOption divOption, ArgIterator argIterator) {
		checkHasNext(argIterator);
		output = argIterator.next();
	}

	public void processRecursive(ArgumentOption divOption, ArgIterator argIterator) {
		recursive = true;
	}

	// =====================================
	public void setExtensions(List<String> extensions) {
		this.extensions = extensions;
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

	public List<FileContainer> getFileContainerList() {
		ensureFileContainerList();
		return fileContainerList;
	}

	private void ensureFileContainerList() {
		if (fileContainerList == null) {
			fileContainerList = new ArrayList<FileContainer>();
		}
	}
	

	// --------------------------------
	
	public boolean parseArgs(String[] commandLineArgs) {
		ArgIterator argIterator = new ArgIterator(commandLineArgs);
		return parseArgs(argIterator);
	}
	
	protected boolean parseArgs(ArgIterator argIterator) {
		return parseArgs(argumentOptionList, argIterator);
	}

	protected boolean parseArgs(List<ArgumentOption> optionList, ArgIterator argIterator) {
		boolean processed = false;
		while (argIterator.hasNext()) {
			String arg = argIterator.next();
			try {
				processed = runReflectedMethod(this.getClass(), optionList, argIterator, arg);
			} catch (Exception e) {
				throw new RuntimeException("cannot process argument: "+arg, e);
			}
		}
		return processed;
	}
	
	public List<String> getExtensions() {
		return extensions;
	}


	protected boolean runReflectedMethod(Class<?> thisClass, List<ArgumentOption> optionList, ArgIterator argIterator, String arg) throws Exception {
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
					method.setAccessible(true);
					method.invoke(this, option, argIterator);
					processed = true;
					break;
				}
			}
			if (!processed) {
				LOG.error("Unknown arg: ("+arg+"), trying to recover");
			}
		}
		return processed;
	}

	protected void processHelp() {
		for (ArgumentOption option : argumentOptionList) {
			System.err.println(option.getHelp());
		}
	}

}
