package org.xmlcml.files;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import nu.xom.Attribute;
import nu.xom.Element;

/** a container for a "result" from an action on a QSNorma.
 * 
 * Normally output to the "results" directory
 * 
 * @author pm286
 *
 */
public class ResultElement extends Element {

	
	private static final Logger LOG = Logger.getLogger(ResultElement.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public static final String TAG = "result";
	public static final String TITLE = "title";
	public static final String PRE = "pre";
	public static final String MATCH = "match";
	public static final String POST = "post";

	public ResultElement() {
		super(TAG);
	}

	public ResultElement(String title) {
		this();
		this.setTitle(title);
	}

	private void setTitle(String title) {
		if (title == null) {
			throw new RuntimeException("title cannot be null");
		}
		this.addAttribute(new Attribute(TITLE, title));
	}

	public String getMatch() {
		return this.getAttributeValue(MATCH);
	}
	
	public void setMatch(String value) {
		setValue(MATCH, value);
	}
	
	public String getPre() {
		return this.getAttributeValue(PRE);
	}
	
	public void setPre(String value) {
		setValue(PRE, value);
	}
	
	public String getPost() {
		return this.getAttributeValue(POST);
	}
	
	public void setPost(String value) {
		setValue(POST, value);
	}
	
	public void setValue(String name, String value) {
		Attribute attribute = new Attribute(name, value);
		this.addAttribute(attribute);
	}
	
}
