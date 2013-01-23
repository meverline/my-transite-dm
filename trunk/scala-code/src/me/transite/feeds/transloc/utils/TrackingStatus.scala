package me.transite.feeds.transloc.utils

sealed abstract class TrackingStatus
case object Up extends TrackingStatus
case object Down extends TrackingStatus