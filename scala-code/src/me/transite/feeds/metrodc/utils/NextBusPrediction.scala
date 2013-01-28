package me.transite.feeds.metrodc.utils

/*
 * XML:
 * 
 * <NextBusPrediction>
 *     <DirectionNum>0</DirectionNum>
 *     <DirectionText>East to Stadium - Armory</DirectionText>
 *     <Minutes>11</Minutes>
 *     <RouteID>96</RouteID>
 *     <VehicleID>2162</VehicleID>
 *  </NextBusPrediction>
 * 
 * RouteID - identifier of the route.
 * DirectionNum - trip direction on the route (0 or 1).
 * DirectionText - name of the direction.
 * Minutes - the minutes to bus arrival. Positive number
 * VehicleID - Identifier of the Bus. You can get all current buses Ids with 
 * 			   "Method 13: Bus Positions". 
 * 
 */

class NextBusPrediction(val directionNum:Integer,
						val directionText:String,
						val minutes:Integer, 
						val routeId:String,
						val vehicleId:Integer) {

}