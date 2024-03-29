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
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.euclid.Real2RangeComparator;
import org.xmlcml.euclid.RealComparator;
import org.xmlcml.euclid.RealRange;
import org.xmlcml.euclid.RealRangeComparator;

public class Real2RangeComparatorTest {

	public final static Double ZERO = 0.0;
	public final static Double EPS = 0.01;
	public final static Double ONE = 1.0;
	public final static Double TWO = 2.0;
	public final static Double THREE = 3.0;
	public final static RealRange ONE_TWO = new RealRange(ONE, TWO);
	public final static RealRange ZERO_ONE = new RealRange(ZERO, ONE);
	public final static Real2Range ONE_TWO_ONE_TWO = new Real2Range(ONE_TWO, ONE_TWO);
	public final static Real2Range ZERO_ONE_ONE_TWO = new Real2Range(ZERO_ONE, ONE_TWO);
	public final static Real2Range ONE_TWO_ZERO_ONE = new Real2Range(ONE_TWO, ZERO_ONE);
	public final static Double SQR3 = Math.sqrt(THREE);
	
	public final static RealComparator RZERO = new RealComparator(0.0);
	public final static RealComparator REPS = new RealComparator(EPS);
	public final static RealRangeComparator RRZERO = new RealRangeComparator(RZERO);
	public final static RealRangeComparator RREPS = new RealRangeComparator(REPS);
	
	@Test
	public void testDummy() {
		
	}
	@Test
	public void comparatorTest() {
		Real2RangeComparator comparator = new Real2RangeComparator(RRZERO);
		Assert.assertEquals(0, comparator.compare(ONE_TWO_ONE_TWO, ONE_TWO_ONE_TWO));
		Assert.assertEquals(-1, comparator.compare(ZERO_ONE_ONE_TWO, ONE_TWO_ONE_TWO));
		Assert.assertEquals(-1, comparator.compare(ONE_TWO_ONE_TWO, ZERO_ONE_ONE_TWO));
		Assert.assertEquals(-1, comparator.compare(ONE_TWO_ONE_TWO, ONE_TWO_ZERO_ONE));
	}
	
	@Test
	public void comparatorTest1() {
		Real2RangeComparator comparator = new Real2RangeComparator(RREPS);
		Assert.assertEquals(0, comparator.compare(ONE_TWO_ONE_TWO, new Real2Range(ONE_TWO, new RealRange(ONE-EPS/2., TWO+EPS/2.))));
		// both min and max in first range are larger
		Assert.assertEquals(-1, comparator.compare(ONE_TWO_ONE_TWO, new Real2Range(new RealRange(ONE-EPS*2., TWO-EPS*2.), ONE_TWO)));
		// both min and max in first range are smaller
		Assert.assertEquals(-1, comparator.compare(ONE_TWO_ONE_TWO, new Real2Range(new RealRange(ONE-EPS*2., TWO-EPS*2.), ONE_TWO)));
		// this gives -1 because there is a disagreement
		Assert.assertEquals(-1, comparator.compare(ONE_TWO_ONE_TWO, new Real2Range(new RealRange(ONE-EPS*2., TWO+EPS*2.), ONE_TWO)));
	}
	
	/** TreeSet works
	 * 
	 */
	@Test
	public void testTreeSet(){
		Real2RangeComparator comparator = new Real2RangeComparator(RRZERO);
		Set<Real2Range> set = new TreeSet<Real2Range>(comparator);
		set.add(ONE_TWO_ONE_TWO);
		set.add(ZERO_ONE_ONE_TWO);
		set.add(ONE_TWO_ONE_TWO);
		set.add(new Real2Range(ONE_TWO, new RealRange(ONE, TWO-0.001)));
		Assert.assertEquals(3, set.size());
	}
	
	/** TreeSet works
	 * 
	 */
	@Test
	public void testTreeSet1(){
		Real2RangeComparator comparator = new Real2RangeComparator(RREPS);
		Set<Real2Range> set = new TreeSet<Real2Range>(comparator);
		set.add(ONE_TWO_ONE_TWO);
		set.add(ZERO_ONE_ONE_TWO);
		set.add(ONE_TWO_ONE_TWO);
		set.add(new Real2Range(ONE_TWO, new RealRange(ONE, TWO-0.001)));
		set.add(new Real2Range(new RealRange(ONE, TWO-0.001), ONE_TWO));
		Assert.assertEquals(2, set.size());
	}
	
}
