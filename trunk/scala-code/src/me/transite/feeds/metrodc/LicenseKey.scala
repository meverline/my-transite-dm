package me.transite.feeds.metrodc

object LicenseKey {

    val application:String = "OpenTransite"
    val key:String = "fjp2vuqvc4bv5h6nbbcs4byy"
    val status:String = "active"
      
    val BUS_SVC = "http://api.wmata.com/Bus.svc/"
    val RAIL_SVC = "http://api.wmata.com/Rail.svc/"
    val RADIUS = "&radius="
      
    def toUrlString() : String = {
      	"api_key=" + key
    } 
}
