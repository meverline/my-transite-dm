package me.transite.feeds.metrodc.utils

/*
 *  XML:
 * <Line>
 *     <DisplayName>Green</DisplayName>
 *     <EndStationCode>E10</EndStationCode>
 *     <InternalDestination1/>
 *     <InternalDestination2/>
 *     <LineCode>GR</LineCode>
 *     <StartStationCode>F11</StartStationCode>
 *   </Line>
 * 
 * DisplayName - The public name (color) of the line.
 * LineCode - ID of the line. Can be RD, BL, YL, OR, GR
 * StartStationCode - The code associated with the first station on the line. 
 * 					  Stations are referenced by codes. See the Rail Stations method 
 * 					  to find station names.
 * EndStationCode - The code associated with the last station on the line.
 * InternalDestination1 - Some trains can start/finish their trips not only at the 
 * 						  first/last station, but at intermediate stations along the line.
 * InternalDestination2 - See InternalDestination2 above.
 */

class Line (val displayName:String, 
			 val endStationCode:String, 
			 val internalDestination: Array[String], 
			 val lineCode:String, 
			 val startStationCode:String) {
  
      def toUrlString() : String = {
      	"LineCode=" + lineCode
      } 

}