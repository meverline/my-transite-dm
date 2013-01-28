package me.transite.feeds.metrodc.rail

import me.transite.feeds.metrodc.LicenseKey
import me.transite.feeds.metrodc.utils.Line

/*
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
  
   //////////////////////////////////////////////////////////////////////////////////
  
   def jason():String = {  
      "http://api.wmata.com/Rail.svc/json/JLines?" + LicenseKey.toUrlString() 
   }
   
   //////////////////////////////////////////////////////////////////////////////////
   
   def xml():String = { 
     "http://api.wmata.com/Rail.svc/Lines?" + LicenseKey.toUrlString()
   }
   
}