package org.xmlcml.files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.FilenameUtils;
import org.xmlcml.euclid.Euclid;

/** general static utilities.
 * 
 * @author pm286
 *
 */
public class EuclidSource {


	public static final String DOI = "doi:";
	public static final String HTTP = "http://";
	public static final String HTTPS = "https://";
	public static final String HTM = "htm";
	public static final String HTML = "html";
	public static final String PDF = "pdf";
	public static final String SVG = "svg";
	public static final String XML = "xml";
	
	public static final String LINE_NUMBER = "lineNumber";
	public static final String LINE_VALUE = "lineValue";
	public static final String XPATH = "xpath";
	
	public static boolean endsWithSeparator(String filename) {
		return filename != null && FilenameUtils.indexOfLastSeparator(filename) == filename.length()-1;
	}

	/** crude tool to guess whether is URL from name.
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isURL(String name) {
		return name.startsWith(HTTP) || name.startsWith(HTTPS);
	}

	/** heuristic ducktype to get input stream;
	 * 
	 * First assumes name is resource on classpath.
	 * if fails; tries it as http:// or https:// URL
	 * if fails; tries as filename
	 * 
	 * @param name (of resource, URL, or filename)
	 * @return Opened stream, or null if not found
	 */
	public static InputStream getInputStream(String name) {
		InputStream is = Euclid.class.getResourceAsStream(name);
		if (is == null) {
			try {
				is = new URL(name).openStream();
			} catch (Exception e) {
				// not a URL
			}
		}
		if (is == null) {
			try {
				is = new FileInputStream(name);
			} catch (FileNotFoundException e) {
				// no file
			}
		}
		return is;
	}


}
