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


public class ECFPositionedFrame extends OrthoFrame {

	private ECFPositionedFrame m_referenceFrame = null;
	// private VectorMath m_positionRelativeToReferenceFrame;
	private ThreeRotationQuaternion m_transformToReferenceFrame;
	private Quaternion m_compositeQuaternion;
	private VectorMath m_positionECF;
	private Vertex m_positionVertex;

	public ECFPositionedFrame()
	{
	}

	public ECFPositionedFrame(VectorMath positionRelativeToReferenceFrame,
							  ThreeRotationQuaternion q) {
		init( positionRelativeToReferenceFrame, q);
	}

	protected void init(VectorMath positionRelativeToReferenceFrame,
					    ThreeRotationQuaternion q)
	{
		// m_positionRelativeToReferenceFrame = positionRelativeToReferenceFrame;
		m_transformToReferenceFrame = q;
		m_compositeQuaternion = m_transformToReferenceFrame;
		m_positionECF = positionRelativeToReferenceFrame;
		m_positionVertex = new Vertex(m_positionECF);
		resetTransform(m_compositeQuaternion);
		m_referenceFrame = this;
	}

	public ECFPositionedFrame referenceFrame() {
		return m_referenceFrame;

	}

	protected ThreeRotationQuaternion transformToReferenceFrame() {
		return m_transformToReferenceFrame;

	}

	public VectorMath positionECF() {
		return m_positionECF;
	}

	public Vertex positionVertex() {
		return m_positionVertex;
	}

	public Quaternion compositeQuaternion() {
		return m_compositeQuaternion;
	}

	public void refreshTransform() {
		resetTransform(compositeQuaternion());
	}

	/// <summary>
	/// Returns a VectorMath relative to this frame that points to the target
	// point
	/// </summary>
	/// <param name="targetPointECF"></param>
	/// <returns></returns>
	public VectorMath putECFVectorMathInLocalFrame(VectorMath targetPointECF) {

		VectorMath fromFrameToTargetECF = this.positionECF().fromHereToThere(targetPointECF);
		Quaternion transformFromECFToThisFrame = compositeQuaternion().inverse();
		VectorMath fromFrameToTarget_Local = Quaternion.rotate(transformFromECFToThisFrame,
															   fromFrameToTargetECF);

		return fromFrameToTarget_Local;
	}

	/// <summary>
	/// Returns a VectorMath relative to this frame that points to the target
	// point
	/// </summary>
	/// <param name="targetPointRelativeToFrame"></param>
	/// <returns></returns>
	public VectorMath putLocalFrameVectorMathIntoECF(
			VectorMath targetPointRelativeToFrame) {
		VectorMath targetPointLOSInECF = Quaternion.rotate(compositeQuaternion(),
														   targetPointRelativeToFrame);
		VectorMath targetPointInECF = positionECF().add(targetPointLOSInECF);
		return targetPointInECF;
	}

}
