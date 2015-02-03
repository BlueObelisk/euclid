package org.xmlcml.files;

import org.apache.commons.io.FilenameUtils;

/** general static utilities.
 * 
 * @author pm286
 *
 */
public class EuclidUtil {


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


}
