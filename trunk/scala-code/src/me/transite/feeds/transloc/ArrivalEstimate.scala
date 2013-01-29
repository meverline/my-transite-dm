package me.transite.feeds.transloc


/*
 * Transloc Version 1.1 Agenecies api inside Data array
 * 
 * URL
 *   http://api.transloc.com/version/arrival-estimates.json
 * Required Parameters
 *	 agencies - a list of agency IDs for which to retrieve the arrival estimates
 * Optional Parameters
 *	 routes - a list of route IDs for which to retrieve the arrival estimates
 *	 stops - a list of stop IDs for which to retrieve the arrival estimates
 * 
 *  {
 *   "api_current_version": "1.1",
 *   "api_version": "1.1",
 *   "generated_on": "2011-01-10T15:47:50+00:00",
 *   "expires_in": 1,
 *   "rate_limit": 5,
 *   "data": [
 *       {
 *           "agency_id": "agency_id",
 *           "stop_id": "stop_id",
 *           "arrivals": [
 *               {
 *                   "vehicle_id": "vehicle_id",
 *                   "route_id": "route_id",
 *                   "arrival_at": "2011-01-10T16:16:21-05:00",
 *                   "type": "vehicle-based"
 *               },
 *               {
 *                   "vehicle_id": "vehicle_id",
 *                   "route_id": "route_id",
 *                   "arrival_at": "2011-01-10T16:26:25-05:00",
 *                   "type": "vehicle-based"
 *               },
 *               {
 *                   "vehicle_id": null, # Schedule-based arrival estimates do not have vehicle_ids.
 *                   "route_id": "route_id",
 *                   "arrival_at": "2011-01-10T16:36:38-05:00",
 *                   "type": "schedule-based"
 *               }
 *           ]
 *       }
 *   ]
 * }
*/

import me.transite.feeds.transloc.utils.TimeEstimate

class ArrivalEstimate(val agency:Agency, 
					  val stopId:String, 
					  val expiresIn:Integer,
					  val arrivals:Array[TimeEstimate]) {
 
}

//////////////////////////////////////////////////////////////////////////////////

object ArrivalEstimate {
   
   val endPoint:String = "arrival-estimates.json?agencies="
  
   def url(agences:Array[Agency]):String = { 
       TransLoc.URL + endPoint + agences.mkString(",")
   }
   
   //////////////////////////////////////////////////////////////////////////////////

   def url(agences:Array[Agency], routes:Array[Route]):String = { 
       TransLoc.URL + endPoint + agences.mkString(",") + TransLoc.toRoutesCgiList(routes)
   }
   
   //////////////////////////////////////////////////////////////////////////////////

   def url(agences:Array[Agency], routes:Array[Route], stops:Array[Stop]):String = { 
       TransLoc.URL + endPoint + agences.mkString(",") + TransLoc.toRoutesCgiList(routes) +
    		   	TransLoc.toStopsCgiList(stops) 
   }
   
   //////////////////////////////////////////////////////////////////////////////////

   def url(agences:Array[Agency], stops:Array[Stop]):String = { 
       TransLoc.URL + endPoint + agences.mkString(",")  + TransLoc.toStopsCgiList(stops)
   }
}