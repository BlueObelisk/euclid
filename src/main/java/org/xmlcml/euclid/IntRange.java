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
/**
 * maximum and minimum values
 * 
 * Contains two ints representing the minimum and maximum of an allowed or
 * observed range.
 * <P>
 * Default is range with low > high; this can be regarded as the uninitialised
 * state. If points are added to a default IntRange it becomes initialised.
 * 
 * @author (C) P. Murray-Rust, 1996
 */
public class IntRange implements EuclidConstants, Comparable<IntRange> {
    /**
     * maximum of range
     */
    protected int maxval;
    /**
     * minimum of range
     */
    protected int minval;
    /**
     * creates invalid range from MAX_VALUE to MIN_VALUE
     */
    public IntRange() {
        minval = Integer.MAX_VALUE;
        maxval = Integer.MIN_VALUE;
    }
    /**
     * initialise with min and max values; if minv > maxv create inValid
     * IntRange
     * 
     * @param minv
     * @param maxv
     */
    public IntRange(int minv, int maxv) {
        maxval = maxv;
        minval = minv;
        if (minval > maxval) {
            minval = Integer.MAX_VALUE;
            maxval = Integer.MIN_VALUE;
        }
    }
    /**
     * copy constructor
     * 
     * @param r
     */
    public IntRange(IntRange r) {
        minval = r.minval;
        maxval = r.maxval;
    }
    
    /**
     * 
     * @param r
     */
    public IntRange(RealRange r) {
        minval = (int) Math.round(r.minval);
        maxval = (int) Math.round(r.maxval);
    }
    /**
     * a Range is only valid if its maxval is not less than its minval; this
     * tests for uninitialised ranges
     * 
     * @return valid
     */
    public boolean isValid() {
        return (minval <= maxval);
    }
    /**
     * invalid ranges return false
     * 
     * @param r
     * @return equals
     * 
     */
    public boolean isEqualTo(IntRange r) {
        return (r != null && minval == r.minval && maxval == r.maxval && minval <= maxval);
    }
    
    
    @Override
    public boolean equals(Object o) {
    	boolean equals = false;
    	if (o != null && o instanceof IntRange) {
    		IntRange ir =(IntRange) o;
    		equals = this.minval == ir.minval && this.maxval == ir.maxval;
    	}
    	return equals;
    }
    
    @Override
    public int hashCode() {
    	return 17*minval + 31*maxval;
    }
    
    /**
     * combine two ranges if both valid; takes greatest limits of both, else
     * returns InValid
     * 
     * @param r2
     * @return range
     */
    public IntRange plus(IntRange r2) {
        if (!this.isValid()) {
            if (r2 == null || !r2.isValid()) {
                return new IntRange();
            }
            return new IntRange(r2);
        }
        IntRange temp = new IntRange();
        temp = new IntRange(Math.min(minval, r2.minval), Math.max(maxval,
                r2.maxval));
        return temp;
    }
    
    public boolean intersectsWith(IntRange r2) {
    	IntRange r = this.intersectionWith(r2);
    	return r != null && r.isValid();
    }
    /**
     * intersect two ranges and take the range common to both; return invalid
     * range if no overlap
     * 
     * @param r2
     * @return range
     */
    public IntRange intersectionWith(IntRange r2) {
        if (!isValid() || r2 == null || !r2.isValid()) {
            return new IntRange();
        }
        int minv = Math.max(minval, r2.minval);
        int maxv = Math.min(maxval, r2.maxval);
        return new IntRange(minv, maxv);
    }
    /**
     * get minimum value (MAX_VALUE if inValid)
     * 
     * @return min
     */
    public int getMin() {
        return minval;
    }
    /**
     * get maximum value (MIN_VALUE if inValid)
     * 
     * @return max
     * 
     */
    public int getMax() {
        return maxval;
    }
    /**
     * get range (MIN_VALUE if invalid)
     * 
     * @return range
     */
    public int getRange() {
        if (!isValid())
            return Integer.MIN_VALUE;
        return maxval - minval;
    }
    /**
     * does one range include another
     * 
     * @param r2
     * @return includes
     */
    public boolean includes(IntRange r2) {
        return (r2 != null && r2.isValid() && this.includes(r2.getMin()) && this
                .includes(r2.getMax()));
    }
    /**
     * is a int within a IntRange
     * 
     * @param f
     * @return includes If inValid, return false
     */
    public boolean includes(int f) {
        return f >= minval && f <= maxval;
    }
    /**
     * synonym for includes()
     * 
     * @param f
     * @return includes
     */
    public boolean contains(int f) {
        return includes(f);
    }
    /**
     * add a value to a range
     * 
     * @param x
     */
    public void add(int x) {
        maxval = Math.max(maxval, x);
        minval = Math.min(minval, x);
    }
    /**
     * to string
     * 
     * @return string
     */
    public String toString() {
        return (minval > maxval) ? "NULL" : S_LBRAK + minval + S_COMMA + maxval + S_RBRAK;
    }

    /** comparees on min values
     * 
     * @param intRange
     * @return
     */
	public int compareTo(IntRange intRange) {
		if (intRange == null) {
			return -1;
		} else if (this.minval < intRange.minval) {
			return -1;
		} else if (this.minval > intRange.minval) {
			return 1;
		} else {
			if (this.maxval < intRange.maxval) {
				return -1;
			} else if (this.maxval > intRange.maxval) {
				return 1;
			}
		}
		return 0;
	}
	
	/** makes new IntRange extended by deltaMin and deltaMax.
	 * 
	 * the effect is for positive numbers to increase the range.
	 * if extensions are negative they are applied, but may result
	 * in invalid range (this is not checked at this stage).
	 * <p>
	 * Does not alter this.
	 * </p>
	 * 
	 * @param minExtend subtracted from min
	 * @param maxExtend  added to max
	 */
	public IntRange getRangeExtendedBy(int minExtend, int maxExtend) {
		return  new IntRange(minval - minExtend, maxval + maxExtend);
	}

}
