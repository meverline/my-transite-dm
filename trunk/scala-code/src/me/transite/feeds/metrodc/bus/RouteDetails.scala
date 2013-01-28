package me.transite.feeds.metrodc.bus

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Stop
import me.transite.feeds.metrodc.utils.Route
import me.utilities.Coordinate

class RouteDetails(val shape:Array[Coordinate], val stops:Array[Stop]) {
}

//////////////////////////////////////////////////////////////////////////////////

object RouteDetails {
  
   private def toUrlQueryString(routeId:Route):String = {
      routeId.toUrlString() + "&" + LicenseKey.toUrlString()
   }
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def jason(routeId:Route):String = {
      "http://api.wmata.com/Rail.svc/json/JRouteDetails?" + toUrlQueryString(routeId)
   }
 
   //////////////////////////////////////////////////////////////////////////////////
   
   def xml(routeId:Route):String = {
      "http://api.wmata.com/Rail.svc/RouteDetails?"  + toUrlQueryString(routeId)
   }
}