package org.xmlcml.files;

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

	public static final String TAG = "result";
	public static final String TITLE = "title";

	public ResultElement() {
		super(TAG);
	}

	public ResultElement(String title) {
		this();
		this.setTitle(title);
	}

	private void setTitle(String title) {
		if (title == null) {
			throw new RuntimeException("tite cannot be null");
		}
		this.addAttribute(new Attribute(TITLE, title));
	}

	public void setValue(String name, String value) {
		Attribute attribute = new Attribute(name, value);
		this.addAttribute(attribute);
	}
	
}
