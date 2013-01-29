package me.transite.feeds.metrodc.bus

/*
 * Method 9: Bus Routes
 * Description: Returns a list of all bus routes.
 * 
 * XML
 *  <RoutesResp xmlns="http://www.wmata.com" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
 *    <Routes>
 *      .... Route ....
 *    </Routes>
 * </RoutesResp>
 * 
 */

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Route

class Routes(val list:Array[Route]) {
}

//////////////////////////////////////////////////////////////////////////////////

object Routes {
  
   val endPoint = "Routes?"
     
   //////////////////////////////////////////////////////////////////////////////////
  
   def jason():String = { 
     LicenseKey.BUS_SVC +"json/J" + endPoint + LicenseKey.toUrlString() 
   }
  
   //////////////////////////////////////////////////////////////////////////////////
   
   def xml():String = { 
     LicenseKey.BUS_SVC + endPoint + LicenseKey.toUrlString()  
   }
   
}