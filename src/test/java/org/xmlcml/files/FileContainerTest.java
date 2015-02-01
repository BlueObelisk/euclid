package org.xmlcml.files;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.xmlcml.Fixtures;
import org.xmlcml.args.DefaultArgProcessor;

public class FileContainerTest {

	
	private static final Logger LOG = Logger.getLogger(FileContainerTest.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public final static File FILE_CONTAINER_DIR = new File("src/test/resources/org/xmlcml/files/");
	public final static File PLOS0115884_DIR = new File(FILE_CONTAINER_DIR, "journal.pone.0115884");
	
	@Test
	public void testReadQuickscrapeDirectory() {
		FileContainer fileContainer = new FileContainer();
		fileContainer.readDirectory(PLOS0115884_DIR);
		Assert.assertEquals("fileCount", 4, fileContainer.getFileList().size());
		Assert.assertTrue("XML", fileContainer.hasFulltextXML());
	}
	
	@Test
	public void testFileContainer() throws IOException {
		File container0115884 = new File("target/plosone/0115884/");
		// copy so we don't write back into test area
		FileUtils.copyDirectory(Fixtures.TEST_PLOSONE_0115884_DIR, container0115884);
		String[] args = {
			"-f", container0115884.toString(),
		};
		DefaultArgProcessor argProcessor = new DefaultArgProcessor();
		argProcessor.parseArgs(args);
		List<FileContainer> fileContainerList = argProcessor.getFileContainerList();
		Assert.assertEquals(1,  fileContainerList.size());
		LOG.debug(fileContainerList.get(0).toString());
	}
}
