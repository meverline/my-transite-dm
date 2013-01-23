package me.transite.feeds.transloc

/*
 * Transloc Version 1.1 Agenecies api inside Data array
 * 
 * URL
 *     http://api.transloc.com/version/vehicles.json
 * Rate Limit
 *   Can be requested every 1 seconds from the same IP address.
 * Required Parameters
 *   agencies - a list of agency IDs for which to retrieve the vehicles
 * Optional Parameters
 *   routes - a list of route IDs for which to retrieve the vehicles
 *   geo_area - a geographical area filter (see geo_area)
 * 
 * {
 *   "api_current_version": "1.1",
 *   "api_version": "1.1",
 *   "generated_on": "2011-01-10T15:47:50+00:00",
 *   "expires_in": 1,
 *   "rate_limit": 1,
 *   "data": {
 *       "agency_id": [
 *           {
 *               "vehicle_id": "vehicle_id",
 *               "call_name": "Gondola #777",
 *               "description": "Senator's private funicular, named after the 777
 *                   votes by which he won the first election.",
 *               "tracking_status": "up",
 *               "heading": 35,
 *               "location": {
 *                   "lat": -78.637339999999995,
 *                   "lng": 35.777357000000002
 *               },
 *               "arrival_estimates": [
 *                   {
 *                       "stop_id": "stop_id",
 *                       "route_id": "route_id",
 *                       "arrival_at": "2011-01-10T16:16:21-05:00",
 *                   }
 *               ],
 *               "route_id": "route_id",
 *               "segment_id": "segment_id",
 *               "speed": 40.0, # in kilometers per hour
 *               "last_updated_on": "2011-01-07T21:29:56-05:00"
 *           },
 *           {
 *               "vehicle_id": "vehicle_id",
 *               "call_name": "Gondola #787",
 *               "description": "Some other vehicle.",
 *               "tracking_status": "down", # vehicle is serving but location is unknown.
 *               "heading": null, # Location is unknown.
 *               "location": null, # Location is unknown.
 *               "arrival_estimates": [ ],
 *               "route_id": "route_id",
 *               "segment_id": "segment_id",
 *               "speed": null, # Location is unknown.
 *               "last_updated_on": "2011-01-07T21:29:56-05:00"
 *           },
 *
 *       ]
 *   }
 *}
 */

import me.utilities.Coordinate
import me.transite.feeds.transloc.utils.TimeEstimate
import me.transite.feeds.transloc.utils.TrackingStatus

class Vehicles( val id:String, 
				val name:String, 
				val descript:String, 
				val status:TrackingStatus, 
				val heading:Integer, 
				val location:Coordinate,
				val esitmates:Array[TimeEstimate],
				val route:String,
				val segment:String,
				val speed:String,
				val lastUpdate:String) {

}

//////////////////////////////////////////////////////////////////////////////////

object Vehicles {
   def url(agences:Array[Agency]):String = { 
     "http://api.transloc.com/1.1/vehicles.json" 
   }
}