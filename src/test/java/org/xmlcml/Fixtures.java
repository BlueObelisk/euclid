package org.xmlcml;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Fixtures {

	
	private static final Logger LOG = Logger.getLogger(Fixtures.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public final static File TEST_RESOURCES = new File("src/test/resources");
	public final static File TEST_DIR = new File(Fixtures.TEST_RESOURCES, "org/xmlcml");
	public final static File FILES_DIR = new File(Fixtures.TEST_DIR, "files");
	public final static File TEST_PLOSONE_0115884_DIR = new File(Fixtures.FILES_DIR, "journal.pone.0115884");
}
