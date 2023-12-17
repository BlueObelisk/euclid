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

public class Line2AndReal2Calculator {

	public double distanceOfProjectionFromEnd1;
	public double distanceOfProjectionFromEnd2;
	public double minimumDistance;
	public boolean offEnd1;
	public boolean offEnd2;
	
	public Line2AndReal2Calculator(Line2 line, Real2 point) {
		Real2 point1 = line.getXY(0);
		Real2 point2 = line.getXY(1);
		Real2 proj = line.getNearestPointOnLine(point);
		/*double dist = arrowLine.getUnsignedDistanceFromPoint(corners[j]);
		if (dist < minDistanceToLine) {
			minDistanceToLine = dist;
		}*/
		distanceOfProjectionFromEnd1 = proj.getDistance(point1);
		distanceOfProjectionFromEnd2 = proj.getDistance(point2);
		if (distanceOfProjectionFromEnd1 > line.getLength() && distanceOfProjectionFromEnd1 > distanceOfProjectionFromEnd2) {
			minimumDistance = point.getDistance(point2);
			offEnd2 = true;
		} else if (distanceOfProjectionFromEnd2 > line.getLength() && distanceOfProjectionFromEnd2 > distanceOfProjectionFromEnd1) {
			minimumDistance = point.getDistance(point1);
			offEnd1 = true;
		} else {
			minimumDistance = point.getDistance(proj);
		}
	}
	
}