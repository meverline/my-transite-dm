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

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonRootName(value = "LocalDownFrame")
public class LocalDownFrame extends ECFPositionedFrame{

	public enum RelativePositionOrder {
		NORTH_THEN_EAST,
		EAST_THEN_NORTH
	}
	
	private Vertex position;
	
	public LocalDownFrame() {	
	}
	
	/**
	 * @return the position
	 */
	@JsonGetter("position")
	public Vertex getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	@JsonSetter("position")
	public void setPosition(Vertex position) {
		this.position = position;
		init(position.getEcfFromLatLon(),
				 ThreeRotationQuaternion.RotationYXY(-Math.PI / 2.0,
						 							 position.getLongitudeRadians(),
						 							 -position.getLatitudeRadians()));
	}



	/// <summary>
	/// Creates a LocalDownFrame from a GeoPoint
	/// </summary>
	/// <param name="position"></param>
	public LocalDownFrame(Vertex position) {
		this.position = position;
		init(position.getEcfFromLatLon(),
			 ThreeRotationQuaternion.RotationYXY(-Math.PI / 2.0,
					 							 position.getLongitudeRadians(),
					 							 -position.getLatitudeRadians()));
	}

	/// <summary>
	/// Creates a LocalDownFrame from an ECF Vector
	/// </summary>
	/// <param name="positionECF"></param>
	public LocalDownFrame(VectorMath positionECF) {
		position = new Vertex(positionECF);
		init(positionECF,
			 ThreeRotationQuaternion.RotationYXY(-Math.PI / 2.0,
					 							 position.getLongitudeRadians(),
					 							 -position.getLatitudeRadians()));
	}

	/// <summary>
	/// Creates a LocalDownFrame from a Lat,Lon,Alt Triple
	/// </summary>
	/// <param name="latDegrees"></param>
	/// <param name="lonDegrees"></param>
	/// <param name="altMeters"></param>
	public LocalDownFrame(double latDegrees, double lonDegrees) {
		position = new Vertex(latDegrees, lonDegrees);
		init(position.getEcfFromLatLon(),
			 ThreeRotationQuaternion.RotationYXY(-Math.PI / 2.0,
					 							 position.getLongitudeRadians(),
					 							 -position.getLatitudeRadians()));
	}

	public double angleInRadiansForArcLength(double distance, double magnitude) {
		return distance/magnitude;
	}

	/// <summary>
	/// Gets a position relative to this frame specified by the distance north and east to travel.
	/// </summary>
	/// <param name="northDistanceMeters"></param>
	/// <param name="eastDistanceMeters"></param>
	/// <param name="order"></param>
	/// <returns></returns>
	@JsonIgnore
	public VectorMath getRelativePosition(double northDistanceMeters,
									      double eastDistanceMeters, 
									      RelativePositionOrder order) {

		double ecaNorth = angleInRadiansForArcLength(northDistanceMeters,
													 positionECF().getMagnitude());
		double ecaEast = angleInRadiansForArcLength(eastDistanceMeters,
													positionECF().getMagnitude());

		VectorMath finalPosition;
		if (order == RelativePositionOrder.EAST_THEN_NORTH) {
			LocalDownFrame intermediateFrame =
				new LocalDownFrame(Quaternion.rotate(eastRotationQuaternion(ecaEast),
								   					 positionECF() ));

			finalPosition = Quaternion.rotate(intermediateFrame.northRotationQuaternion(ecaNorth),
											  intermediateFrame.positionECF());
		} else {
			LocalDownFrame intermediateFrame =
				new LocalDownFrame(Quaternion.rotate(northRotationQuaternion(ecaNorth),
								   					 positionECF() ));

			Quaternion rotator = eastRotationAtLatitudeQuaternion(intermediateFrame.positionVertex().getLatitudeRadians(),
																  eastDistanceMeters);
			finalPosition = Quaternion.rotate(rotator,
											  intermediateFrame.positionECF());

		}

		return finalPosition;
	}

	/// <summary>
	/// Returns a Quaternion that will rotate you northward by the angle specified
	/// </summary>
	/// <param name="ecaAngleRadians"></param>
	/// <returns></returns>
	public Quaternion northRotationQuaternion(double ecaAngleRadians) {
		return Quaternion.rotationAxis(getYAxis(), -ecaAngleRadians);
	}

	/// <summary>
	/// Returns a Quaternion that will rotate you eastward by the angle specifed
	/// </summary>
	/// <param name="ecaAngleRadians"></param>
	/// <returns></returns>
	public Quaternion eastRotationQuaternion(double ecaAngleRadians) {
		return Quaternion.rotationAxis(getXAxis(), ecaAngleRadians);
	}

	/// <summary>
	/// Returns a quaternion that will rotate you eastward around the Z axis at a specified latitude
	/// by a specified ground range
	/// </summary>
	/// <param name="latRadians"></param>
	/// <param name="groundRangeMeters"></param>
	/// <returns></returns>
	public static Quaternion eastRotationAtLatitudeQuaternion(
			double latRadians, double groundRangeMeters) {
		double radiusAdjustedForLatitude = Math.cos(latRadians)
				* EarthConstants.EquatorialRadiusMeters();

		double longitudeToRotate = groundRangeMeters
				/ radiusAdjustedForLatitude;

		return Quaternion.rotationZ(longitudeToRotate); // Rotate around ECF Z to keep
		// The Latitude Constant

	}
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////

	public static class DynamoConvert implements DynamoDBTypeConverter<String, LocalDownFrame>
	{
		private final ObjectMapper mapper = new ObjectMapper();
		@Override
		public String convert(LocalDownFrame crossCovData) {
			try {
				return mapper.writeValueAsString(crossCovData);
			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException(e);
			}
		}

		@Override
		public LocalDownFrame unconvert(String s) {
			try {
				return mapper.readValue(s, LocalDownFrame.class);
			} catch (JsonProcessingException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}


}
