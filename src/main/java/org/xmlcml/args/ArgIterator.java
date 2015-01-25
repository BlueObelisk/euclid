package org.xmlcml.args;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/** wraps ListIterator as this causes reflection problems.
 * 
 * @author pm286
 *
 */
public class ArgIterator {

	private ListIterator<String> listIterator;
	
	public ArgIterator(ListIterator<String> listIterator) {
		this.listIterator = listIterator;
	}
	
	public ArgIterator(String[] args) {
		listIterator = args == null ? null : Arrays.asList(args).listIterator();
	}

	public boolean hasNext() {
		return listIterator == null ? false : listIterator.hasNext();
	}
	
	public String previous() {
		return listIterator == null ? null : listIterator.previous();
	}

	public String next() {
		return listIterator == null ? null : listIterator.next();
	}

	/** read tokens until next - sign.
	 * 
	 * leave iterator ready to read next minus
	 * 
	 * @param argIterator
	 * @return
	 */
	public List<String> createTokenListUpToNextMinus() {
		List<String> list = new ArrayList<String>();
		while (this.hasNext()) {
			String next = this.next();
			if (next.startsWith(DefaultArgProcessor.MINUS)) {
				this.previous();
				break;
			}
			list.add(next);
		}
		return list;
	}

}