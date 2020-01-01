/**
 *    Copyright 2011 Peter Murray-Rust
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.xmlcml.euclid;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/** holds an array of IntRanges
 * may or may not overlap or be sorted
 * @author pm286
 *
 */
public class IntRangeArray implements Iterable<IntRange> {

	private static final PrintStream SYSOUT = System.out;
	private List<IntRange> rangeList;

	public IntRangeArray() {
		init();
	}
	
	public IntRangeArray(List<IntRange> ranges) {
		init();
		rangeList.addAll(ranges);
	}

	/** deep copy
	 * 
	 * @param array
	 */
	public IntRangeArray(IntRangeArray array) {
		this();
		for (IntRange range : array.rangeList) {
			this.add(new IntRange(range));
		}
	}

	private void init() {
		rangeList = new ArrayList<IntRange>();
	}
	
	public void add(IntRange range) {
		rangeList.add(range);
	}
	
	public void sort() {
		Collections.sort(rangeList);
	}
	
	public void sortAndRemoveOverlapping() {
		sort();
		List<IntRange> newList = new ArrayList<IntRange>();
		Iterator<IntRange> iterator = rangeList.iterator();
		IntRange lastRange = null;
		while (iterator.hasNext()) {
			IntRange range = iterator.next();
			if (lastRange == null) {
				newList.add(range);
				lastRange = range;
			} else {
				boolean intersects = lastRange.intersectsWith(range);
				if (intersects) {
					IntRange merged = lastRange.plus(range);
					newList.set(newList.size() - 1, merged);
					lastRange = merged;
				} else {
					newList.add(range);
					lastRange = range;
				}
			}
		}
		rangeList = newList;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof IntRangeArray)) {
			return false;
		}
		IntRangeArray array2 = (IntRangeArray) obj;
		if (this.size() != array2.size()) return false;
		for (int i = 0; i < this.size(); i++) {
			if (!this.get(i).equals(array2.get(i))) return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() {
		int h = 17;
		for (int i = 0; i < rangeList.size(); i++) {
			h += rangeList.get(i).hashCode() * 31;
		}
		return h;
	}

	public int size() {
		return rangeList.size();
	}

	public IntRange get(int serial) {
		return rangeList.get(serial);
	}

	public void debug() {
		for (IntRange range : rangeList) {
			SYSOUT.println(range);
		}
	}
	
//	public IntRangeArray intersectionWith(IntRangeArray array) {
//		IntRangeArray newArray = null;
//		if (array != null) {
//			IntRangeArray this2 = new IntRangeArray(this);
//			this2.sortAndRemoveOverlapping();
//			Iterator<IntRange> thisIterator = this2.iterator();
//			IntRangeArray array2 = new IntRangeArray(array);
//			array2.sortAndRemoveOverlapping();
//			Iterator<IntRange> arrayIterator = array.iterator();
//			Iterator<IntRange> currentIterator = thisIterator;
//			Iterator<IntRange> otherIterator = arrayIterator;
//			IntRange currentRange = currentIterator.hasNext() ? currentIterator.next() : null;
//			IntRange otherRange = otherIterator.hasNext() ? otherIterator.next() : null;
//			while (true) {
//				if (currentRange.i)
//			}
//		}
//		return newArray;
//	}
	
	public IntRangeArray plus(IntRangeArray array) {
		IntRangeArray newArray = null;
		if (array != null) {
			newArray = new IntRangeArray();
			for (IntRange intRange : this.rangeList) {
				newArray.add(new IntRange(intRange));
			}
			for (IntRange intRange : array.rangeList) {
				newArray.add(new IntRange(intRange));
			}
			newArray.sortAndRemoveOverlapping();
		}
		return newArray;
	}

	/** create array representing the gaps in this
	 * gaps at ends are NOT filled
	 * does not alter this
	 * @return
	 */
	public IntRangeArray inverse() {
		IntRangeArray newArray = null;
		IntRangeArray copy = new IntRangeArray(this);
		copy.sortAndRemoveOverlapping();
		if (copy.size() > 0) {
			newArray = new IntRangeArray();
			IntRange last = null;
			for (IntRange current : copy) {
				if (last != null) {
					IntRange gap = new IntRange(last.maxval, current.minval);
					newArray.add(gap);
				}
				last = current;
			}
		}
		return newArray;
	}

	public Iterator<IntRange> iterator() {
		return rangeList.iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (IntRange range : rangeList) {
			sb.append(range.toString());
		}
		sb.append("]");
		return sb.toString();
	}
}
