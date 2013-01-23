package me.transite.feeds.transloc.utils


sealed abstract class EstimateType
case object Vehicle_Based extends EstimateType
case object Schedule_Based extends EstimateType
