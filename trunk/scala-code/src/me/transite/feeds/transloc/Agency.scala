package me.transite.feeds.transloc

/*
 * Transloc Version 1.1 Agenecies api inside Data array
 * 
 * URL
 *    http://api.transloc.com/version/agencies.json
 * Rate Limit
 *  Can be requested every 10 seconds from the same IP address.
 *  Required Parameters
 *   None
 * Optional Parameters
 *   agencies - a list of agency IDs which to retrieve
 *   geo_area - a geographical area filter (see geo_area)
 * 
 *       {
 * 			 "agency_id": "agency_id",
 *           "name": "sample-agency",
 *           "short_name": "Sample Agency",
 *           "long_name": "Sample Agency from Raleigh, NC",
 *           "url": "http://www.example.org/",
 *           "phone": "123-555-7788",
 *           "language": "en",
 *           "timezone": "America/New_York", # These values are taken from the tz database
 *           "bounding_box": [
 *               {"lat": 48.234661, "lng": -76.23456},
 *               {"lat": 48.134661, "lng": -76.63456}
 *           ]
 *        }
 */
class Agency ( val agencyId:String,
			   val name:String, 
			   val shortName:String, 
			   val longName:String, 
			   val url:String, 
			   val timezone:String ) {
  
  
   def toUrlString() : String = {
       agencyId
   } 
      
}

//////////////////////////////////////////////////////////////////////////////////

object Agency {
   def url():String = {  "http://api.transloc.com/1.1/agencies.json" }
}