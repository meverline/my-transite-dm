package me.transite.feeds.metrodc.bus

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Trip
import me.transite.feeds.metrodc.utils.Route

class Schedule(val outBound: Array[Trip], val inBound: Array[Trip]) {

}

//////////////////////////////////////////////////////////////////////////////////

object Schedule {
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def jason(route:Route):String = { 
        "http://api.wmata.com/Bus.svc/json/JRouteSchedule?" + route.toUrlString() + "&" + LicenseKey.toUrlString()
   }
  
   //////////////////////////////////////////////////////////////////////////////////
   
   def xml(route:Route):String = { 
      "http://api.wmata.com/Bus.svc/RouteSchedule?" + route.toUrlString() + "&" + LicenseKey.toUrlString()
   }
   
}