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
import org.xmlcml.euclid.Axis.Axis2;
/**
 * 2-D double limits Contains two RealRanges. Can therefore be used to describe
 * 2-dimensional limits (for example axes of graphs, rectangles in graphics,
 * limits of a molecule, etc.)
 * <P>
 * Default is two default/invalid RealRange components. Adding points will
 * create valid ranges.
 * 
 * @author (C) P. Murray-Rust, 1996
 */
public class Real2Range implements EuclidConstants {
	/**
     * X-range
     */
    RealRange xrange;
    /**
     * Y-range
     */
    RealRange yrange;
    /**
     * constructor.
     * Creates INVALID range
     */
    public Real2Range() {
    }
    /**
     * initialise with min and max values;
     * 
     * @param xr
     * @param yr
     */
    public Real2Range(RealRange xr, RealRange yr) {
        if (xr.isValid() && yr.isValid()) {
            xrange = xr;
            yrange = yr;
        }
    }
    /** create from min corner of box and max corner
     * 
     * @param r2a
     * @param r2b
     */
    public Real2Range(Real2 r2a, Real2 r2b) {
    	double x0 = r2a.getX();
    	double x1 = r2b.getX();
    	xrange = new RealRange(Math.min(x0, x1), Math.max(x0, x1));
    	double y0 = r2a.getY();
    	double y1 = r2b.getY();
    	yrange = new RealRange(Math.min(y0, y1), Math.max(y0, y1));
    }
    
    /**
     * copy constructor
     * 
     * @param r
     */
    public Real2Range(Real2Range r) {
        if (r.isValid()) {
            xrange = new RealRange(r.xrange);
            yrange = new RealRange(r.yrange);
        }
    }

    /**
     * reads in format of toString()
     * ((a,b)(c,d))
     * @param s
     */
    public static Real2Range createFrom(String s) {
    	if (s == null) {
    		return null;
    	}
    	if (s.startsWith(S_LBRAK+S_LBRAK) && s.endsWith(S_RBRAK+S_RBRAK)) {
    		String ss = s.substring(2, s.length()-2);
    		int i = ss.indexOf(S_RBRAK);
    		int j = ss.indexOf(S_LBRAK);
    		if (i == -1 || j == -1 || i+2 != j) {
    			throw new RuntimeException("Bad Real2Range syntax: "+s);
    		}
    		RealRange xr = getRealRange(ss.substring(0, i));
    		RealRange yr = getRealRange(ss.substring(j+1));
    		if (xr == null || yr == null) {
    			throw new RuntimeException("Bad Real2Range syntax: "+s);
    		}
    		return new Real2Range(xr, yr);
    	} else {
			throw new RuntimeException("Bad Real2Range syntax: "+s);
    	}
    }
	private static RealRange getRealRange(String s) {
		RealArray xa = new RealArray(s.replaceAll(S_COMMA, S_SPACE));
		return  (xa.size() == 2)  ? new RealRange(xa.get(0), xa.get(1)) : null;
	}
    /**
     * a Real2Range is valid if both its constituent ranges are
     * 
     * @return valid
     */
    public boolean isValid() {
        return (xrange != null && yrange != null && xrange.isValid() && yrange
                .isValid());
    }
    /**
     * is equals to.
     * 
     * @param r2
     * @return tru if equal
     */
    @Deprecated
    public boolean isEqualTo(Real2Range r2) {
        if (isValid() && r2 != null && r2.isValid()) {
            return (xrange.isEqualTo(r2.xrange) && yrange.isEqualTo(r2.yrange));
        } else {
            return false;
        }
    }
    
    /**
     * is equals to.
     * 
     * @param r2
     * @return tru if equal
     */
    public boolean isEqualTo(Real2Range r2, double eps) {
        if (isValid() && r2 != null && r2.isValid()) {
            return (xrange.isEqualTo(r2.xrange, eps) && yrange.isEqualTo(r2.yrange, eps));
        } else {
            return false;
        }
    }
    /**
     * merge two ranges and take the maximum extents
     * not sure the logic is right
     * if this is INVALID then replace with r2
     * if this is VALID and r2 is INVALID return this
     * else return union of the two
     * @param r2
     * @return range
     */
    public Real2Range plus(Real2Range r2) {
        if (!isValid()) {
            if (r2 == null || !r2.isValid()) {
                return new Real2Range();
            } else {
                return new Real2Range(r2);
            }
        }
        if (r2 == null || !r2.isValid()) {
            return new Real2Range(this);
        }
        return new Real2Range(xrange.plus(r2.xrange), yrange.plus(r2.yrange));
    }
    /**
     * intersect two ranges and take the range common to both; return invalid
     * range if no overlap or either is null/invalid
     * 
     * @param r2
     * @return range
     * 
     */
    public Real2Range intersectionWith(Real2Range r2) {
        if (!isValid() || r2 == null || !r2.isValid()) {
            return new Real2Range();
        }
        RealRange xr = this.getXRange().intersectionWith(r2.getXRange());
        RealRange yr = this.getYRange().intersectionWith(r2.getYRange());
        return (xr == null || yr == null) ? null : new Real2Range(xr, yr);
    }
    
    /**
     * get xrange
     * 
     * @return range
     */
    public RealRange getXRange() {
        return xrange;
    }
    /**
     * get yrange
     * 
     * @return range
     */
    public RealRange getYRange() {
        return yrange;
    }
    
    public void setXRange(RealRange xrange) {
		this.xrange = xrange;
	}
    
	public void setYRange(RealRange yrange) {
		this.yrange = yrange;
	}
	
    /**
     * get yrange
     * 
     * @return range
     */
    public Real2 getCentroid() {
        return new Real2(xrange.getMidPoint(), yrange.getMidPoint());
    }
    
    /** gets lower left and upper right.
     * @return minx,miny ... maxx, maxy
     */
    public Real2[] getCorners() {
    	Real2[] rr = new Real2[2];
    	rr[0] = new Real2(xrange.getMin(), yrange.getMin());
    	rr[1] = new Real2(xrange.getMax(), yrange.getMax());
    	return rr;
    }
    
    /**
     * is an Real2 within a Real2Range
     * 
     * @param p
     * @return includes
     */
    public boolean includes(Real2 p) {
        if (!isValid()) {
            return false;
        }
        return (xrange.includes(p.getX()) && yrange.includes(p.getY()));
    }
    /**
     * is one Real2Range completely within another
     * 
     * @param r
     * @return includes
     */
    public boolean includes(Real2Range r) {
        if (!isValid() || r == null || !r.isValid()) {
            return false;
        }
        RealRange xr = r.getXRange();
        RealRange yr = r.getYRange();
        return (xrange.includes(xr) && yrange.includes(yr));
    }
    /**
     * add a Real2 to a range
     * 
     * @param p
     */
    public void add(Real2 p) {
        if (p == null)
            return;
        if (xrange == null)
            xrange = new RealRange();
        if (yrange == null)
            yrange = new RealRange();
        xrange.add(p.getX());
        yrange.add(p.getY());
    }
    /**
     * merge range for given axis.
     * 
     * @param ax
     *            axis
     * @param range
     */
    public void add(Axis2 ax, RealRange range) {
        if (range == null)
            return;
        if (ax.equals(Axis2.X)) {
            if (xrange == null) {
                xrange = new RealRange();
            }
            xrange = xrange.plus(range);
        }
        if (ax.equals(Axis2.Y)) {
            if (yrange == null) {
                yrange = new RealRange();
            }
            yrange = yrange.plus(range);
        }
    }

    /** gets minimum X and Y translations required to move point into range
     * uses RealRange.distanceOutside() - see this
     * @param p 
     * @return null if p == null or has bad coordinates; Real2(0,0) if in or on range, else translations to touch range
     */
    public Real2 distanceOutside(Real2 p) {
    	Real2 r2 = null;
    	if (p != null) {
	    	double dx = xrange.distanceOutside(p.getX());
	    	double dy = yrange.distanceOutside(p.getY());
	    	if (!Double.isNaN(dx) && !Double.isNaN(dy)) {
	    		r2 = new Real2(dx, dy);
	    	}
    	}
    	return r2;
    }

    /** transform range (as copy)
     * 
     * @param t2
     * @return new Range
     */
    public Real2Range getTranformedRange(Transform2 t2) {
    	RealRange xRange = this.getXRange();
    	RealRange yRange = this.getYRange();
    	Real2 xyMin = new Real2(xRange.getMin(), yRange.getMin());
    	xyMin.transformBy(t2);
    	Real2 xyMax = new Real2(xRange.getMax(), yRange.getMax());
    	xyMax.transformBy(t2);
    	return new Real2Range(xyMin, xyMax);
    }
    /**
     * to string.
     * 
     * @return string
     */
    public String toString() {
        String xx = (xrange == null) ? "NULL" : xrange.toString();
        String yy = (yrange == null) ? "NULL" : yrange.toString();
        return EC.S_LBRAK + xx + EC.S_COMMA + yy + EC.S_RBRAK;
    }
    
	public Real2Range format(Integer decimalPlaces) {
		if (xrange != null && yrange != null) {
	    	xrange = xrange.format(decimalPlaces);
	    	yrange = yrange.format(decimalPlaces);
		}
    	return this;
	}
	public static boolean isNull(Real2Range r2r) {
		return r2r == null || (r2r.getXRange() == null && r2r.getYRange() == null);
	}
}
