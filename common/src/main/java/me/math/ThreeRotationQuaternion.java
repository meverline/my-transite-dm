//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyright 2009 by Russ Brasser, Mark Everline and Eric Franklin
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
//
//    Orginal C# code by Frank Levine and Paul Greene
//    Traslated to Java by Mark Everline

package me.math;


public class ThreeRotationQuaternion extends Quaternion {

    private double m_angleA;
	private double m_angleB;
	private double m_angleC;

	protected ThreeRotationQuaternion(double x0, double x1, double x2,
			double x3, double a, double b, double c) {
		super(x0, x1, x2, x3);
		m_angleA = a;
		m_angleB = b;
		m_angleC = c;
	}

	public double AngleA() {
		return m_angleA;
	}

	public double AngleB() {
		return m_angleB;
	}

	public double AngleC() {
		return m_angleC;
	}

	public static ThreeRotationQuaternion RotationZYX(double alpha, double beta,
			double gamma) {
		Quaternion quat = Quaternion.rotationZ(alpha).times(Quaternion.rotationY(beta));
				   quat = quat.times(Quaternion.rotationX(gamma));

		return new ThreeRotationQuaternion(quat.getX0(),
										   quat.getX1(),
										   quat.getX2(),
										   quat.getX3(),
										   alpha,
										   beta,
										   gamma);
	}

	public static ThreeRotationQuaternion RotationYXY(double alpha,
			double beta, double gamma) {

		double cb = Math.cos(beta / 2.0);
		double sb = Math.sin(beta / 2.0);

		double cos_a_plus_g = Math.cos(alpha / 2.0 + gamma / 2.0);
		double cos_a_minus_g = Math.cos(alpha / 2.0 - gamma / 2.0);
		double sin_a_plus_g = Math.sin(alpha / 2.0 + gamma / 2.0);
		double sin_a_minus_g = Math.sin(alpha / 2.0 - gamma / 2.0);

		double x0 = cb * cos_a_plus_g;
		double x1 = sb * cos_a_minus_g;
		double x2 = cb * sin_a_plus_g;
		double x3 = -sb * sin_a_minus_g;

		return new ThreeRotationQuaternion(x0, x1, x2, x3, alpha, beta, gamma);
	}

	public static ThreeRotationQuaternion RotationXYZ(double alpha,
			double beta, double gamma) {

		double ca = Math.cos(alpha / 2.0);
		double cb = Math.cos(beta / 2.0);
		double cc = Math.cos(gamma / 2.0);

		double sa = Math.sin(alpha / 2.0);
		double sb = Math.sin(beta / 2.0);
		double sc = Math.sin(gamma / 2.0);

		double x0 = ca * cb * cc - sa * sb * sc;
		double x1 = sa * cb * cc + ca * sb * sc;
		double x2 = ca * sb * cc - sa * cb * sc;
		double x3 = ca * cb * sc + sa * sb * cc;

		return new ThreeRotationQuaternion(x0, x1, x2, x3, alpha, beta, gamma);
	}

}
