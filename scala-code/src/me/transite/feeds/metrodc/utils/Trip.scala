package me.transite.feeds.metrodc.utils

/*
 *  XML
 *  <Trip>
 *     <DirectionNum>0</DirectionNum>
 *     <EndTime>2010-10-26T17:27:00</EndTime>
 *     <RouteID>16L</RouteID>
 *     <StartTime>2010-10-26T16:50:00</StartTime>
 *        ... StopTime ...
 *     <TripDirectionText>WEST</TripDirectionText>
 *     <TripHeadsign>ANNANDALE</TripHeadsign>
 *     <TripID>10968_11</TripID>
 *  <Trip> 
 * 
 *  TripID - Identifier of the trip
 *  StartTime - Start of the trip
 *  EndTime - End of the trip
 *  RouteID - Identifier of the route.
 *  TripDirectionText - Name of the direction.
 *  TripHeadsign - Trip headsign.
 * 
 */

import java.util.Calendar

class Trip (val direction:String, 
			val headSign:String, 
			val route:Route,
			val startTime:Calendar, 
			val endTime:Calendar, 
			val stops:Array[StopTime],
			val id:String ) {

}