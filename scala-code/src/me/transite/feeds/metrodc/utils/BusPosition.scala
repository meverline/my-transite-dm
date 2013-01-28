package me.transite.feeds.metrodc.utils

/*
 * XML:
 * 
 * <BusPosition>
 *     <DateTime>2010-10-27T13:18:41</DateTime>
 *     <Deviation>-7.9833333333</Deviation>
 *     <DirectionNum>0</DirectionNum>
 *     <DirectionText>NORTH</DirectionText>
 *     <Lat>38.869629</Lat>
 *     <Lon>-77.053001</Lon>
 *     <RouteID>10A</RouteID>
 *     <TripHeadsign>PENTAGON</TripHeadsign>
 *     <TripID>19283_11</TripID>
 *     <TripStartTime>1753-01-01T12:30:00</TripStartTime>
 *     <VehicleID>4366</VehicleID>
 * </BusPosition>
 * 
 * RouteID - Route identifier.
 * DateTime - Time of last known bus report.
 * Deviation - Deviation from the schedule (in minutes). Negative deviation denotes delay.
 * DirectionNum - Direction on the route. 0 or 1.
 * Lat - Latitude.
 * Lon - Longitude.
 * TripHeadsign - Trip headsign.
 * TripID - Identifier of the Trip.
 * TripStartTime - Start Time of the trip.
 * VehicleID - Identifier of the bus.
 * 
 */

import me.utilities.Coordinate

class BusPosition(val dateTime:String, 
				  val deviation:Double, 
				  val direction:Integer,
				  val location:Coordinate, 
				  val routeId:String,
				  val headSign:String, 
				  val tripId:String, 
				  val tripStartTime:String, 
				  val vehicleId:Integer)	 {

}