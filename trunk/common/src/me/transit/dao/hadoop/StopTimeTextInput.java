package me.transit.dao.hadoop;

import java.io.IOException;
import java.util.ArrayList;

import me.transit.database.StopTime;
import me.transit.database.impl.StopTimeImpl;
import me.transit.parser.TransitFeedParser.StopTripInfo;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

public class StopTimeTextInput extends  FileInputFormat<Text, StopTimeImpl> {

	@Override
	public RecordReader<Text, StopTimeImpl> getRecordReader(InputSplit arg0, JobConf arg1, Reporter arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class StopTripRecordReader implements RecordReader<Text, StopTimeImpl>  {

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Text createKey() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public StopTimeImpl createValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getPos() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float getProgress() throws IOException {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean next(Text arg0, StopTimeImpl arg1) throws IOException {
			
			while ( inStream.ready() ) {
				
				String line = inStream.readLine();
				if ( line.trim().length() > 0 && line.indexOf(',') != -1 ) {
					String data[] = line.split(",");
					
					stopTime = new StopTimeImpl(last);
					
					String id = data[indexMap.get("TripId")].replace('"', ' ').trim();
					
					if ( indexMap.containsKey("ArrivalTime") ) {
					  String time[] = data[indexMap.get("ArrivalTime")].trim().split(":");
					  StringBuilder builder = new StringBuilder();
					  for ( String str : time ) {
						  builder.append(str);
					  }
					  if ( builder.toString().length() > 0 ) {	  
						  stopTime.setArrivalTime(Long.parseLong(builder.toString().replace('"', ' ').trim()));
					  }
					} 
					
					if ( indexMap.containsKey("DepartureTime") ) {
						String time[]  = data[indexMap.get("DepartureTime")].trim().split(":");
						StringBuilder builder = new StringBuilder();
						for ( String str : time ) {
							builder.append(str);
						}
						if ( builder.toString().length() > 0 ) {	 
						   stopTime.setDepartureTime(Long.parseLong(builder.toString().replace('"', ' ').trim()));	
						}
						stopTime.setStopId(data[indexMap.get("StopId")].replace('"', ' ').trim());
					} 
					
					if ( indexMap.containsKey("DropOffType") ) {
					  int ndx = Integer.parseInt(data[indexMap.get("DropOffType")].replace('"', ' ').trim());
					  stopTime.setDropOffType(StopTime.PickupType.values()[ndx]);
					} 
					
					if ( indexMap.containsKey("PickupType") ) {
					   int ndx = Integer.parseInt(data[indexMap.get("PickupType")].replace('"', ' ').trim());
					   stopTime.setPickupType(StopTime.PickupType.values()[ndx]);
					} 
					
					if ( indexMap.containsKey("DistTravel") ) {
						double dist = Double.parseDouble(data[indexMap.get("DistTravel")].replace('"', ' ').trim());
						stopTime.setShapeDistTravel(dist);
					} 
					
					if ( indexMap.containsKey("StopHeadSign") ) {
						stopTime.setStopHeadSign(data[indexMap.get("StopHeadSign")].trim());
					}
					
					if ( indexMap.containsKey("StopId") ) {
						stopTime.setStopId(data[indexMap.get("StopId")].replace('"', ' ').trim());
						if ( ! stopMap.containsKey( stopTime.getStopId()) ) {
							stopMap.put( stopTime.getStopId(), new ArrayList<StopTripInfo>());
						}
					}
					
					if ( current == null ) { current = id; }
					
					if ( current.compareTo(id) != 0 ) {
							trip = tripMap.get(id);
							trip.getTrip().addStopTime(stopTime);
							
							if ( ! stopMap.get(stopTime.getStopId()).contains(trip.getRouteId()) ) {
								stopMap.get(stopTime.getStopId()).add( new StopTripInfo( trip.getRouteId(), 
																						 trip.getTrip().getHeadSign()));
							}
						current = id;
						trip = null;
					}
						
					trip = tripMap.get(current);
					trip.getTrip().addStopTime(stopTime);
					if ( ! stopMap.get(stopTime.getStopId()).contains(trip.getRouteId()) ) {
						stopMap.get(stopTime.getStopId()).add( new StopTripInfo( trip.getRouteId(), 
																				 trip.getTrip().getHeadSign()));
					}
					lineCnt++;
					cnt++;
					if ( cnt > 100000 ) {
						log.info("parseStopTimes " + lineCnt + " ...");
						cnt = 0;
					}
				}
			}
			return false;
		}
		
	}

}
    
