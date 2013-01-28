package me.transite.feeds.metrodc.bus

/*
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
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def jason():String = { 
     "http://api.wmata.com/Bus.svc/json/JRoutes?" + LicenseKey.toUrlString() 
   }
  
   //////////////////////////////////////////////////////////////////////////////////
   
   def xml():String = { 
     "http://api.wmata.com/Bus.svc/Routes?" + LicenseKey.toUrlString()  
   }
   
}