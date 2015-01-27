package org.xmlcml.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;


/** collection of files from quickscrape with addiitons from Norma.
 * 
 * @author pm286
 *
 */
public class FileContainer {


	private static final Logger LOG = Logger.getLogger(FileContainer.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	private static final String RESULTS_JSON = "results.json";
	private static final String FULLTEXT_DOCX = "fulltext.docx";
	private static final String FULLTEXT_HTML = "fulltext.html";
	private static final String FULLTEXT_PDF = "fulltext.pdf";
	private static final String FULLTEXT_XML = "fulltext.xml";
	private static final String ABSTRACT_HTML = "abstract.html";

	private List<File> fileList;
	private File directory;
	
	public FileContainer() {
		
	}
	
	public void readQuickscrapeDirectory(File dir) {
		this.directory = dir;
		Multimap<String, File> map = HashMultimap.create();
		
		requireDirectoryExists(dir);
		fileList = new ArrayList<File>(FileUtils.listFiles(dir, null, false));
		checkRequiredQuickscrapeFiles();
		indexByFileExtensions();
	}

	private void indexByFileExtensions() {
		for (File file : fileList) {
			addFileToExtensionTable(file);
			
		}
	}

	private void addFileToExtensionTable(File file) {
		String extension = FilenameUtils.getExtension(file.getName()).toLowerCase();
	}

	private void checkRequiredQuickscrapeFiles() {
		requireExistingNonEmptyFile(new File(directory, RESULTS_JSON));
	}

	/*
	private void checkMandatory(String testFilename) {
		testFilename = FilenameUtils.separatorsToUnix(testFilename);
		testFilename = FilenameUtils.normalize(testFilename);
		String testPath = FilenameUtils.getPath(testFilename);
		String testBase = FilenameUtils.getBaseName(testFilename);
		for (File file : fileList) {
			String fname = FilenameUtils.separatorsToUnix(file.getName());
			fname = FilenameUtils.normalize(fname);
			String path = FilenameUtils.getPath(fname);
			String base = FilenameUtils.getBaseName(testFilename);
		}
	}
	*/

	public List<File> getFileList() {
		return fileList;
	}

	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}

	private boolean hasExistingFile(File file) {
		return (file != null) && file.exists() && !file.isDirectory();
	}

	private boolean hasExistingSubDirectory(File subdir) {
		return (subdir != null) && subdir.exists() && !subdir.isDirectory();
	}

	private void requireDirectoryExists(File dir) {
		if (dir == null) {
			throw new RuntimeException("Null directory");
		}
		if (!dir.exists()) {
			throw new RuntimeException("Directory: "+dir+" does not exist");
		}
		if (!dir.isDirectory()) {
			throw new RuntimeException("File: "+dir+" is not a directory");
		}
	}
	
	private void requireExistingNonEmptyFile(File file) {
		if (file == null) {
			throw new RuntimeException("Null file");
		}
		if (!file.exists()) {
			throw new RuntimeException("File: "+file+" does not exist");
		}
		if (file.isDirectory()) {
			throw new RuntimeException("File: "+file+" must not be a directory");
		}
		if (FileUtils.sizeOf(file) == 0) {
			throw new RuntimeException("File: "+file+" must not be empty");
		}
	}

	public boolean hasFulltextXML() {
		return hasExistingFile(new File(directory, FULLTEXT_XML));
	}
	
	public boolean hasFulltextHTML() {
		return hasExistingFile(new File(directory, FULLTEXT_HTML));
	}
	
	public boolean hasFulltextPDF() {
		return hasExistingFile(new File(directory, FULLTEXT_PDF));
	}
	
	public boolean hasFulltextDOCX() {
		return hasExistingFile(new File(directory, FULLTEXT_DOCX));
	}
	
	
}
