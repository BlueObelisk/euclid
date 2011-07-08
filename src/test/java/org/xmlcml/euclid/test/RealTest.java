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

import org.junit.Test;
import org.xmlcml.euclid.Real;

/**
 * test Real.
 * 
 * @author pmr
 * 
 */
public class RealTest {

	/**
	 * Test method for 'org.xmlcml.euclid.Real.zeroArray(double, double[])'
	 */
	@Test
	public void testZeroArray() {
		double[] rr = new double[5];
		Real.zeroArray(5, rr);
		DoubleTestBase.assertEquals("double[] ", new double[] { 0.0, 0.0, 0.0,
				0.0, 0.0 }, rr, EPS);
	}

	/**
	 * Test method for 'org.xmlcml.euclid.Real.initArray(double, double[],
	 * double)'
	 */
	@Test
	public void testInitArray() {
		double[] rr = new double[5];
		Real.initArray(5, rr, 3.0);
		DoubleTestBase.assertEquals("double[] ", new double[] { 3.0, 3.0, 3.0,
				3.0, 3.0 }, rr, EPS);
	}

}
