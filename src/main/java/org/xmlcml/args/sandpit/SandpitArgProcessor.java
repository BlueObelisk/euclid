package org.xmlcml.args.sandpit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.args.ArgIterator;
import org.xmlcml.args.ArgumentOption;
import org.xmlcml.args.DefaultArgProcessor;
import org.xmlcml.euclid.IntArray;
import org.xmlcml.euclid.RealArray;

/** 
 * Processes commandline arguments.
 * for Norma
 * 
 * @author pm286
 */
public class SandpitArgProcessor extends DefaultArgProcessor{
	
	public static final Logger LOG = Logger.getLogger(SandpitArgProcessor.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
		
	private static String RESOURCE_NAME_TOP = "/org/xmlcml/args/sandpit";
	private static String ARGS_RESOURCE = RESOURCE_NAME_TOP+"/"+"args.xml";

	private Double dubble;
	private RealArray doubleArray;
	private Integer intg;
	private IntArray intArray;
	
	public SandpitArgProcessor() {
		super();
		this.readArgumentOptions(ARGS_RESOURCE);
	}

	public SandpitArgProcessor(String[] args) {
		this();
		parseArgs(args);
	}

	// ============= METHODS =============
	
 	public void parseDouble(ArgumentOption option, ArgIterator argIterator) {
		dubble = argIterator.getDouble(option);
	}

	
 	public void parseDoubleArray(ArgumentOption option, ArgIterator argIterator) {
		doubleArray = argIterator.getDoubleArray(option);
	}

 	public void parseInteger(ArgumentOption option, ArgIterator argIterator) {
		intg = argIterator.getInteger(option);
	}

	
 	public void parseIntegerArray(ArgumentOption option, ArgIterator argIterator) {
		intArray = argIterator.getIntArray(option);
	}

	
	// ===========run===============
	
	public Double getDouble() {
		return dubble;
	}

	public RealArray getDoubleArray() {
		return doubleArray;
	}

	public Integer getInteger() {
		return intg;
	}

	public IntArray getIntArray() {
		return intArray;
	}

	public void runTest(ArgumentOption option) {
		LOG.debug("RUN_TEST "+"is a dummy");
	}
		

	// ==========================
	


}
