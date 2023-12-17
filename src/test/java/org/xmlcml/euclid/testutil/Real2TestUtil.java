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
package org.xmlcml.euclid.testutil;

import static org.xmlcml.euclid.EuclidConstants.S_RBRAK;

import org.junit.Assert;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.test.DoubleTestBase;

public class Real2TestUtil {
	/**
	 * equality test. true if both args not null and equal within epsilon
	 * 
	 * @param msg
	 *            message
	 * @param test
	 * @param expected
	 * @param epsilon
	 */
	
	public static void assertEquals(String msg, Real2 test, Real2 expected,
			double epsilon) {
		Assert.assertNotNull("test should not be null (" + msg + S_RBRAK, test);
		Assert.assertNotNull("expected should not be null (" + msg + S_RBRAK,
				expected);
		DoubleTestBase.assertEquals(msg, test.getXY(), expected.getXY(),
				epsilon);
	}

}
