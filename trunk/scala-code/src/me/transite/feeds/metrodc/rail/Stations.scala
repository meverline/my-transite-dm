package me.transite.feeds.metrodc.rail

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Station
import me.transite.feeds.metrodc.utils.Line

/*
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
  
   //////////////////////////////////////////////////////////////////////////////////
  
   private def toUrlQueryString(railLine:Line):String = {
       railLine match {
         case a:Any => railLine.toUrlString() +  "&" + LicenseKey.toUrlString()
         case _ => "&" + LicenseKey.toUrlString()
       }  	
   }

   //////////////////////////////////////////////////////////////////////////////////
  
   def jason(railLine:Line ):String = {
       "http://api.wmata.com/Rail.svc/json/JStations" + toUrlQueryString(railLine)
   }
   
   //////////////////////////////////////////////////////////////////////////////////
  
   def xml(railLine:Line ):String = { 
      "http://api.wmata.com/Rail.svc/Stations" + toUrlQueryString(railLine)
   }
}