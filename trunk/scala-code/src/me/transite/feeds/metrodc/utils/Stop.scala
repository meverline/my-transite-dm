package me.transite.feeds.metrodc.utils

/*
 * XML
 * 
 * <Stop>
 *     <Lat>38.87835555</Lat>
 *     <Lon>-76.99037803</Lon>
 *     <Name>K ST + POTOMAC AVE</Name>
 *     <Routes xmlns:a="http://schemas.microsoft.com/2003/10/Serialization/Arrays">
 *       <a:string>V7</a:string>
 *       <a:string>V7c</a:string>
 *       <a:string>V7v1</a:string>
 *       <a:string>V8</a:string>
 *       <a:string>V9</a:string>
 *     </Routes>
 *     <StopID>1000533</StopID>
 * </Stop>
 * 
 * StopID - Regional BusStop ID
 * Lat - Latitude of the BusStop
 * Lon - Longitude of the BusStop
 * Name - The name of the BusStop
 * Routes - Array of Routes for this stop.
 */
import me.utilities.Coordinate

class Stop(val location:Coordinate, 
		   val name:String,
		   val id:String) {

	  var routes:Array[Route] = new Array[Route](0);
	  
	  def this(location:Coordinate, name:String, id:String, list:Array[Route]) {
         this(location, name, id)
         routes = list;
	  }
  
      def toUrlString() : String = {
      	"StopID=" + id
      } 

}
