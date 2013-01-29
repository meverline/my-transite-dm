package me.transite.feeds.metrodc.bus

/*
 * Method 10: Bus Stops
 * Description: Returns the list of all bus stops. If a latitude/ longitude or radius is 
 * 				not provided or equals 0, all stops will be returned.  Radius is 
 * 				expressed in meters.  Stops are ordered by distance from latitude/ longitude, 
 * 				if provided.
 * 
 * XML:
 * 
 * <StopsResp xmlns="http://www.wmata.com" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
 *    <Stops>
 *      ... Stop...
 *    </Stops>
 * </StopsResp>
 * 
 */

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Stop
import me.utilities.Coordinate

class Stops(val stops:Array[Stop]) {
}

//////////////////////////////////////////////////////////////////////////////////

object Stops {
  
   val endPoint:String = "Stops?"
    
   //////////////////////////////////////////////////////////////////////////////////////
 
   private def toUrlQueryString(location:Coordinate, radiusMeters:Double):String = {
       "&" + location.toMetroString() + LicenseKey.RADIUS + radiusMeters + "&" + LicenseKey.toUrlString()
   }
   
   //////////////////////////////////////////////////////////////////////////////////////
 
   def jason(location:Coordinate, radiusMeters:Double):String = {  
      LicenseKey.BUS_SVC +"json/J" + endPoint + toUrlQueryString(location, radiusMeters)
   }
   
   //////////////////////////////////////////////////////////////////////////////////////
   
   def xml(location:Coordinate, radiusMeters:Double):String = {  
       LicenseKey.BUS_SVC + endPoint + toUrlQueryString(location, radiusMeters)  
   }
   
}