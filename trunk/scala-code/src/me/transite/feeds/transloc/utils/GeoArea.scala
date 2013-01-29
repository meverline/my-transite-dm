package me.transite.feeds.transloc.utils

import me.utilities.Coordinate

abstract class GeoArea {
   def toUrlString(): String;
}

///////////////////////////////////////////////////////////////////////////////////

class Rectangle ( val upperRight: Coordinate, val lowerleft:Coordinate) extends GeoArea 
{
    def toUrlString() : String = {
      "geo_area=" + upperRight.toTranslocString() + "|" + lowerleft.toTranslocString() 
   } 
}

///////////////////////////////////////////////////////////////////////////////////

class Circle ( val center: Coordinate, val radius:Double) extends GeoArea 
{
    def toUrlString() : String = {
      "geo_area=" + center.toTranslocString() + "|" + radius.toString() 
   } 
}