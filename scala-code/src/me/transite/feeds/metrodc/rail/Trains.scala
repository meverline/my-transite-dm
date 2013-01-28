package me.transite.feeds.metrodc.rail

/*
 *  Description: Returns train arrival information as it appears on the 
 *               Public Information Displays throughout the system.
 * 
 *  XML:
 *   <AIMPredictionResp xmlns="http://www.wmata.com" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
 *      <Trains>
 *          .. AIMPredictionTrainInfo ..
 *      </Trains>
 *   </AIMPredictionResp>
 */

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.PredictionTrainInfo
import me.transite.feeds.metrodc.utils.Station

class Trains (val predictions: Array[PredictionTrainInfo]){

}

//////////////////////////////////////////////////////////////////////////////////

object Trains {
  
   private def toUrlQueryString(list:Array[Station]):String = {
      list.mkString(",") + "?" + LicenseKey.key;
   }
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def jason(list:Array[Station]):String = {
      "http://api.wmata.com/Rail.svc/json/GetPrediction/" + toUrlQueryString(list)
   }
 
   //////////////////////////////////////////////////////////////////////////////////
   
   def xml(list:Array[Station]):String = {
      "http://api.wmata.com/Rail.svc/GetPrediction/"  + toUrlQueryString(list)
   }
}