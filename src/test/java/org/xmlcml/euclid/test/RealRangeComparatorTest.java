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
package org.xmlcml.euclid.test;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.euclid.RealComparator;
import org.xmlcml.euclid.RealRange;
import org.xmlcml.euclid.RealRangeComparator;

public class RealRangeComparatorTest {

	public final static Double ZERO = 0.0;
	public final static Double EPS = 0.01;
	public final static Double ONE = 1.0;
	public final static Double TWO = 2.0;
	public final static Double THREE = 3.0;
	public final static RealRange ONE_TWO = new RealRange(ONE, TWO);
	public final static RealRange ZERO_ONE = new RealRange(ZERO, ONE);
	public final static Double SQR3 = Math.sqrt(THREE);
	
	public final static RealComparator RZERO = new RealComparator(0.0);
	public final static RealComparator REPS = new RealComparator(EPS);
	
	@Test
	public void comparatorTest() {
		RealRangeComparator comparator = new RealRangeComparator(RZERO);
		Assert.assertEquals(0, comparator.compare(ONE_TWO, ONE_TWO));
		Assert.assertEquals(1, comparator.compare(ONE_TWO, ZERO_ONE));
		Assert.assertEquals(-1, comparator.compare(ZERO_ONE, ONE_TWO));
		Assert.assertEquals(-1, comparator.compare(ZERO_ONE, null));
	}
	
	@Test
	public void comparatorTest1() {
		RealRangeComparator comparator = new RealRangeComparator(REPS);
		Assert.assertEquals(0, comparator.compare(ONE_TWO, new RealRange(ONE-EPS/2., TWO+EPS/2.)));
		// both min and max in first range are larger
		Assert.assertEquals(1, comparator.compare(ONE_TWO, new RealRange(ONE-EPS*2., TWO-EPS*2)));
		// both min and max in first range are smaller
		Assert.assertEquals(-1, comparator.compare(ONE_TWO, new RealRange(ONE+EPS*2., TWO+EPS*2)));
		// this gives -1 because there is a disagreement
		Assert.assertEquals(-1, comparator.compare(ONE_TWO, new RealRange(ONE+EPS*2., TWO-EPS*2)));
		// this gives -1 because there is a disagreement
		Assert.assertEquals(-1, comparator.compare(ONE_TWO, new RealRange(ONE-EPS*2., TWO+EPS*2)));
	}
	
	/** TreeSet works
	 * 
	 */
	@Test
	public void testTreeSet(){
		RealRangeComparator comparator = new RealRangeComparator(new RealComparator(0.0));
		Set<RealRange> set = new TreeSet<RealRange>(comparator);
		set.add(ONE_TWO);
		set.add(ZERO_ONE);
		set.add(ONE_TWO);
		set.add(new RealRange(ONE, TWO-0.01));
		Assert.assertEquals(3, set.size());
	}
	
	/** TreeSet works
	 * 
	 */
	@Test
	public void testTreeSet1(){
		RealRangeComparator comparator = new RealRangeComparator(new RealComparator(0.01));
		Set<RealRange> set = new TreeSet<RealRange>(comparator);
		set.add(ONE_TWO);
		set.add(ZERO_ONE);
		set.add(ONE_TWO);
		set.add(new RealRange(ONE, TWO-0.005));
		set.add(new RealRange(ONE+0.005, TWO-0.005));
		Assert.assertEquals(2, set.size());
	}
	
}
