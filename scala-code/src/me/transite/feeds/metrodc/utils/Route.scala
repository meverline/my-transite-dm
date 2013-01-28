package me.transite.feeds.metrodc.utils

/*
 *  XML
 *  <Route>
 *     <Name>10B - 10B HUNT TWRS-BALL (156)</Name>
 *     <RouteID>10B</RouteID>
 *  </Route>
 * 
 * RouteID - Route identifier.
 * Name - Full name of the route.
 */

class Route(val name:String, val id:String) {

  
    def toUrlString() : String = {
      	"routeId=" + id
    } 
}