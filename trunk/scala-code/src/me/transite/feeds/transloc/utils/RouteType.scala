package me.transite.feeds.transloc.utils


sealed abstract class RouteType
case object Light_Rail extends RouteType
case object Subway extends RouteType
case object Rail extends RouteType
case object Bus extends RouteType
case object Ferry extends RouteType
case object Cable_Car extends RouteType
case object Gondola extends RouteType
case object Finicular extends RouteType