//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright © 2009 by Russ Brasser, Mark Everline and Eric Franklin
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <http://www.gnu.org/licenses/>.

package me.math;


/*************************************************************************
 * Copyright © 2007, Robert Sedgewick and Kevin Wayne.
 * Last updated: Tue Sep 29 16:17:41 EDT 2009. 
 * 
 *  Source Copied/Taken from
 *  http://www.cs.princeton.edu/introcs/32class/Quaternion.java.html
 *
 *  Compilation:  javac Quaternion.java
 *  Execution:    java Quaternion
 *
 *  Data type for quaternions.
 *
 *  http://mathworld.wolfram.com/Quaternion.html
 *
 *  The data type is "immutable" so once you create and initialize
 *  a Quaternion, you cannot change it.
 *
 *  Copied/Taken from
 *  http://www.cs.princeton.edu/introcs/32class/Quaternion.java.html
 *
 *  % java Quaternion
 *
 *************************************************************************/

public class Quaternion {

    private double x0, x1, x2, x3;

	// create a new object with the given components
	public Quaternion(double x0, double x1, double x2, double x3) {
		this.x0 = x0;
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
	}

	// create a new object with the given components
	public Quaternion(double x0, VectorMath vec) {
		this.x0 = x0;
		this.x1 = vec.getX();
		this.x2 = vec.getY();
		this.x3 = vec.getZ();
	}

	protected void setX0(double x0) {
		this.x0 = x0;
	}

	protected void setX1(double x1) {
		this.x1 = x1;
	}

	protected void setX2(double x2) {
		this.x2 = x2;
	}

	protected void setX3(double x3) {
		this.x3 = x3;
	}

	public double getX0() {
		return x0;
	}

	public double getX1() {
		return x1;
	}

	public double getX2() {
		return x2;
	}

	public double getX3() {
		return x3;
	}

	// return a string representation of the invoking object
	public String toString() {
		return x0 + " + " + x1 + "i + " + x2 + "j + " + x3 + "k";
	}

	// return the quaternion norm
	public double norm() {
		return Math.sqrt(x0 * x0 + x1 * x1 + x2 * x2 + x3 * x3);
	}

	// return the quaternion conjugate
	public Quaternion conjugate() {
		return new Quaternion(x0, -x1, -x2, -x3);
	}

	// return a new Quaternion whose value is (this + b)
	public Quaternion plus(Quaternion b) {
		Quaternion a = this;
		return new Quaternion(a.x0 + b.x0, a.x1 + b.x1, a.x2 + b.x2, a.x3
				+ b.x3);
	}

	// return a new Quaternion whose value is (this * b)
	public Quaternion times(Quaternion b) {
		Quaternion a = this;
		double y0 = a.x0 * b.x0 - a.x1 * b.x1 - a.x2 * b.x2 - a.x3 * b.x3;
		double y1 = a.x0 * b.x1 + a.x1 * b.x0 + a.x2 * b.x3 - a.x3 * b.x2;
		double y2 = a.x0 * b.x2 - a.x1 * b.x3 + a.x2 * b.x0 + a.x3 * b.x1;
		double y3 = a.x0 * b.x3 + a.x1 * b.x2 - a.x2 * b.x1 + a.x3 * b.x0;
		return new Quaternion(y0, y1, y2, y3);

	}

	// return a new Quaternion whose value is the inverse of this
	public Quaternion inverse() {
		double d = x0 * x0 + x1 * x1 + x2 * x2 + x3 * x3;
		return new Quaternion(x0 / d, -x1 / d, -x2 / d, -x3 / d);
	}

	// return a / b
	public Quaternion divides(Quaternion b) {
		Quaternion a = this;
		return a.inverse().times(b);
	}

	public Quaternion normalize() {
		double norm = norm();
		return new Quaternion(getX0() / norm, getX1() / norm, getX2() / norm,
				getX3() / norm);
	}

	public VectorMath vectorPart() {
		return new VectorMath(getX1(), getX2(), getX3());
	}

	public static Quaternion conjugate(Quaternion q) {
		return new Quaternion(q.getX0(), -q.getX1(), -q.getX2(), -q.getX3());
	}

	public static VectorMath rotate(Quaternion rotationQuaternion,
			VectorMath vectorToRotate) {
		Quaternion v = rotationQuaternion.times(new Quaternion(0, vectorToRotate));
		Quaternion r = v.times(Quaternion.conjugate(rotationQuaternion));
		return r.vectorPart();
	}

	public static Quaternion rotationAxis(VectorMath axis, double angleRadians) {
		Quaternion quat = new Quaternion(Math.cos(angleRadians / 2.0), axis
				.unit().scalarMult(Math.sin(angleRadians / 2.0)));
		quat.normalize();
		return quat;
	}

	public static Quaternion rotationAxis(VectorMath from, VectorMath to,
			double angleRadians) {
		VectorMath rotationAxis = from.cross(to);
		return Quaternion.rotationAxis(rotationAxis, angleRadians);
	}

	public static Quaternion rotationX(double angleRadians) {
		return rotationAxis(new VectorMath(1, 0, 0), angleRadians);
	}

	public static Quaternion rotationY(double angleRadians) {
		return rotationAxis(new VectorMath(0, 1, 0), angleRadians);
	}

	public static Quaternion rotationZ(double angleRadians) {
		return rotationAxis(new VectorMath(0, 0, 1), angleRadians);
	}

	// sample client for testing
	public static void main(String[] args) {
		Quaternion a = new Quaternion(3.0, 1.0, 0.0, 0.0);

		System.out.println("a = " + a);
		Quaternion b = new Quaternion(0.0, 5.0, 1.0, -2.0);
		System.out.println("b = " + b);
		System.out.println("norm(a)  = " + a.norm());
		System.out.println("conj(a)  = " + a.conjugate());
		System.out.println("a + b    = " + a.plus(b));
		System.out.println("a * b    = " + a.times(b));
		System.out.println("b * a    = " + b.times(a));
		System.out.println("a / b    = " + a.divides(b));
		System.out.println("a^-1     = " + a.inverse());
		System.out.println("a^-1 * a = " + a.inverse().times(a));
		System.out.println("a * a^-1 = " + a.times(a.inverse()));
	}

}


