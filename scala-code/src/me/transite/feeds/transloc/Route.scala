package me.transite.feeds.transloc

import me.transite.feeds.transloc.utils.RouteType
import me.transite.feeds.transloc.utils.GeoArea

/*
 * Transloc Version 1.1 Agenecies api inside Data array
 * 
 * URL
 *  http://api.transloc.com/version/routes.json
 * Rate Limit
 *   Can be requested every 10 seconds from the same IP address.
 * Required Parameters
 *   agencies - a list of agency IDs which to retrieve
 * Optional Parameters
 *    geo_area - a geographical area filter (see geo_area)
 * 
 *   {
 *   "api_current_version": "1.1",
 *   "api_version": "1.1",
 *   "generated_on": "2011-01-10T15:47:50+00:00",
 *   "expires_in": 3600,
 *   "rate_limit": 10,
 *   "data": {
 *       "agency_id": [
 *           {
 *               "route_id": "route_id",
 *               "long_name": "17 Mile Drive",
 *               "short_name": "17mi dr",
 *               "abbreviation": "17midr",
 *               "type": "funicular", # Can be one of ['light_rail', 'subway',
 *                                    # 'rail', 'bus', 'ferry', 'cable_car', 'gondola', 'funicular']
 *               "color": "cc0000",
 *               "description": "A very long and beautiful funicular ride.",
 *               "url": "http://www.example.com/17-mile-ride",
 *               "segments": [
 *                   "segment_id"
 *               ],
 *               "stops": [
 *                   "stop_id
 *               ]
 *           }
 *       ]
 *    }
 *  }
 * 
*/

class Route (val id:String, 
			 val name:String, 
			 val shortName:String, 
			 val routeType:RouteType, 
			 val description:String,
			 val segments: Array[String],
			 val stops: Array[String]) {
  
     override def toString() : String = {
        id
     } 
     
}

//////////////////////////////////////////////////////////////////////////////////

object Route {
  
   val endPoint:String = "routes.json?agencies="
     
   def url(agences:Array[Agency]):String = { 
        TransLoc.URL + endPoint + agences.mkString(",") 
   }
   
   //////////////////////////////////////////////////////////////////////////////////
   
   def url(agences:Array[Agency], area:GeoArea):String = { 
       TransLoc.URL + endPoint + agences.mkString(",") + "&" + area.toUrlString()
   }
   
}