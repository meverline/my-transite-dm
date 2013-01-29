package me.transite.feeds.metrodc.utils

/*
 * XML
 * 
 * <StopTime>
 *    <StopID>4000376</StopID>
 *    <StopName>SEMINARY RD + FAIRBANKS AVE</StopName>
 *    <StopSeq>21</StopSeq>
 *    <Time>2010-10-26T17:30:23</Time>
 * </StopTime>
 *
 */

import java.util.Calendar

class StopTime (val stop:Stop, 
				val time:Calendar, 
				val seq:Integer) {

}