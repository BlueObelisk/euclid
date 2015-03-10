package org.xmlcml.files;

import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.xml.XMLUtil;

/** a container for ResultElement's.
 * 
 * @author pm286
 *
 */

public class ResultsElement extends Element {

	
	private static final Logger LOG = Logger.getLogger(ResultsElement.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public static final String TAG = "results";
	public static final String TITLE = "title";

	public ResultsElement() {
		super(TAG);
	}

	public ResultsElement(String title) {
		this();
		this.setTitle(title);
	}

	public void setTitle(String title) {
		this.addAttribute(new Attribute(TITLE, title));
	}

	public String getTitle() {
		return this.getAttributeValue(TITLE);
	}
	

	/** transfers with detachment ResultElemen's in one ResultsElement to another.
	 * 
	 * @param subResultsElement source of ResultElement's
	 */
	public void transferResultElements(ResultsElement subResultsElement) {
		List<ResultElement> subResults = subResultsElement.getResultElementList();
		for (ResultElement subResult : subResults) {
			subResult.detach();
			this.appendChild(subResult);
		}
	}

	private List<ResultElement> getResultElementList() {
		List<ResultElement> resultElementList = new ArrayList<ResultElement>();
		List<Element> resultChildren = XMLUtil.getQueryElements(this, "./*[local-name()='"+ResultElement.TAG+"']");
		for (Element resultElement : resultChildren) {
			resultElementList.add((ResultElement) resultElement);
		}
		return resultElementList;
	}

	
}
