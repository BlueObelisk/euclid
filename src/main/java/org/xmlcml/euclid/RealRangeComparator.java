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

import java.util.Comparator;

/** comparator for use with TreeSet<Double> and other tools which normally require equals().
 * 
 * @author pm286
 *
 */
public class RealRangeComparator implements Comparator<RealRange> {


	private RealComparator comparator;
	
	public RealRangeComparator(RealComparator comparator) {
		this.setComparator(comparator);
	}

	public RealRangeComparator(double d) {
		this(new RealComparator(d));
	}

	/**
	 * if Math.abs(d0-d1) {@literal <}= epsilon
	 * return -1 if either arg is null or any ranges in r0 or r1 are null or comparisons clash
	 */
	public int compare(RealRange r0, RealRange r1) {
		if (r0 == null || r1 == null) {
			return -1;
		}
		Double r0min = r0.getMin();
		Double r0max = r0.getMax();
		Double r1min = r1.getMin();
		Double r1max = r1.getMax();
		int compareMin = comparator.compare(r0min, r1min);
		int compareMax = comparator.compare(r0max, r1max);
		return (compareMin == compareMax) ? compareMin : -1;
	}
	
	/** set the comparator
	 * @param comparator
	 */
	public void setComparator(RealComparator comparator) {
		this.comparator = comparator;
	}

}
