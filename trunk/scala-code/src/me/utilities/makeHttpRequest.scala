package me.utilities


import scala.io.Source

class makeHttpRequest {
  
  
  def request(url:String):String = {
      Source.fromURL(url).mkString
  }

}