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

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.euclid.RealComparator;

public class RealComparatorTest {

	public final static Double ZERO = 0.0;
	public final static Double ONE = 1.0;
	public final static Double TWO = 2.0;
	public final static Double THREE = 3.0;
	public final static Double SQR3 = Math.sqrt(THREE);
	
	@Test
	public void comparatorTest() {
		RealComparator comparator = new RealComparator(ZERO);
		Assert.assertEquals(0, comparator.compare(ONE, ONE));
		Assert.assertEquals(1, comparator.compare(ONE, ZERO));
		Assert.assertFalse(comparator.compare(THREE, SQR3*SQR3) == 0);
		Assert.assertEquals(-1, comparator.compare(ONE, TWO));
	}
	
	@Test
	public void comparatorTest1() {
		RealComparator comparator = new RealComparator(0.01);
		Assert.assertEquals(0, comparator.compare(ONE, 1.001));
		Assert.assertEquals(0, comparator.compare(ONE, 0.999));
		Assert.assertEquals(0, comparator.compare(THREE, SQR3*SQR3));
		Assert.assertEquals(1, comparator.compare(ONE, 0.98));
		Assert.assertEquals(-1, comparator.compare(ONE, 1.02));
	}
	
	/** HashSet only works with exactness
	 * 
	 */
	@Test
	public void testHashSet(){
		Set<Double> set = new HashSet<Double>();
		set.add(1.0);
		set.add(1.0);
		Assert.assertEquals(1, set.size());
	}
	
	/** HashSet only works with exactness
	 * 
	 */
	@Test
	public void testHashSet1(){
		Set<Double> set = new HashSet<Double>();
		set.add(1.0);
		set.add((Math.sqrt(3.0)*Math.sqrt(3.0))/3.0);
		Assert.assertEquals(2, set.size());
	}
	
	/** TreeSet works
	 * 
	 */
	@Test
	public void testTreeSet(){
		RealComparator comparator = new RealComparator(0.0);
		Set<Double> set = new TreeSet<Double>(comparator);
		set.add(ONE);
		set.add(ONE);
		set.add(THREE);
		Assert.assertEquals(2, set.size());
	}
	
	/** TreeSet works
	 * 
	 */
	@Test
	public void testTreeSet1(){
		RealComparator comparator = new RealComparator(0.0);
		Set<Double> set = new TreeSet<Double>(comparator);
		set.add(ONE);
		set.add(ONE-0.001);
		set.add(THREE);
		Assert.assertEquals(3, set.size());
	}
	
	/** TreeSet works
	 * 
	 */
	@Test
	public void testTreeSet2(){
		RealComparator comparator = new RealComparator(0.01);
		Set<Double> set = new TreeSet<Double>(comparator);
		set.add(ONE);
		set.add(ONE - 0.001);
		set.add(THREE);
		Assert.assertEquals(2, set.size());
	}
	
	/** TreeSet works
	 * 
	 */
	@Test
	public void testTreeSet3(){
		RealComparator comparator = new RealComparator(0.01);
		Set<Double> set = new TreeSet<Double>(comparator);
		set.add(ONE - 0.001);
		set.add(ONE);
		set.add(THREE);
		Assert.assertEquals(2, set.size());
	}
	
	/** TreeSet works
	 * 
	 */
	@Test
	public void testTreeSetContains(){
		RealComparator comparator = new RealComparator(0.01);
		Set<Double> set = new TreeSet<Double>(comparator);
		set.add(ONE - 0.001);
		set.add(ONE);
		set.add(THREE);
		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.contains(1.0));
		Assert.assertTrue(set.contains(0.995));
	}
	
}
