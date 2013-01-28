package me.transite.feeds.metrodc.utils

/*
 * XML:
 * <AIMPredictionTrainInfo>
 *	  <Car>6</Car>
 *	  <Destination>Shady Gr</Destination>
 *	  <DestinationCode>A15</DestinationCode>
 *	  <DestinationName>Shady Grove</DestinationName>
 * 	  <Group>2</Group> 
 *	  <Line>RD</Line>
 *	  <LocationCode>A10</LocationCode>
 *	  <LocationName>Medical Center</LocationName>
 * 	  <Min>1</Min>
 * 	</AIMPredictionTrainInfo>
 * 
 * Car - Number of cars in a particular train (usually 6 or 8).
 * Destination - The short name of the destination station.
 * DestinationCode - The ID of destination station.
 * DestinationName - The name of destination station.
 * Group - Track number (1 or 2).
 * Line - ID of the metro line. Can be RD, BL, YL, OR, GR
 * LocationCode - ID of the station where the train is arriving.
 * LocationName - The name of the station where the train is arriving.
 * Min - The minutes to train arrival. Can be "BRD", "ARR" or positive number.
 * 
 */

class PredictionTrainInfo (val car:Integer, 
						   val station:Station, 
						   val group:Integer, 
						   val line:String, 
						   val locationCode:String,
						   val locationName:String, 
						   val min:PredicationMinType)	{

}