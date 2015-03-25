package org.xmlcml.files;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xmlcml.xml.XMLUtil;

/** a container for ResultElement's.
 * 
 * @author pm286
 *
 */

public class ResultsElement extends Element implements Iterable<ResultElement> {

	
	private static final Logger LOG = Logger.getLogger(ResultsElement.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	public static final String TAG = "results";
	public static final String TITLE = "title";
	
	protected List<ResultElement> resultElementList;

	public ResultsElement() {
		super(TAG);
	}

	public ResultsElement(ResultsElement element) {
		this();
		copyAttributesAndAddChildren(element);
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
	
	/** create ResultsElement from reading Element.
	 * 
	 * @param element
	 * @return
	 */
	public static ResultsElement createResults(Element element) {
		return (ResultsElement) createResults0(element);
	}
	
	private static Element createResults0(Element element) {
		Element newElement = null;
		String tag = element.getLocalName();
		if (ResultsElement.TAG.equals(tag)) {	
			newElement = new ResultsElement();
		} else if (ResultElement.TAG.equals(tag)) {	
			newElement = new ResultElement();
		} else {
			LOG.error("Unknown element: "+tag);
		}
		XMLUtil.copyAttributes(element, newElement);
		for (int i = 0; i < element.getChildCount(); i++) {
			Node child = element.getChild(i);
			if (child instanceof Text) {
				child = child.copy();
			} else {
				child = ResultsElement.createResults0((Element)child);
			}
			if (newElement != null && child != null) {	
				newElement.appendChild(child);
			}
		}
		LOG.trace("XML :"+newElement.toXML());
		return newElement;
	}

	/** transfers with detachment ResultElemen's in one ResultsElement to another.
	 * 
	 * @param subResultsElement source of ResultElement's
	 */
	public void transferResultElements(ResultsElement subResultsElement) {
		List<ResultElement> subResults = subResultsElement.getOrCreateResultElementList();
		for (ResultElement subResult : subResults) {
			subResult.detach();
			this.appendChild(subResult);
		}
	}

	protected List<ResultElement> getOrCreateResultElementList() {
		resultElementList = new ArrayList<ResultElement>();
		List<Element> resultChildren = XMLUtil.getQueryElements(this, "./*[local-name()='"+ResultElement.TAG+"']");
		for (Element resultElement : resultChildren) {
			resultElementList.add((ResultElement) resultElement);
		}
		return resultElementList;
	}

	@Override
	public Iterator<ResultElement> iterator() {
		getOrCreateResultElementList();
		return resultElementList.iterator();
	}

	public int size() {
		getOrCreateResultElementList();
		return resultElementList == null ? 0 : resultElementList.size();
	}

	protected void copyAttributesAndAddChildren(ResultsElement resultsElement) {
		if (resultsElement == null) {
			throw new RuntimeException("Null ResultsElement");
		}
		XMLUtil.copyAttributesFromTo(resultsElement, this);
		for (ResultElement resultElement : resultsElement) {
			this.appendChild(resultElement);
		}
	}
}
