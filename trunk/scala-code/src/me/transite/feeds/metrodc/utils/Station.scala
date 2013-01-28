package me.transite.feeds.metrodc.utils

import me.utilities.Coordinate

/*
 * XML:
 * <Station>
 *     <Code>C02</Code>
 *     <Lat>38.9013327968</Lat>
 *     <LineCode1>BL</LineCode1>
 *     <LineCode2>OR</LineCode2>
 *     <LineCode3 i:nil="true" />
 *     <LineCode4 i:nil="true" />
 *     <Lon>-77.0336341721</Lon>
 *     <Name>McPherson Square</Name>
 *     <StationTogether1 />
 *     <StationTogether2 />
 *   </Station>
 * 
 * Code - The code associated with a specific station.
 * Name - The name of the station.
 * Lat - The latitude of the station.
 * Lon - The longitude of the station.
 * LineCode1 - The code that indicates which line the station is associated with. 
 * 			   Can be RD, BL, YL, OR, GR
 * LineCode2 - If a station serves two lines this field will indicate the code of the second line.
 * LineCode3 - Unused.
 * LineCode4 - Unused.
 * StationTogether1 - Due to the presence of multi-level track platforms, the Metro Center,
 * 					  L'Enfant Plaza, Gallery Pl / Chinatown and Fort Totten locations have 
 * 					  two station codes associated with them. In the case of any of these 
 * 					  stations, StationTogether1 will return the alternative code
 * 					  for the station.
 * StationTogether2 - Unused.
 */
class Station(val code:String, 
			  val location:Coordinate, 
			  val lineCode:Array[String], 
			  val name:String,
			  val stationTogether:Array[String] ) {
  
     def toUrlString() : String = {
      	"LineCode=" + code
     }
     
     override def toString() : String = {
        code
     }

}