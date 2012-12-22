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

	//    public void SetAngles(double a, double b, double c)
	//    {
	//        m_angleA = a;
	//        m_angleB = b;
	//        m_angleC = c;
	//        m_myInitializer.init(this, a, b, c);
	//    }

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
		// / q = cos(a) [ 0 sin(a) 0 ]
		// / r = cos(b) [ sin(b) 0 0 ]
		// / s = cos(g) [ 0 sin(g) 0 ]
		// /
		// / qr = cos(a)cos(b) - 0 Scalar Part
		// / 	= [cos(a)sin(b) 0 0 ] q0[R]
		// / 	+ [0 sin(a)cos(b) 0 ] r0[Q]
		// / 	+ [0 0 -sin(a)sin(b)] QxR
		// / 	= [cos(a)sin(b) sin(a)cos(b) -sin(a)sin(b)]
		// /
		// / qr = cos(a)cos(b) [cos(a)sin(b) sin(a)cos(b) -sin(a)sin(b)]
		// / s = cos(g) [0 sin(g) 0 ]
		// /
		// / qrs = cos(a)cos(b)cos(g) - sin(a)cos(b)sin(g)
		// / -----------------------------------------------------------
		// / 	 = [0 cos(a)cos(b)sin(g) 0 ]
		// / 	 + [cos(a)sin(b)cos(g) sin(a)cos(b)cos(g) -sin(a)sin(b)cos(g)]
		// / 	 + [sin(a)sin(b)sin(g) 0 cos(a)sin(b)sin(g)]
		// / 	 = [sin(b)(cos(a)cos(g)+sin(a)sin(g) cos(b)(cos(a)sin(g) +
		// 			sin(a)cos(g)) -sin(b)(sin(a)cos(g)-cos(a)sin(g))
		// /
		// / 	 = cos(b)cos(a-g) [sin(b)cos(a+g) cos(b)sin(a+g) -sin(b)sin(a-g)
		// /

		// Sum formulas for sine and cosine
		// sin (s + t) = sin s cos t + cos s sin t
		// cos (s + t) = cos s cos t – sin s sin t
		// Difference formulas for sine and cosine
		// sin (s – t) = sin s cos t – cos s sin t
		// cos (s – t) = cos s cos t + sin s sin t

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
		// / q = cos(a) + [sin(a) 0 0 ]
		// / r = cos(b) + [0 sin(b) 0 ]
		// / s = cos(c) + [0 0 sin(c) ]
		// /
		// / qr[s] = cos(a)cos(b) - 0
		// / qr[v] = [0 cos(a)sin(b) 0 ]
		// / 		 [sin(a)cos(b) 0 0 ]
		// / 		 [0 0 sin(a)sin(b)]
		// / 	   = [sin(a)cos(b) cos(a)sin(b) sin(a)sin(b)]
		// /
		// / qr = cos(a)cos(b) [sin(a)cos(b) cos(a)sin(b) sin(a)sin(b)]
		// / s  = cos(c) [0 0 sin(c) ]
		// /
		// / qrs[s] = cos(a)cos(b)cos(c) - sin(a)sin(b)sin(c)
		// / qrs[v] = [0 0 cos(a)cos(b)sin(c)]
		// / 		  [sin(a)cos(b)cos(c) cos(a)sin(b)cos(c) sin(a)sin(b)cos(c)]
		// / 		  [cos(a)sin(b)sin(c) -sin(a)cos(b)sin(c) 0 ]
		// / [1] = [sin(a)cos(b)cos(c) + cos(a)sin(b)sin(c)]
		// / [2] = [cos(a)sin(b)cos(c) - sin(a)cos(b)sin(c)]
		// / [3] = [cos(a)cos(b)sin(c) + sin(a)sin(b)cos(c)]
		// /

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
