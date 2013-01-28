package me.transite.feeds.metrodc.bus

/*
 * Description: Returns the real-time positions of each bus travel a specified route inside
 *              specified area.  Bus position information is updated every two minutes or less.
 *  XML:
 * 
 * <BusPositionsResp xmlns="http://www.wmata.com" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
 *   <BusPositions>
 *    	.... BusPosition ...
 *   </BusPositions>
 * </BusPositionsResp>
 */

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.BusPosition
import me.utilities.Coordinate

class Positions(val busLocation:Array[BusPosition]) {
}


//////////////////////////////////////////////////////////////////////////////////

object Positions {
  
   private def toUrlQueryString(location:Coordinate, radiusMeters:Double):String = {
      location.toUrlString() + "&radius=" + radiusMeters + "&" + LicenseKey.toUrlString()
   }
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def jason(location:Coordinate, radiusMeters:Double):String = {
      "http://api.wmata.com/Rail.svc/json/JBusPositions?" + toUrlQueryString(location, radiusMeters)
   }
 
   //////////////////////////////////////////////////////////////////////////////////
   
   def xml(location:Coordinate, radiusMeters:Double):String = {
      "http://api.wmata.com/Rail.svc/BusPositions?"  + toUrlQueryString(location, radiusMeters)
   }
}