package org.xmlcml.files;

import java.io.File;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

public class TestFileContainer {

	
	private static final Logger LOG = Logger.getLogger(TestFileContainer.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public final static File FILE_CONTAINER_DIR = new File("src/test/resources/org/xmlcml/files/");
	public final static File PLOS0115884_DIR = new File(FILE_CONTAINER_DIR, "journal.pone.0115884");
	
	@Test
	public void testReadQuickscrapeDirectory() {
		FileContainer fileContainer = new FileContainer();
		fileContainer.readQuickscrapeDirectory(PLOS0115884_DIR);
		Assert.assertEquals("fileCount", 4, fileContainer.getFileList().size());
		Assert.assertTrue("XML", fileContainer.hasFulltextXML());
	}
}
