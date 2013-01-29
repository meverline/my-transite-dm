package me.transite.feeds.metrodc.rail

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Line

/*
 * Method 1: Rail Lines
 * Description: Returns descriptive information about all rail lines.
 *  
 * XML:
 *  <LinesResp xmlns="http://www.wmata.com" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
 *    <Lines>
 *       1..N Line
 *    </Lines>
*  </LinesResp>
 */
class Lines(val railLines:Array[Line]) {
}

//////////////////////////////////////////////////////////////////////////////////

object Lines {
  
   val endPoint:String = "Lines?"
     
   //////////////////////////////////////////////////////////////////////////////////
  
   def url():String = {  
     LicenseKey.RAIL_SVC + LicenseKey.JASON_J  + endPoint + LicenseKey.toUrlString() 
   }
   
   //////////////////////////////////////////////////////////////////////////////////
   
   def xmlUrl():String = { 
     LicenseKey.RAIL_SVC + endPoint + LicenseKey.toUrlString()
   }
   
}