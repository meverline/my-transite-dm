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
			InputSplit arg0, JobConf arg1, Reporter arg2) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public class StopTripRecordReader implements
			RecordReader<Text, List<StopTimeImpl>> {

		private CompressionCodecFactory compressionCodecs = null;
		private long start;
		private long pos;
		private long end;
		private StopTimeTextInput.LineReader in;
		private StopTimeImpl last= null;
		private HashMap<String,Integer> indexMap = null;
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
			boolean skipFirstLine = false;
			if (codec != null) {
				in = new LineReader(codec.createInputStream(fileIn), job);
				end = Long.MAX_VALUE;
			} else {
				if (start != 0) {
					skipFirstLine = true;
					--start;
					fileIn.seek(start);
				}
				in = new LineReader(fileIn, job);
			}
			
			List<String> header = new ArrayList<String>();
			
			Text data = new Text();
			in.readLine(data, 
					    maxLineLength,
					    Math.max((int) Math.min(Integer.MAX_VALUE, end - pos),
								  maxLineLength));
			
			indexMap = processHeader(data.toString(), "shape", header);
			
			if (skipFirstLine) { // skip first line and re-establish "start".
				start += in.readLine(new Text(), 0,
						(int) Math.min((long) Integer.MAX_VALUE, end - start));
			}
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
		}

		@Override
		public boolean next(Text key, List<StopTimeImpl> value)
				throws IOException {
			
			Text data = new Text();
			while (pos < end) {
				
				int newSize = in.readLine(data, 
										  maxLineLength,
										  Math.max((int) Math.min(Integer.MAX_VALUE, end - pos),
												  	maxLineLength));
				processLine(data.toString());
				if (newSize == 0) {
					return false;
				}
				pos += newSize;
				if (newSize < maxLineLength) {
					key.set(pos);
					return true;
				}

				// line too long. try again
				LOG.info("Skipped line of size " + newSize + " at pos "
						+ (pos - newSize));
			}

			return false;
		}
	}

	/**
	 * Copied from Hadoop LineRecorederReader source
	 * 
	 * @author meverline
	 * 
	 */
	public static class LineReader {
		private final static int BUFFER_SIZE = 1024;
		private InputStream in;
		private byte[] buffer;
		// the number of bytes of real data in the buffer
		private int bufferLength = 0;
		// the current position in the buffer
		private int bufferPosn = 0;

		/**
		 * Create a line reader that reads from the given stream using the given
		 * buffer-size.
		 * 
		 * @param in
		 * @throws IOException
		 */
		LineReader(InputStream in) {
			this.in = in;
			this.buffer = new byte[LineReader.BUFFER_SIZE];
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
		public LineReader(InputStream in, Configuration conf)
				throws IOException {
			this(in);
		}

		/**
		 * Fill the buffer with more data.
		 * 
		 * @return was there more data?
		 * @throws IOException
		 */
		boolean backfill() throws IOException {
			bufferPosn = 0;
			bufferLength = in.read(buffer);
			return bufferLength > 0;
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
		 * Read from the InputStream into the given Text.
		 * 
		 * @param str
		 *            the object to store the given line
		 * @param maxLineLength
		 *            the maximum number of bytes to store into str.
		 * @param maxBytesToConsume
		 *            the maximum number of bytes to consume in this call.
		 * @return the number of bytes read including the newline
		 * @throws IOException
		 *             if the underlying stream throws
		 */
		public int readLine(Text str) throws IOException {
			
			str.clear();
			boolean hadFinalNewline = false;
			boolean hadFinalReturn = false;
			boolean hitEndOfFile = false;
			int startPosn = bufferPosn;
			long bytesConsumed = 0;
			
			outerLoop: while (true) {
				if (bufferPosn >= bufferLength) {
					if (!backfill()) {
						hitEndOfFile = true;
						break;
					}
				}
				startPosn = bufferPosn;
				for (; bufferPosn < bufferLength; ++bufferPosn) {
					switch (buffer[bufferPosn]) {
					case '\n':
						hadFinalNewline = true;
						bufferPosn += 1;
						break outerLoop;
					case '\r':
						if (hadFinalReturn) {
							// leave this \r in the stream, so we'll get it next
							// time
							break outerLoop;
						}
						hadFinalReturn = true;
						break;
					default:
						if (hadFinalReturn) {
							break outerLoop;
						}
					}
				}
				bytesConsumed += bufferPosn - startPosn;
				int length = bufferPosn - startPosn - (hadFinalReturn ? 1 : 0);
				length = (int) Math
						.min(length, maxLineLength - str.getLength());
				if (length >= 0) {
					str.append(buffer, startPosn, length);
				}
				if (bytesConsumed >= maxBytesToConsume) {
					return (int) Math.min(bytesConsumed,
							(long) Integer.MAX_VALUE);
				}
			}
			int newlineLength = (hadFinalNewline ? 1 : 0)
					+ (hadFinalReturn ? 1 : 0);
			if (!hitEndOfFile) {
				bytesConsumed += bufferPosn - startPosn;
				int length = bufferPosn - startPosn - newlineLength;
				length = (int) Math
						.min(length, maxLineLength - str.getLength());
				if (length > 0) {
					str.append(buffer, startPosn, length);
				}
			}
			return (int) Math.min(bytesConsumed, (long) Integer.MAX_VALUE);
		}

	}

}
