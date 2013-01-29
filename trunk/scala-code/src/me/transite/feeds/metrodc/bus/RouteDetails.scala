package me.transite.feeds.metrodc.bus

/*
 * Method 12: Bus Route Details
 * Description: Returns a sequence of lat/long points which can be used to describe a 
 *              specific bus route.
 * 
 * XML:
 * <RouteDetailsInfo xmlns="http://www.wmata.com" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
 *   <Direction0>
 *       <DirectionNum>0</DirectionNum>
 *       <DirectionText>WEST</DirectionText>
 *       ... Shape ... 
 *       ... Stops ...
 *   </Direction0>
 *   <Direction1>
 *       <DirectionNum>1</DirectionNum>
 *       <DirectionText>EAST</DirectionText>
 *       ... Shape ... 
 *       ... Stops ...
 *   </Direction1>
 * </RouteDetailsInfo>
 * 
 */

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Stop
import me.transite.feeds.metrodc.utils.Route
import me.utilities.Coordinate

class RouteDetails(val shape:Array[Coordinate], val stops:Array[Stop]) {
}

//////////////////////////////////////////////////////////////////////////////////

object RouteDetails {
  
   val endPoint:String = "RouteDetails?"
  
   private def toUrlQueryString(routeId:Route):String = {
      routeId.toUrlString() + "&" + LicenseKey.toUrlString()
   }
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def url(routeId:Route):String = {
      LicenseKey.BUS_SVC + LicenseKey.JASON_J  + endPoint + toUrlQueryString(routeId)
   }
 
   //////////////////////////////////////////////////////////////////////////////////
   
   def xmlUrl(routeId:Route):String = {
      LicenseKey.BUS_SVC + endPoint  + toUrlQueryString(routeId)
   }
}