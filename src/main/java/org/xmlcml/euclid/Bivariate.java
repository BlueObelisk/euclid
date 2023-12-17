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

import org.apache.log4j.Logger;

public class Bivariate {

	private final static Logger LOG = Logger.getLogger(Bivariate.class);
	
	private Real2Array real2Array;
	private Double slope;
	private Double intercept;
	private RealArray xarr;
	private RealArray yarr;
	private Double corrCoeff;
	private RealArray residuals;

	public Bivariate(Real2Array real2Array) {
		this.real2Array = real2Array;
		this.xarr = real2Array.getXArray();
		this.yarr = real2Array.getYArray();
	}
	
	public Double getSlope() {
		ensureSlope();
		return slope;
	}

	public Double getIntercept() {
		ensureSlope();
		return intercept;
	}

	private void ensureSlope() {
		if (slope == null && xarr.size() > 1) {
			double count = (double) xarr.size();
			double sigmax = xarr.sumAllElements();
			double sigmay = yarr.sumAllElements();
			double sigmaxy = xarr.sumProductOfAllElements(yarr);
			double numerator = sigmaxy - sigmax * sigmay / count;
			double sigmax2 = xarr.sumProductOfAllElements(xarr);
			double sigmay2 = yarr.sumProductOfAllElements(yarr);
			double denominator = sigmax2 - sigmax * sigmax / count;
			slope = numerator / denominator;
			intercept = sigmay / count - slope * sigmax / count; 
			corrCoeff = (count * sigmaxy - sigmax * sigmay) / 
					Math.sqrt((count * sigmax2 - sigmax * sigmax)*(count * sigmay2 - sigmay * sigmay));
		}
	}

	public Double getCorrelationCoefficient() {
		ensureSlope();
		return corrCoeff;
	}

	public RealArray getResiduals() {
		ensureSlope();
		residuals = new RealArray(xarr.size());
		for (int i = 0; i < xarr.size(); i++) {
			double deltay = yarr.elementAt(i) - (slope * xarr.elementAt(i) + intercept); 
			residuals.setElementAt(i, deltay);
		}
		return residuals;
	}
	
	public RealArray getNormalizedResiduals() {
		getResiduals();
		LOG.trace("R> "+residuals.format(2));
		Univariate univariate = new Univariate(residuals);
		RealArray normalisedResiduals = univariate.getNormalizedValues();
		LOG.trace("N> "+normalisedResiduals.format(2));
		return normalisedResiduals;
	}
}
