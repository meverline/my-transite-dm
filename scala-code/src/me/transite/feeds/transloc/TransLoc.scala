package me.transite.feeds.transloc


object TransLoc {
  
	val URL:String = "http://api.transloc.com/1.1/"
	val ROUTES:String = "&routes="
	val STOPS:String = "&stops="
	  
	//////////////////////////////////////////////////////////////////////////////////
	  
	def toRoutesCgiList(routes:Array[Route]):String = {
	    ROUTES + routes.mkString(",")
	}
	
	//////////////////////////////////////////////////////////////////////////////////
	
    def toStopsCgiList(stops:Array[Stop]):String = {
	    STOPS + stops.mkString(",")
	}
	  
}