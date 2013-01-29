package me.transite.feeds.metrodc.bus

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Trip
import me.transite.feeds.metrodc.utils.Route

/*
 * Method 11: Bus Schedule by Route
 * Description: Returns the bus schedule associated with a requested route.
 * 
 * <RouteScheduleInfo xmlns="http://www.wmata.com" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
 *   <Direction0>
 *      ... Trip ... 
 *   </Direction0>
 *   <Direction1>
 *      ... Trip ... 
 *   </Direction1>
 *   <Name>16L - 16L ANN-SKY CITY-PENT (521)</Name>
 * </RouteScheduleInfo>
 * 
 */

class Schedule(val outBound: Array[Trip], val inBound: Array[Trip]) {

}

//////////////////////////////////////////////////////////////////////////////////

object Schedule {
  
   val endPoint = "RouteSchedule?"
     
   //////////////////////////////////////////////////////////////////////////////////
  
   def jason(route:Route):String = { 
        LicenseKey.BUS_SVC +"json/J" + endPoint + route.toUrlString() + "&" + LicenseKey.toUrlString()
   }
  
   //////////////////////////////////////////////////////////////////////////////////
   
   def xml(route:Route):String = { 
      LicenseKey.BUS_SVC + endPoint + route.toUrlString() + "&" + LicenseKey.toUrlString()
   }
   
}