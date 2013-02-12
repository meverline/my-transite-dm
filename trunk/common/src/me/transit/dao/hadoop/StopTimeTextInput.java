package me.transit.dao.hadoop;

import java.io.IOException;
import java.io.InputStream;
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
		FileInputFormat<Text, List<StopTimeImpl>> {

	@Override
	public RecordReader<Text, List<StopTimeImpl>> getRecordReader(
			InputSplit split, JobConf job, Reporter arg2) throws IOException {
		// TODO Auto-generated method stub
		return new StopTripRecordReader( job, FileSplit.class.cast(split));
	}

	public class StopTripRecordReader implements
			RecordReader<Text, List<StopTimeImpl>> {

		private CompressionCodecFactory compressionCodecs = null;
		private long start;
		private long pos;
		private long end;
		private StopTimeTextInput.streambuf in;
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
				in = new streambuf(codec.createInputStream(fileIn), job);
				end = Long.MAX_VALUE;
			} else {
				if (start != 0) {
					--start;
					fileIn.seek(start);
				}
				in = new streambuf(fileIn, job);
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
		public List<StopTimeImpl> createValue() {
			return new ArrayList<StopTimeImpl>();
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
			}
			
			return stopTime;
		}

		/**
		 * 
		 */
		@Override
		public boolean next(Text key, List<StopTimeImpl> value)
				throws IOException {
			
			boolean endTrip = false;
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

	/**
	 * 
	 * @author meverline
	 * 
	 */
	public static class streambuf {
		private final static int BUFFER_SIZE = 1024;
		private InputStream in;
		private byte[] buffer;
		// the number of bytes of real data in the buffer
		private int egptr = 0;
		// the current position in the buffer
		private int gptr = 0;
		
		/**
		 * Create a line reader that reads from the given stream using the given
		 * buffer-size.
		 * 
		 * @param in
		 * @throws IOException
		 */
		streambuf(InputStream in) {
			this.in = in;
			this.buffer = new byte[streambuf.BUFFER_SIZE];
		}

		/**
		 * Create a line reader that reads from the given stream using the
		 * <code>io.file.buffer.size</code> specified in the given
		 * <code>Configuration</code>.
		 * 
		 * @param in
		 *            input stream
		 * @param conf
		 *            configuration
		 * @throws IOException
		 */
		public streambuf(InputStream in, Configuration conf)
				throws IOException {
			this(in);
		}
		
		/**
		 * 
		 * @return
		 */
		private byte sgetc() throws IOException {
			if ( gptr > egptr ) {
				underflow();
			}
			return buffer[gptr++];
		}
		
		/**
		 * 
		 * @return
		 */
		private int in_avail() throws IOException {
			if ( gptr > egptr ) {
				underflow();
			}
			return egptr - gptr;
		}

		/**
		 * Fill the buffer with more data.
		 * 
		 * @return was there more data?
		 * @throws IOException
		 */
		private boolean underflow() throws IOException {
			gptr = 0;
			egptr = in.read(buffer);
			return egptr > 0;
		}

		/**
		 * Close the underlying stream.
		 * 
		 * @throws IOException
		 */
		public void close() throws IOException {
			in.close();
		}

		/**
		 * 
		 * @param strBuffer
		 * @return
		 * @throws IOException
		 */
		public int readLine(StringBuffer strBuffer) throws IOException {
						
			strBuffer.delete(0, strBuffer.length());
			while ( in_avail() > 0 ) {
				
				byte c = sgetc();
				switch ( c ) {
					case '\n':
						return in_avail();
					case '\r':
						return in_avail();
					default:
						strBuffer.append(c);
				}
				
			}
			return in_avail();

		}

	}

}
