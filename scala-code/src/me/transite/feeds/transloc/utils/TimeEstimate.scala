package me.transite.feeds.transloc.utils

class TimeEstimate (val routeId:String, val estimate: String ) {
  
    private var stopId:String = _;
    private var vehicleId:String = _;
    private var estimateType:EstimateType = Vehicle_Based
    
    ////////////////////////////////////////////////////////////////////////////////////
    
    def this(routeId:String, estimate: String, stop:String) {
       this(routeId, estimate)
       stopId = stop;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
 
    def this(routeId:String, estimate: String, vehicle:String, etype:EstimateType) {
       this(routeId, estimate)
       vehicleId = vehicle
       estimateType = etype
    }

}