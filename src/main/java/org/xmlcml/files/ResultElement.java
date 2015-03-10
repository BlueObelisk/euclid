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

	public ResultElement() {
		super(TAG);
	}

	public void setValue(String name, String value) {
		Attribute attribute = new Attribute(name, value);
		this.addAttribute(attribute);
	}
	
}
