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

/** comparator for use with TreeSet<Double> and other tools which normally require equals().
 * epsilon is initially set to zero, so only exact equality matches
 * 
 * @author pm286
 *
 */
public class RealComparator implements Comparator<Double> {

	private double epsilon = 0.0d;

	public RealComparator(double eps) {
		this.setEpsilon(eps);
	}

	/**
	 * if Math.abs(d0-d1) {@literal <}= epsilon
	 * return -1 if either arg is null
	 */
	public int compare(Double d0, Double d1) {
		if (d0 == null || d1 == null) {
			return -1;
		}
		double delta = Math.abs(d0 - d1);
		if (delta <= epsilon) {
			return 0;
		}
		return (d0 < d1) ? -1 : 1;
	}
	
	/** set the tolerance
	 * negative values are converted to positive
	 * @param epsilon
	 */
	public void setEpsilon(double epsilon) {
		this.epsilon = Math.abs(epsilon);
	}

}
