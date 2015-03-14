package org.xmlcml.files;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/** wrapper for collection of ResultsElement.
 * 
 * @author pm286
 *
 */
public class ResultsElementList implements Iterable<ResultsElement> {

	private static final Logger LOG = Logger.getLogger(ResultsElementList.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}

	protected List<ResultsElement> resultsElementList;
	
	public ResultsElementList() {
		
	}

	public void add(ResultsElement resultsElement) {
		ensureResultsElementList();
		resultsElementList.add(resultsElement);
	}

	protected void ensureResultsElementList() {
		if (resultsElementList == null) {
			resultsElementList = new ArrayList<ResultsElement>();
		}
	}

	@Override
	public Iterator<ResultsElement> iterator() {
		ensureResultsElementList();
		return resultsElementList.iterator();
	}
}
