package me.transite.feeds.metrodc.utils

sealed abstract class PredicationMinType {
  var time:Integer = 0
  
}

case object Arriving extends PredicationMinType
case object Boarding extends PredicationMinType
case object Minuite extends PredicationMinType
