package me.transite.feeds.transloc.utils

import me.utilities.Coordinate

class GeoArea;

///////////////////////////////////////////////////////////////////////////////////

class Rectangle ( val upperRight: Coordinate, val lowerleft:Coordinate) extends GeoArea 
{
    def toUrlString() : String = {
      "geo_area=" + upperRight.lat + "," + upperRight.lon + "|" + lowerleft.lat + "," + lowerleft.lon 
   } 
}

///////////////////////////////////////////////////////////////////////////////////

class Circle ( val center: Coordinate, val radius:Double) extends GeoArea 
{
    def toUrlString() : String = {
      "geo_area=" + center.lat + "," + center.lon + "|" + radius.toString() 
   } 
}