package me.transit.dao.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.transit.database.StopTime;
import me.transit.database.impl.StopTimeImpl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;

public class StopTimeTextInput extends
		FileInputFormat<Text, ArrayListWriteable<StopTimeImpl>> {

	@Override
	public RecordReader<Text, ArrayListWriteable<StopTimeImpl>> getRecordReader(
			InputSplit split, JobConf job, Reporter arg2) throws IOException {
		return new StopTripRecordReader( job, FileSplit.class.cast(split));
	}
	
	@Override
	protected boolean isSplitable(FileSystem fs, Path filename) {
		return true;
	}

	/**
	 * 
	 * @author meverline
	 *
	 */
	public class StopTripRecordReader implements
			RecordReader<Text, ArrayListWriteable<StopTimeImpl>> {

		private CompressionCodecFactory compressionCodecs = null;
		private long start;
		private long pos;
		private long end;
		private Streambuf in;
		private StopTimeImpl last= null;
		private HashMap<String,Integer> indexMap = null;
		private StringBuffer buffer = new StringBuffer();
		
		/**
		 * 
		 * @param job
		 * @param split
		 * @throws IOException
		 */
		public StopTripRecordReader(Configuration job, FileSplit split)
				throws IOException {
			start = split.getStart();
			end = start + split.getLength();
			final Path file = split.getPath();
			compressionCodecs = new CompressionCodecFactory(job);
			final CompressionCodec codec = compressionCodecs.getCodec(file);

			// open the file and seek to the start of the split
			FileSystem fs = file.getFileSystem(job);
			FSDataInputStream fileIn = fs.open(split.getPath());

			if (codec != null) {
				in = new Streambuf(codec.createInputStream(fileIn), job);
				end = Long.MAX_VALUE;
			} else {
				if (start != 0) {
					--start;
					fileIn.seek(start);
				}
				in = new Streambuf(fileIn, job);
			}
			
			List<String> header = new ArrayList<String>();
			
			in.readLine(buffer);
			indexMap = processHeader(buffer.toString(), "shape", header);
			this.pos = start;
		}
		
		/**
		 * 
		 * @param header
		 * @param strip
		 * @param order
		 * @return
		 */
		private HashMap<String,Integer> processHeader(String header, String strip, List<String> order)
		{
			String fields[] = header.split(",");
			HashMap<String,Integer> indexMap = new HashMap<String,Integer>(); 

			int ndx = 0;
			String mapTo = null;
			for ( String fld : fields) {
				String item = fld.replace('"', ' ').trim();
				if ( item.indexOf('_') == -1 ) {
					mapTo = item.substring(0,1).toUpperCase() + item.substring(1);
					order.add(mapTo);
				    indexMap.put(mapTo, ndx);
				} else {
					String data[] = item.toLowerCase().split("_");
					StringBuilder fieldName = new StringBuilder();
					
					for ( String name : data) {
						if ( name.compareTo(strip) != 0) {
						     mapTo = name.substring(0,1).toUpperCase() + name.substring(1);
							fieldName.append(mapTo);
						}
					}
					order.add(fieldName.toString());
					indexMap.put(fieldName.toString(), ndx);
				}
				ndx++;
			}
			
			return indexMap;
		}

		@Override
		public void close() throws IOException {
			if (in != null) {
				in.close();
			}
		}

		@Override
		public Text createKey() {
			return new Text();
		}

		@Override
		public ArrayListWriteable<StopTimeImpl> createValue() {
			return new ArrayListWriteable<StopTimeImpl>();
		}

		@Override
		public long getPos() throws IOException {
			return pos;
		}

		@Override
		public float getProgress() throws IOException {
			if (start == end) {
				return 0.0f;
			} else {
				return Math.min(1.0f, (pos - start) / (float) (end - start));
			}
		}
		
		/**
		 * 
		 * @param line
		 * @return
		 */
		private StopTimeImpl processLine(String line)
		{
			String data[] = line.split(",");
			
			StopTimeImpl stopTime = new StopTimeImpl(last);
			
			String id = data[indexMap.get("TripId")].replace('"', ' ').trim();
			
			stopTime.setTripId(id);
			
			if ( indexMap.containsKey("ArrivalTime") ) {
			  String time[] = data[indexMap.get("ArrivalTime")].trim().split(":");
			  StringBuilder builder = new StringBuilder();
			  for ( String str : time ) {
				  builder.append(str);
			  }
			  if ( builder.toString().length() > 0 ) {	  
//				  stopTime.setArrivalTime(Long.parseLong(builder.toString().replace('"', ' ').trim()));
			  }
			} 
			
			if ( indexMap.containsKey("DepartureTime") ) {
				String time[]  = data[indexMap.get("DepartureTime")].trim().split(":");
				StringBuilder builder = new StringBuilder();
				for ( String str : time ) {
					builder.append(str);
				}
				if ( builder.toString().length() > 0 ) {	 
//				   stopTime.setDepartureTime(Long.parseLong(builder.toString().replace('"', ' ').trim()));	
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
			}
			
			return stopTime;
		}

		/**
		 * 
		 */
		@Override
		public boolean next(Text key, ArrayListWriteable<StopTimeImpl> value)
				throws IOException {
			
			boolean endTrip = false;
			
			if ( last != null ) {
				value.add(last);
				key.set(last.getTripId());
			}
			
			while (endTrip) {
				
				int newSize = in.readLine(buffer);
				if ( newSize == -1 ) {
					endTrip = true;
				} else {
					StopTimeImpl stopTime = processLine(buffer.toString());
					
					if ( stopTime.getTripId().compareTo(last.getTripId()) != 0 ) {
						endTrip = true;
					} else {
						key.set(stopTime.getTripId());
						value.add(stopTime);
					}
					last = stopTime;
				}
			}

			return false;
		}
	}

}
