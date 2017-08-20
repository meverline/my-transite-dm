//	  CIRAS: Crime Information Retrieval and Analysis System
//    Copyrightx 2009 by Russ Brasser, Mark Everline and Eric Franklin
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


public class OrthoFrame {

	protected VectorMath m_x;
	protected VectorMath m_y;
	protected VectorMath m_z;

	public VectorMath getXAxis() {
		return m_x;
	}

	public void setXAxis(VectorMath x) {
		m_x = x;
	}

	public VectorMath getYAxis() {
		return m_y;
	}

	public void setYAxis(VectorMath x) {
		m_y = x;
	}

	public VectorMath getZAxis() {
		return m_z;
	}

	public void setZAxis(VectorMath x) {
		m_z = x;
	}

	public OrthoFrame() {
		m_x = new VectorMath(1, 0, 0);
		m_y = new VectorMath(0, 1, 0);
		m_z = new VectorMath(0, 0, 1);
	}

	public OrthoFrame(VectorMath x, VectorMath y, VectorMath z)

	{
		m_x = x;
		m_y = y;
		m_z = z;
	}

	public void resetTransform()

	{
		m_x = new VectorMath(1, 0, 0);
		m_y = new VectorMath(0, 1, 0);
		m_z = new VectorMath(0, 0, 1);
	}

	public void resetTransform(Quaternion q)

	{
		resetTransform();
		applyTransform(q);
	}

	public void applyTransform(Quaternion transformQuat)

	{
		m_x = Quaternion.rotate(transformQuat, m_x);
		m_y = Quaternion.rotate(transformQuat, m_y);
		m_z = Quaternion.rotate(transformQuat, m_z);
	}

}
