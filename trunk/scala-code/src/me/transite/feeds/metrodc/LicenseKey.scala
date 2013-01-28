package me.transite.feeds.metrodc

object LicenseKey {

    val application:String = "OpenTransite"
    val key:String = "fjp2vuqvc4bv5h6nbbcs4byy"
    val status:String = "active"
      
    def toUrlString() : String = {
      	"api_key=" + key
    } 
}
