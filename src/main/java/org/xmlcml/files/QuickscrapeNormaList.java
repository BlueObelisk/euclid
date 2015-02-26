package org.xmlcml.files;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/** list of QuickscrapeNorma objects.
 * 
 * @author pm286
 *
 */
public class QuickscrapeNormaList implements Iterable<QuickscrapeNorma> {

	
	private static final Logger LOG = Logger
			.getLogger(QuickscrapeNormaList.class);
	static {
		LOG.setLevel(Level.DEBUG);
	}
	
	private List<QuickscrapeNorma> qnList;
	
	public QuickscrapeNormaList() {
		ensureQNList();
	}

	private void ensureQNList() {
		if (qnList == null) {
			qnList = new ArrayList<QuickscrapeNorma>();
		}
	}

	public int size() {
		ensureQNList();
		return qnList.size();
	}

	@Override
	public Iterator<QuickscrapeNorma> iterator() {
		ensureQNList();
		return qnList.iterator();
	}
	
	public QuickscrapeNorma get(int i) {
		ensureQNList();
		return qnList.get(i);
	}
	
	public void add(QuickscrapeNorma qn) {
		ensureQNList();
		qnList.add(qn);
	}
	
}
