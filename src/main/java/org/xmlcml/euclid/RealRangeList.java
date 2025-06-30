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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * a sorted list of ranges.
 * 
 * keeps list sorted at all times.
 * 
 * when a RealRange is added it looks for the next lowest and highest ranges. If
 * it does not overlap it is inserted in the free space. If it overlaps with
 * either or both they merge.
 * 
 * Currently buublesort-like and assumes "not too many" and may suffer from
 * quadratic performance.
 * 
 * // TODO add binary chop or other sort
 * 
 * @author pm286
 * 
 */
public class RealRangeList {

	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(RealRangeList.class);

	private List<RealRange> rangeList;
	private int pointer;
	private boolean merged;
	private RealRange newRange;
	private RealRange oldRange;

	public RealRangeList() {
		rangeList = new ArrayList<RealRange>();
	}

	/**
	 * adds range and returns position of result.
	 * 
	 * if range overlaps with any existing ranges merges them
	 * 
	 * if no overlap inserts before next highest non-overlapping range
	 * 
	 * @param range
	 *            to add
	 * @return
	 */
	public int addRange(RealRange range) {
		int result = -1;
		if (rangeList.size() == 0) {
			rangeList.add(range);
			result = 0;
		} else {
			this.newRange = range;
			result = insertRange1();
		}
		return result;
	}

//	private int insertRange() {
//		int result = -1;
//		pointer = 0;
//		while (pointer < rangeList.size()) {
//			RealRange lastRange = (pointer == 0) ? null : rangeList
//					.get(pointer - 1);
//			RealRange nextRange = (pointer >= rangeList.size()) ? null
//					: rangeList.get(pointer);
//			// look for next?
//			if (newRange.getMin() > nextRange.getMax()) {
//				pointer++;
//				continue;
//			}
//			// insert?
//			boolean overlapsLast = overlapsAtBottom(newRange, lastRange)
//					&& lastRange != null;
//			boolean nextOverlaps = overlapsAtBottom(nextRange, newRange);
//			if (!overlapsLast && !nextOverlaps) {
//				result = pointer;
//				rangeList.add(pointer, newRange);
//				break;
//			}
//			result = pointer;
//			merged = false;
//			if (overlapsLast) {
//				lastRange.plusEquals(newRange);
//				newRange = lastRange;
//				result--;
//				merged = true;
//			}
//			// overlap with next and merge
////			LOG.debug(this);
//			result = pointer;
//			List<Integer> mergedRanges = iterateTillNextNonOverlapping(newRange,
//					nextRange, nextOverlaps);
//			LOG.trace(newRange + "/" +this + "/" +  pointer);
//			if (merged) {
//				int largest = Math.max(0, rangeList.size() - 1);
//				newRange.plusEquals(rangeList.get(largest)); // take largest extent
//				removeOverlappedRangesAndReplaceByOverallRange(result,
//						mergedRanges);
//			}
//			break;
//		}
//		// add at end?
//		if (result == -1) {
//			result = rangeList.size();
//			rangeList.add(newRange);
//		}
//		return result;
//	}
	
	private int insertRange1() {
		pointer = findFirstLargerOrOverlappingExistingRange();
		int firstHigher = pointer;
		List<Integer> overlappingRanges = findAllOverlappingRanges();
		subsumeAndDeleteAllOverlappingRanges(overlappingRanges);
		rangeList.add(firstHigher, newRange);
		return firstHigher;
	}

	private int findFirstLargerOrOverlappingExistingRange() {
		pointer = 0;
		for (; pointer < rangeList.size(); pointer++) {
			oldRange = rangeList.get(pointer);
			if (oldRange.getMax() >= newRange.getMin()) {
				break;
			}
		}
		return pointer;
	}

	private List<Integer> findAllOverlappingRanges() {
		List<Integer> overlappingRanges = new ArrayList<Integer>();
		for (; pointer < rangeList.size(); pointer++) {
			oldRange = rangeList.get(pointer);
			if (oldRange.getMin() > newRange.getMax()) {
				break;
			}
			newRange.plusEquals(oldRange);
			overlappingRanges.add(pointer);
		}
		return overlappingRanges;
	}

	private void subsumeAndDeleteAllOverlappingRanges(List<Integer> overlappingRanges) {
		Collections.reverse(overlappingRanges);
		int noverlap = overlappingRanges.size();
		for (int i = 0; i < noverlap; i++) {
			int toRemove = overlappingRanges.get(i);
			rangeList.remove(toRemove);
		}
	}

//	private void removeOverlappedRangesAndReplaceByOverallRange(
//			int result, List<Integer> mergedRanges) {
//		Collections.reverse(mergedRanges);
//		//LOG.debug(mergedRanges);
//		for (int ii = 0; ii < mergedRanges.size() - 1; ii++) {
//			int toRemove = (int) mergedRanges.get(ii);
//			rangeList.remove(toRemove);
//		}
//		rangeList.set(result, newRange);
//	}

//	private List<Integer> iterateTillNextNonOverlapping(RealRange range,
//			RealRange nextRange, boolean nextOverlaps) {
//		List<Integer> mergedRanges = new ArrayList<Integer>();
//		while (nextOverlaps) {
//			range.plusEquals(nextRange);
//			nextRange = this.get(pointer);
//			nextOverlaps = overlapsAtBottom(nextRange, range);
//			if (!nextOverlaps) {
//				break;
//			}
//			mergedRanges.add(pointer);
//			merged = true;
//			pointer++;
//		}
//		return mergedRanges;
//	}

//	// is all of range1 less than all of range0?
//	private boolean overlapsAtBottom(RealRange higher, RealRange lower) {
//		return higher != null && lower != null
//				&& lower.getMax() >= higher.getMin();
//	}

	public int size() {
		return rangeList.size();
	}

	public RealRange get(int i) {
		return (i < 0 || i >= rangeList.size()) ? null : rangeList.get(i);
	}

	public RealRange remove(int i) {
		return (i < 0 || i >= rangeList.size()) ? null : rangeList.remove(i);
	}

	public String toString() {
		return rangeList.toString();
	}
}
