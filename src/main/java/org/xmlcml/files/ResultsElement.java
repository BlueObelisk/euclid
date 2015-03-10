package org.xmlcml.files;

import java.util.ArrayList;
import java.util.List;

import org.xmlcml.xml.XMLUtil;

import nu.xom.Element;

/** a container for ResultElement's.
 * 
 * @author pm286
 *
 */

public class ResultsElement extends Element {

	public static final String TAG = "results";

	public ResultsElement() {
		super(TAG);
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
