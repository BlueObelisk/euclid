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

import junit.framework.Assert;

import org.junit.Test;
import org.xmlcml.euclid.Bivariate;
import org.xmlcml.euclid.Real2Array;
import org.xmlcml.euclid.RealArray;

public class BivariateTest {

	@Test
	public void testBivariate() {
		Real2Array r2a = new Real2Array(
				new RealArray(new double[]{1.47, 1.50, 1.52, 1.55, 1.57, 1.60, 1.63, 1.65, 1.68, 1.70, 1.73, 1.75, 1.78, 1.80, 1.83}),
				new RealArray(new double[]{52.21, 53.12, 54.48, 55.84, 57.20, 58.57, 59.93, 61.29, 63.11, 64.47, 66.28, 68.10, 69.92, 72.19, 74.46})
				);
		Bivariate bivariate = new Bivariate(r2a);
		Double slope = bivariate.getSlope();
		Assert.assertEquals("slope", 61.272, slope, 0.001);
		Double intercept = bivariate.getIntercept();
		Assert.assertEquals("intercept", -39.062, intercept, 0.001);
		Double r = bivariate.getCorrelationCoefficient();
		Assert.assertEquals("r", 0.9945, r, 0.001);
	}
}
