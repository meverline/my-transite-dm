package me.transite.feeds.metrodc.bus

/*
 * Method 13: Bus Positions
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
  
   val endPoint:String = "BusPositions?"
     
   private def toUrlQueryString(location:Coordinate, radiusMeters:Double):String = {
      location.toMetroString() + LicenseKey.RADIUS + radiusMeters + "&" + LicenseKey.toUrlString()
   }
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def url(location:Coordinate, radiusMeters:Double):String = {
      LicenseKey.BUS_SVC + LicenseKey.JASON_J + endPoint + toUrlQueryString(location, radiusMeters)
   }
 
   //////////////////////////////////////////////////////////////////////////////////
   
   def xmlUrl(location:Coordinate, radiusMeters:Double):String = {
      LicenseKey.BUS_SVC + endPoint  + toUrlQueryString(location, radiusMeters)
   }
}