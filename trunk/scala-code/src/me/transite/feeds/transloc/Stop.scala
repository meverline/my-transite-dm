package me.transite.feeds.transloc

/*
 * Transloc Version 1.1 Agenecies api inside Data array
 * 
 * URL
 *   http://api.transloc.com/version/stops.json
 * Rate Limit
 *   Can be requested every 10 seconds from the same IP address.
 * Required Parameters
 *   agencies - a list of agency IDs which to retreive
 * Optional Parameters
 *   geo_area - a geographical area filter (see geo_area)
 * 
 * {
 *   "api_current_version": "1.1",
 *   "api_version": "1.1",
 *   "generated_on": "2011-01-10T15:47:50+00:00",
 *   "expires_in": 3600,
 *   "rate_limit": 10,
 *   "data": [
 *       {
 *           "stop_id": "stop_id",
 *           "agency_id": "agency_id",
 *           "name": "Livingston Ave",
 *           "description": "A very high brow stop.",
 *           "code": "livingston",
 *           "url": "http://www.example.com/livingston-ave",
 *           "location_type": "stop", # One of ['stop', 'station']
 *           "location": {
 *               "lat": -78.637339999999995,
 *               "lng": 35.777357000000002
 *           },
 *           "routes": [
 *               route_id
 *           ]
 *       }
 *   ]
 *}
 * 
 */

import me.utilities.Coordinate
import me.transite.feeds.transloc.utils.LocationType


class Stop(val stop_id:String, 
		   val agency_id:String, 
		   val name:String, 
		   val description:String, 
		   val code:String,
		   val url:String,
		   val locType:LocationType,
		   val coord:Coordinate,
		   val routes:Array[String]) {
  
     def toUrlString() : String = {
       stop_id
   } 

}
