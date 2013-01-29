package me.transite.feeds.transloc.utils

import java.util.Calendar

class TimeEstimate (val routeId:String, val estimate: Calendar ) {
  
    private var stopId:String = _;
    private var vehicleId:String = _;
    private var estimateType:EstimateType = Vehicle_Based
    
    ////////////////////////////////////////////////////////////////////////////////////
    
    def this(routeId:String, estimate: Calendar, stop:String) {
       this(routeId, estimate)
       stopId = stop;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
 
    def this(routeId:String, estimate:Calendar, vehicle:String, etype:EstimateType) {
       this(routeId, estimate)
       vehicleId = vehicle
       estimateType = etype
    }

}