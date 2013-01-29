package me.utilities

class Coordinate (val lat: Double, val lon:Double){
  
    def toMetroString() : String = {
      	"lat=" + lat + "&lon=" +lon;
    } 
    
    def toTranslocString() : String = {
       lat + "," +lon;
    } 
}