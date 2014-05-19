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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xmlcml.euclid.Angle;
import org.xmlcml.euclid.Angle.Units;
import org.xmlcml.euclid.Line2;
import org.xmlcml.euclid.Real;
import org.xmlcml.euclid.Real2;
import org.xmlcml.euclid.Vector2;

/**
 * test for Line2
 * 
 * @author pm286
 * 
 */
public class Line2Test {

	static double sqrt2 = Math.sqrt(2.);
	static double sqrt5 = Math.sqrt(5.);
	Real2 p00 = new Real2(0., 0.);
	Real2 p02 = new Real2(0., 2.);
	Real2 p20 = new Real2(2., 0.);
	Real2 p12 = new Real2(1., 2.);
	Real2 p21 = new Real2(2., 1.);
	Real2 p11 = new Real2(1., 1.);
	Vector2 v12 = new Vector2(1., 2.);
	Line2 l0002;
	Line2 l0200;
	Line2 l0020;
	Line2 l1221;
	Line2 l1112;

	/**
	 * @throws Exception
	 * */
	@Before
	public void setUp() throws Exception {
		l0002 = new Line2(p00, p02);
		l0200 = new Line2(p02, p00);
		l0020 = new Line2(p00, p20);
		l1221 = new Line2(p12, p21);
		l1112 = new Line2(p11, v12);
	}

	/** dewisott */
	@Test
	public final void testLine2Real2Real2() {
		Assert.assertNotNull("l0002", l0002);
		Vector2 v = l0002.getVector();
		Real2Test.assertEquals("vector", new Vector2(0., 2.), v, Real.EPS);
		Real2 p = l0002.getFrom();
		Real2Test.assertEquals("from", new Real2(0., 0.), p, Real.EPS);
		p = l0002.getTo();
		Real2Test.assertEquals("to", new Real2(0., 2.), p, Real.EPS);

		Assert.assertNotNull("l1221", l1221);
		v = l1221.getVector();
		Real2Test.assertEquals("vector", new Vector2(1., -1.), v, Real.EPS);
		p = l1221.getFrom();
		Real2Test.assertEquals("from", new Real2(1., 2.), p, Real.EPS);
		p = l1221.getTo();
		Real2Test.assertEquals("to", new Real2(2., 1.), p, Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testLine2Real2Vector2() {
		Assert.assertNotNull("l1112", l1112);
		Vector2 v = l1112.getVector();
		Real2Test.assertEquals("vector", new Vector2(1., 2.), v, Real.EPS);
		Real2 p = l1112.getFrom();
		Real2Test.assertEquals("from", new Real2(1., 1.), p, Real.EPS);
		p = l1112.getTo();
		Real2Test.assertEquals("to", new Real2(2., 3.), p, Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testGetSlope() {
		double slope = l0002.getSlope();
		Assert.assertEquals("slope", Double.POSITIVE_INFINITY, slope, Real.EPS);
		slope = l0200.getSlope();
		Assert.assertEquals("slope", Double.NEGATIVE_INFINITY, slope, Real.EPS);
		slope = l0020.getSlope();
		Assert.assertEquals("slope", 0.0, slope, Real.EPS);
		slope = l1221.getSlope();
		Assert.assertEquals("slope", -1.0, slope, Real.EPS);
		slope = l1112.getSlope();
		Assert.assertEquals("slope", 2.0, slope, Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testGetYIntercept() {
		double yint = l1221.getYIntercept();
		Assert.assertEquals("yint", 3.0, yint, Real.EPS);
		yint = l1112.getYIntercept();
		Assert.assertEquals("yint", -1.0, yint, Real.EPS);
		Line2 ll = new Line2(new Real2(1., 1.), new Vector2(0., 1.));
		yint = ll.getYIntercept();
		Assert.assertEquals("yint", Double.NaN, yint, Real.EPS);
		ll = new Line2(new Real2(1., 1.), new Vector2(1., 0.));
		yint = ll.getYIntercept();
		Assert.assertEquals("yint", 1.0, yint, Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testGetXIntercept() {
		double xint = l1221.getXIntercept();
		Assert.assertEquals("xint", 3.0, xint, Real.EPS);
		xint = l1112.getXIntercept();
		Assert.assertEquals("xint", 0.5, xint, Real.EPS);
		Line2 ll = new Line2(new Real2(1., 1.), new Vector2(0., 1.));
		xint = ll.getXIntercept();
		Assert.assertEquals("xint", 1.0, xint, Real.EPS);
		ll = new Line2(new Real2(1., 1.), new Vector2(1., 0.));
		xint = ll.getXIntercept();
		Assert.assertEquals("xint", Double.NaN, xint, Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testGetIntersection() {
		Real2 p = l0002.getIntersection(l0020);
		Real2Test.assertEquals("intersect", new Real2(0., 0.), p, Real.EPS);
		p = l0002.getIntersection(l1112);
		Real2Test.assertEquals("intersect", new Real2(0.0, -1.0), p, Real.EPS);
		p = l1221.getIntersection(l1112);
		Real2Test.assertEquals("intersect", new Real2(4. / 3., 5. / 3.), p,
				Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testGetUnitVector() {
		Vector2 v = l1112.getUnitVector();
		Real2Test.assertEquals("unitv", new Real2(1. / sqrt5, 2. / sqrt5), v,
				Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testGetDistanceFromPoint() {
		double d = l1112.getDistanceFromPoint(new Real2(0., 0.));
		Assert.assertEquals("distance", 1. / sqrt5, d, Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testGetNearestPointOnLine() {
		Real2 p = l1112.getNearestPointOnLine(new Real2(0., 0.));
		Real2Test.assertEquals("point", new Real2(0.4, -0.2), p, 0.0000001);
	}

	/** dewisott */
	@Test
	public final void testGetLambda() {
		Real2 p = new Real2(0.4, -0.2);
		double lambda = l1112.getLambda(p);
		Assert.assertEquals("lambda", -0.6, lambda, Real.EPS);

		p = new Real2(0.0, -1.0);
		lambda = l1112.getLambda(p);
		Assert.assertEquals("lambda", -1.0, lambda, Real.EPS);

		lambda = l1112.getLambda(l1112.getTo());
		Assert.assertEquals("lambda", 1.0, lambda, Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testGetLength() {
		Assert.assertEquals("length", sqrt2, l1221.getLength(), Real.EPS);
	}

	/** dewisott */
	@Test
	public final void testGetMidPoint() {
		Real2Test.assertEquals("mid point", new Real2(1.5, 2), l1112
				.getMidPoint(), Real.EPS);
	}
		
	@Test
	public void testCreatePointOnLine() {
		Line2 line = new Line2(new Real2(0.,0.), new Vector2(1., 0.));
		Real2 newPoint = line.createPointOnLine(3.0);
		Real2Test.assertEquals("extend", new Real2(3.0, 0.), newPoint, 0.01);
	}

	@Test
	public void testCreatePointOnLine1() {
		Line2 line = new Line2(new Real2(1., 2.), new Real2(4., -2.));
		Real2 newPoint = line.createPointOnLine(10.0);
		Real2Test.assertEquals("extend", new Real2(7., -6.), newPoint, 0.01);
	}
	
	@Test
	public void testGetSerial() {
		double eps = 0.01;
		Line2 line = new Line2(new Real2(1., 1.), new Real2(2., 2.));
		Assert.assertEquals("from", 0, line.getSerial(new Real2(1.001, 1.001), eps));
		Assert.assertEquals("to", 1, line.getSerial(new Real2(2.001, 2.001), eps));
		Assert.assertEquals("none", -1, line.getSerial(new Real2(3.001, 3.001), eps));
	}

	@Test
	public void testGetSerial1() {
		double eps = 0.01;
		Line2 line = new Line2(new Real2(1., 1.), new Vector2(1., 1.));
		Real2Test.assertEquals("check to", new Real2(2.001, 2.002), line.getXY(1), eps);
		Assert.assertEquals("from", 0, line.getSerial(new Real2(1.001, 1.001), eps));
		Assert.assertEquals("to", 1, line.getSerial(new Real2(2.001, 2.001), eps));
		Assert.assertEquals("to", -1, line.getSerial(new Real2(3.001, 3.001), eps));
	}

	@Test
	public void testCreatePointOnLineIndex() {
		Line2 line = new Line2(new Real2(0.0, 0.0), new Real2(3., 4.));
		double dist = 10;
		Real2 point0 = line.createPointOnLine(dist, 0);
		Real2Test.assertEquals("point0 ", new Real2(6., 8.), point0, 0.01);
		Real2 point1 = line.createPointOnLine(dist, 1);
		Real2Test.assertEquals("point1 ", new Real2(-3., -4.), point1, 0.01);
	}
	
	@Test
	public void testParallelLinesAndDistances() {
		Angle epsAngle = new Angle(0.015, Angle.Units.RADIANS);
		// these lines are not quite parallel - quite a large deviation
		Line2 line0 = new Line2(new Real2(172.14,512.58), new Real2(172.14,504.3)); //v((0.0,-8.28))
		Line2 line1 = new Line2(new Real2(172.5,504.06), new Real2 (172.62,512.58)); //v((0.12,8.52))	
		Double d0 = line0.getUnsignedDistanceFromPoint(new Real2(0., 0.));
		Assert.assertEquals(172.14,  d0, 0.01);
		Double d1 = line1.getUnsignedDistanceFromPoint(new Real2(0., 0.));
		Assert.assertEquals(165.38,  d1, 0.01);
		Assert.assertTrue(line0.isAntiParallelTo(line1, epsAngle));
		Assert.assertFalse(line0.isParallelTo(line1, epsAngle));
		Assert.assertTrue(line0.isParallelOrAntiParallelTo(line1, epsAngle));
		Double dist01 = line0.calculateUnsignedDistanceBetweenLines(line1, epsAngle);
		Double dist10 = line1.calculateUnsignedDistanceBetweenLines(line0, epsAngle);
		// note how distances are not equal - the lines diverge slightly
		Assert.assertEquals(0.48, dist01, 0.001);
		Assert.assertEquals(0.36, dist10, 0.001);
		// smaller tolerance means lines are not parallel
		Assert.assertFalse(line0.isAntiParallelTo(line1, epsAngle.multiplyBy(0.1)));
		Assert.assertFalse(line0.isParallelTo(line1, epsAngle.multiplyBy(0.1)));
		Assert.assertFalse(line0.isParallelOrAntiParallelTo(line1, epsAngle.multiplyBy(0.1)));

	}
	
	@Test
	public void testIsPerpendicularTo() {
		Angle angleEps = new Angle(0.01, Units.RADIANS);
		Line2 line10 = new Line2(new Real2(0., 0.), new Real2(1.0, 0.0));
		Line2 line01 = new Line2(new Real2(0., 0.), new Real2(0.0, 0.1));
		Assert.assertTrue("perp", line01.isPerpendicularTo(line10, angleEps));
		Line2 line10d = new Line2(new Real2(0., 0.), new Real2(1.0, 0.001));
		Line2 line01d = new Line2(new Real2(0., 0.), new Real2(0.001, 1.0));
		Assert.assertTrue("perp", line01d.isPerpendicularTo(line10d, angleEps));
		// lower the tolerance
		Assert.assertFalse("perp", line01d.isPerpendicularTo(line10d, angleEps.multiplyBy(0.01)));
	}

}
