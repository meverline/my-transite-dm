package me.transite.feeds.metrodc.rail

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Station
import me.transite.feeds.metrodc.utils.Line

/*
 * Method 2: Rail Stations
 * Description: Returns list of all stations in the system or all stations by line.
 * 
 * XML
 *  <StationsResp xmlns="http://www.wmata.com" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
 * 	   <Stations>
 *     </Stations>
 *  </StationsResp>
 */

class Stations(val stations:Array[Station]) {

}

//////////////////////////////////////////////////////////////////////////////////

object Stations {
  
   val endPoint:String = "Stations?"
  
   //////////////////////////////////////////////////////////////////////////////////
  
   private def toUrlQueryString(railLine:Line):String = {
       railLine match {
         case a:Any => railLine.toUrlString() +  "&" + LicenseKey.toUrlString()
         case _ => LicenseKey.toUrlString()
       }  	
   }

   //////////////////////////////////////////////////////////////////////////////////
  
   def jason(railLine:Line ):String = {
      LicenseKey.RAIL_SVC + "json/J" + endPoint + toUrlQueryString(railLine)
   }
   
   //////////////////////////////////////////////////////////////////////////////////
  
   def xml(railLine:Line ):String = { 
      LicenseKey.RAIL_SVC + endPoint + toUrlQueryString(railLine)
   }
}