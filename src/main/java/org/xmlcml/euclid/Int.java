package org.xmlcml.euclid;

import org.apache.log4j.Logger;

/**
 * Int supports various utilities for integers Use Integer where you want a
 * first-class Java object
 * 
 * @author (C) P. Murray-Rust, 1996
 */
public abstract class Int implements EuclidConstants {
    final static Logger logger = Logger.getLogger(Int.class);
    /**
     * set an array to zero
     * 
     * @param nelem
     * @param arr
     */
    public static void zeroArray(int nelem, int[] arr) {
        for (int i = 0; i < nelem; i++) {
            arr[i] = 0;
        }
    }
    /**
     * set an array to given value
     * 
     * @param nelem
     * @param arr
     * @param f
     */
    public static void initArray(int nelem, int[] arr, int f) {
        for (int i = 0; i < nelem; i++) {
            arr[i] = f;
        }
    }
    /**
     * print a int[]
     * 
     * @param a
     * 
     */
    public static void printArray(int[] a) {
        for (int i = 0; i < a.length; i++) {
            logger.info(a[i] + EC.S_SPACE);
        }
        logger.info("");
    }
	/**
	 * tests equality of int arrays. arrays must be of same length
	 * 
	 * @param a
	 *            first array
	 * @param b
	 *            second array
	 * @return array elements equal
	 */
	public static boolean equals(int[] a, int[] b) {
		boolean result = false;
		if (a.length == b.length) {
			result = true;
			for (int i = 0; i < a.length; i++) {
				if (a[i] != b[i]) {
					result = false;
					break;
				}
			}
		}
		return result;
	}
	/**
	 * compare integer arrays.
	 * 
	 * @param a
	 * @param b
	 * @return message or null
	 */
	public static String testEquals(int[] a, int[] b) {
		String s = null;
		if (a == null) {
			s = "a is null";
		} else if (b == null) {
			s = "b is null";
		} else if (a.length != b.length) {
			s = "unequal arrays: " + a.length + S_SLASH + b.length;
		} else {
			for (int i = 0; i < a.length; i++) {
				if (a[i] != b[i]) {
					s = "unequal element (" + i + "), " + a[i] + " != " + b[i];
					break;
				}
			}
		}
		return s;
	}
	/**
	 * compare arrays.
	 * 
	 * @param a
	 * @param b
	 * @return message or null if equal
	 */
	public static String testEquals(int[][] a, int[][] b) {
		String s = null;
		if (a == null) {
			s = "a is null";
		} else if (b == null) {
			s = "b is null";
		} else if (a.length != b.length) {
			s = "unequal arrays: " + a.length + S_SLASH + b.length;
		} else {
			for (int i = 0; i < a.length; i++) {
				if (a[i].length != b[i].length) {
					s = "row (" + i + ") has unequal lengths: " + a[i].length
							+ S_SLASH + b[i].length;
					break;
				}
				for (int j = 0; j < a[i].length; j++) {
					if (a[i][j] != b[i][j]) {
						s = "unequal element at (" + i + ", " + j + "), ("
								+ a[i][j] + " != " + b[i][j] + S_RBRAK;
						break;
					}
				}
			}
		}
		return s;
	}
    
    
    
}
