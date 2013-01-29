package me.transite.feeds.metrodc.rail

/*
 * Method 5: Rail Station Prediction
 * Description: Returns train arrival information as it appears on the 
 *              Public Information Displays throughout the system.
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
  
   val endPoint:String = "GetPrediction/"
  
   private def toUrlQueryString(list:Array[Station]):String = {
      list.mkString(",") + "?" + LicenseKey.key;
   }
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def url(list:Array[Station]):String = {
      LicenseKey.RAIL_SVC + LicenseKey.JASON + "/" + endPoint + toUrlQueryString(list)
   }
 
   //////////////////////////////////////////////////////////////////////////////////
   
   def xmlUrl(list:Array[Station]):String = {
      LicenseKey.RAIL_SVC + endPoint  + toUrlQueryString(list)
   }
}