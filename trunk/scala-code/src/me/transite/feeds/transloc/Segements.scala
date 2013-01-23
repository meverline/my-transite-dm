package me.transite.feeds.transloc

import me.utilities.Coordinate

/*
 * Transloc Version 1.1 Agenecies api inside Data array
 * 
 * URL
 *   http://api.transloc.com/version/segments.json
 * Rate Limit
 *   Can be requested every 10 seconds from the same IP address.
 * Required Parameters
 *   agencies - a list of agency IDs which to retreive
 * Optional Parameters
 *   routes - a list of route IDs which to retreive
 *   geo_area - a geographical area filter (see geo_area)
 * 
 * {
 *   "api_current_version": "1.1",
 *   "api_version": "1.1",
 *   "generated_on": "2011-01-10T15:47:50+00:00",
 *   "expires_in": 3600,
 *   "rate_limit": 10,
 *   "data": {
 *       "segment_id": "encoded polyline"
 *   }
 * }
*/
class Segements (val segementId:String, val pologon: Array[Coordinate]) {

   
}

//////////////////////////////////////////////////////////////////////////////////

object Segements {
   def url(agences:Array[Agency]):String = {  "http://api.transloc.com/1.1/segments.json" }
}