package org.xmlcml.euclid.test;

import org.junit.Assert;
import org.junit.Test;
import org.xmlcml.euclid.Int;

/**
 * test Int.
 * 
 * @author pmr
 * 
 */
public class IntTest {

	/**
	 * Test method for 'org.xmlcml.euclid.Int.zeroArray(int, int[])'
	 */
	@Test
	public void testZeroArray() {
		int[] ii = new int[5];
		Int.zeroArray(5, ii);
		String s = Int.testEquals((new int[] { 0, 0, 0, 0, 0 }), ii);
		if (s != null) {
			Assert.fail("int[] " + "; " + s);
		}
	}

	/**
	 * Test method for 'org.xmlcml.euclid.Int.initArray(int, int[], int)'
	 */
	@Test
	public void testInitArray() {
		int[] ii = new int[5];
		Int.initArray(5, ii, 3);
		String s = Int.testEquals((new int[] { 3, 3, 3, 3, 3 }), ii);
		if (s != null) {
			Assert.fail("int[] " + "; " + s);
		}
	}

}
