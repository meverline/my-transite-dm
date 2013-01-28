package me.transite.feeds.metrodc.bus

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Stop
import me.utilities.Coordinate

class Stops(val stops:Array[Stop]) {
}

//////////////////////////////////////////////////////////////////////////////////

object Stops {
    
   //////////////////////////////////////////////////////////////////////////////////////
 
   private def toUrlQueryString(location:Coordinate, radiusMeters:Double):String = {
       "&" + location.toUrlString + "&radius=" + radiusMeters + "&" + LicenseKey.toUrlString()
   }
   
   //////////////////////////////////////////////////////////////////////////////////////
 
   def jason(location:Coordinate, radiusMeters:Double):String = {  
      "http://api.wmata.com/Bus.svc/json/JStops?" + toUrlQueryString(location, radiusMeters)
   }
   
   //////////////////////////////////////////////////////////////////////////////////////
   
   def xml(location:Coordinate, radiusMeters:Double):String = {  
       "http://api.wmata.com/Bus.svc/Stops?" + toUrlQueryString(location, radiusMeters)  
   }
   
}