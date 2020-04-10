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

package me.math;

//Written by Kyle Slattery (http://kyleslattery.com)
//Free to use and edit, so long as above line stays intact
public class VectorMath {
	private double x,y,z;
	private double magnitude;

	private VectorMath unit;

	public VectorMath(boolean isUnit, double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;

		magnitude = Math.sqrt(x * x + y * y + z * z);

		if (isUnit) {
			this.unit = this;

		} else {
			if (magnitude == 0) {
				this.unit = new VectorMath(true, 0, 0, 0);
			} else {
				this.unit = new VectorMath(true, x / magnitude, y / magnitude,
						z / magnitude);
			}
		}
	}

	public VectorMath(double x, double y, double z) {

		this(false, x, y, z);
	}

	// Accessor Methods
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public VectorMath getUnitVector() {
		return unit;
	}

	// Operations
	public VectorMath scalarMult(double scalar) {
		return new VectorMath(x * scalar, y * scalar, z * scalar);
	}

	public VectorMath add(VectorMath vec) {
		return new VectorMath(x + vec.getX(), y + vec.getY(), z + vec.getZ());
	}

	public VectorMath subtract(VectorMath v) {
		return (new VectorMath(getX() - v.getX(), getY() - v.getY(), getZ()
				- v.getZ()));
	}

	public VectorMath add(VectorMath[] vec) {
		double x2, y2, z2;
		x2 = y2 = z2 = 0;
		for (int i = 0; i < vec.length; i++) {

			x2 += vec[i].getX();
			y2 += vec[i].getY();
			z2 += vec[i].getZ();
		}

		return new VectorMath(x + x2, y + y2, z + z2);
	}

	public double dot(VectorMath vec) {
		return x*vec.getX() + y*vec.getY() + z*vec.getZ();
	}

	public double norm() {
		return Math.sqrt(Math.abs(Math.pow(getX(), 2))
						 + Math.abs(Math.pow(getY(), 2))
						 + Math.abs(Math.pow(getZ(), 2)));
	}

	public VectorMath cross(VectorMath vec) {
		double xi = y*vec.getZ() - z*vec.getY();
		double yi = z*vec.getX() - x*vec.getZ();
		double zi = x*vec.getY() - y*vec.getX();
		return new VectorMath(xi,yi,zi);

	}

    public double angle( VectorMath v)
    {
        VectorMath uHat = getUnitVector();
        VectorMath vHat = v.getUnitVector();

        if( uHat.dot(vHat) < 0.0)
        {
            VectorMath w = vHat.subtract(uHat).scalarMult(-1.0);

            return Math.PI - 2 * Math.asin(w.getMagnitude() / 2.0);
        }
        else
        {
            VectorMath w = vHat.subtract(uHat);
            return 2 * Math.asin(w.getMagnitude() / 2);
        }
    }

    public VectorMath unit() {
		double m = this.getMagnitude();
		return new VectorMath(getX() / m, getY() / m, getZ() / m);
	}

	public VectorMath minus(VectorMath vec) {
		return new VectorMath(x-vec.getX(), y-vec.getY(), z-vec.getZ());
	}

    public VectorMath fromHereToThere(VectorMath here) {
		// here + hereToThere = there
		// hereToThere = there - here
		return here.subtract(this);
	}

	public double distance(VectorMath there) {
		// here + hereToThere = there
		// hereToThere = there - here
		double dx = there.getX() - getX();
		double dy = there.getY() - getY();
		double dz = there.getZ() - getZ();

		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public String toString() {
		return "<" + x + ", " + y + ", " + z + ">";
	}
}
