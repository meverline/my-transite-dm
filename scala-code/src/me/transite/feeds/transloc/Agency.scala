package me.transite.feeds.transloc

/*
 * Transloc Version 1.1 Agenecies api inside Data array
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
class Agency ( agencyId:String,
			   name:String, 
			   shortName:String, 
			   longName:String, 
			   url:String, 
			   timezone:String ) {

}