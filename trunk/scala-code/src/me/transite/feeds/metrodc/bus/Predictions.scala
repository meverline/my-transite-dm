package me.transite.feeds.metrodc.bus

/*
 * Description: Returns the bus arrival predictions for a specific bus stop according to the 
 *              real-time positions of the buses.
 * 
 * XML:
 * <NextBusResponse xmlns="http://www.wmata.com" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
 *    <Predictions>
 *      ..... NextBusPrediction
 *    </Predictions>
 *    <StopName>29th St + #2745</StopName>
 * </NextBusResponse>
 */
import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.NextBusPrediction
import me.transite.feeds.metrodc.utils.Stop

class Predictions( val stopName:String, 
				   val list:Array[NextBusPrediction]) {

}

//////////////////////////////////////////////////////////////////////////////////

object Predictions {
  
   private def toUrlQueryString(stopId:Stop):String = {
      stopId.toUrlString() + "&" + LicenseKey.toUrlString()
   }
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def jason(stopId:Stop):String = {
      "http://api.wmata.com/Rail.svc/json/JPredictions?" + toUrlQueryString(stopId)
   }
 
   //////////////////////////////////////////////////////////////////////////////////
   
   def xml(stopId:Stop):String = {
      "http://api.wmata.com/Rail.svc/Predictions?"  + toUrlQueryString(stopId)
   }
}