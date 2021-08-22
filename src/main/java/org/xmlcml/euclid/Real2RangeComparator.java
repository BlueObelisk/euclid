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

import org.apache.log4j.Logger;

/** comparator for use with TreeSet<Double> and other tools which normally require equals().
 * 
 * @author pm286
 *
 */
public class Real2RangeComparator implements Comparator<Real2Range> {

	private final static Logger LOG = Logger.getLogger(Real2RangeComparator.class);

	private RealRangeComparator comparatorx;
	private RealRangeComparator comparatory;
	
	public Real2RangeComparator(RealRangeComparator comparator) {
		this.setComparators(comparator, comparator);
	}

	public Real2RangeComparator(RealRangeComparator comparatorx, RealRangeComparator comparatory) {
		this.setComparators(comparatorx, comparatory);
	}

	public Real2RangeComparator(double d) {
		this(new RealRangeComparator(d));
	}

	/**
	 * if Math.abs(d0-d1) <= epsilon  
	 * return -1 if either arg is null or any ranges in r0 or r1 are null or comparisons clash
	 */
	public int compare(Real2Range r0, Real2Range r1) {
		Real2Range limit = new Real2Range(new RealRange(0.,240.), new RealRange(0.,60.));
		String s1 = "(56.689,231.236),(31.643,49.536)";
//		(56.689,231.236),(31.643,49.536)
		if (r0.toString().contains(s1) || r1.toString().contains(s1)) {
			LOG.trace(r0+" / "+r1);
		}
		if (r0 == null || r1 == null) {
			return -1;
		}
		RealRange r0x = r0.getXRange();
		RealRange r0y = r0.getYRange();
		RealRange r1x = r1.getXRange();
		RealRange r1y = r1.getYRange();
		if (r0x == null || r1x == null || r0y == null || r1y == null) {
			return -1;
		}
		int comparex = comparatorx.compare(r0x, r1x);
		int comparey = comparatory.compare(r0y, r1y);
		if (limit.includes(r0) && limit.includes(r1)) {
//		if (comparex * comparey == 0) {
			LOG.trace(r0+" / "+r1+" / "+comparex+"/"+comparey);
		}
		return (comparex == comparey) ? comparex : -1;
	}
	
	/** set the tolerance
	 * negative values are converted to positive
	 * @param comparatorx
     * @param comparatory
	 */
	public void setComparators(RealRangeComparator comparatorx, RealRangeComparator comparatory) {
		this.comparatorx = comparatorx;
		this.comparatory = comparatory;
	
	}

}
