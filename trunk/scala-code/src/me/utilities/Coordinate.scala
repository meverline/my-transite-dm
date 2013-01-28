package me.utilities

class Coordinate (val lat: Double, val lon:Double){
  
    def toUrlString() : String = {
      	"lat=" + lat + "&lon=" +lon;
    } 
}