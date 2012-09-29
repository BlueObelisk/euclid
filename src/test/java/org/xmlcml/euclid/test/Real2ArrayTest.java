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

import static org.xmlcml.euclid.EC.EPS;
import static org.xmlcml.euclid.EC.S_PIPE;
import static org.xmlcml.euclid.EC.S_SPACE;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.euclid.EC;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Real2Array;
import org.xmlcml.euclid.Real2Range;
import org.xmlcml.euclid.RealArray;
import org.xmlcml.euclid.RealRange;

/**
 * test Real2Array
 * 
 * @author pmr
 * 
 */
public class Real2ArrayTest {

	Real2Array ra0;

	Real2Array ra1;

	/**
	 * setup.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		ra0 = new Real2Array();
		ra1 = new Real2Array(new RealArray(new double[] { 1, 2, 3, 4, 5, 6 }),
				new RealArray(new double[] { 11, 12, 13, 14, 15, 16 }));
	}

	/**
	 * Test method for 'org.xmlcml.euclid.Real2Array.Real2Array()'
	 */
	@Test
	public void testReal2Array() {
		Assert.assertEquals("empty", "()", ra0.toString());
	}

	/**
	 * Test method for 'org.xmlcml.euclid.Real2Array.getRange2()'
	 */
	@Test
	public void testGetRange2() {
		Real2Range real2Range = ra1.getRange2();
		Assert.assertTrue("range2", real2Range.isEqualTo(new Real2Range(
				new RealRange(1, 6), new RealRange(11, 16)), 0.001));
	}

	/**
	 * Test method for 'org.xmlcml.euclid.Real2Array.Real2Array(RealArray,
	 * RealArray)'
	 */
	@Test
	public void testReal2ArrayRealArrayRealArray() {
		Assert.assertEquals("realArrays", EC.S_LBRAK + "(1.0,11.0)" + "(2.0,12.0)"
				+ "(3.0,13.0)" + "(4.0,14.0)" + "(5.0,15.0)" + "(6.0,16.0)"
				+ EC.S_RBRAK, ra1.toString());
	}

	/**
	 * Test method for 'org.xmlcml.euclid.Real2Array.getXArray()'
	 */
	@Test
	public void testGetXArray() {
		RealArray xarr = ra1.getXArray();
		Assert.assertTrue("getXArray", xarr.isEqualTo(new RealArray(
				new double[] { 1., 2., 3., 4., 5., 6. })));
	}

	/**
	 * Test method for 'org.xmlcml.euclid.Real2Array.getYArray()'
	 */
	@Test
	public void testGetYArray() {
		RealArray yarr = ra1.getYArray();
		Assert.assertTrue("getYArray", yarr.isEqualTo(new RealArray(
				new double[] { 11., 12., 13., 14., 15., 16. })));
	}

	/**
	 * Test method for 'org.xmlcml.euclid.Real2Array.size()'
	 */
	@Test
	public void testSize() {
		Assert.assertEquals("size", 6, ra1.size());
	}

	@Test
	public void testCreateFromPairs() {
		String s = "1,2 3,4 5,6 7,8";
		Real2Array real2Array = Real2Array.createFromPairs(s, EC.S_COMMA+S_PIPE+S_SPACE);
		Assert.assertEquals("size", 4, real2Array.size());
		RealArray xarr = real2Array.getXArray();
		Assert.assertTrue("getXArray", xarr.isEqualTo(new RealArray(
				new double[] { 1., 3., 5., 7. })));
		RealArray yarr = real2Array.getYArray();
		Assert.assertTrue("getYArray", yarr.isEqualTo(new RealArray(
				new double[] { 2., 4., 6., 8. })));
	}

	/**
	 * Test method for 'org.xmlcml.euclid.Real2Array.elementAt(int)'
	 */
	@Test
	public void testElementAt() {
		Real2 real2 = ra1.elementAt(4);
		Assert.assertEquals("elementAt", 5., real2.getX(), EPS);
		Assert.assertEquals("elementAt", 15., real2.getY(), EPS);
	}
	@Test
	public void testCreateFromCoords() {
		String coords = "((112.559,238.695)(121.217,238.695)(129.215,238.695)(139.877,238.695)(146.543,238.695)(149.543,238.695)(153.533,238.695)(160.295,238.695)(167.621,238.695)(176.279,238.695)(182.876,238.695)(186.836,238.695)(197.498,238.695)(204.164,238.695)(208.154,238.695)(211.199,238.695)(219.863,238.695)(223.199,238.695)(227.879,238.695)(230.879,238.695)(236.189,238.695)(241.499,238.695)(244.817,238.695)(250.127,238.695)(256.109,238.695)(259.091,238.695)(262.091,238.695)(266.069,238.695)(272.051,238.695)(276.029,238.695)(279.029,238.695))"	;
		Real2Array real2Array = Real2Array.createFromCoords(coords);
		Assert.assertNotNull("coords", real2Array);
		Assert.assertEquals("coords", 31, real2Array.size());
		
		Assert.assertTrue("coords 0, found: "+real2Array.get(0), new Real2(112.559,238.695).isEqualTo(real2Array.get(0), 0.001));
		
				
	}
}
