/**
 * 
 */
package me.transit.dao.hadoop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import me.transit.database.impl.StopTimeImpl;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

/**
 * @author meverline
 *
 */
public class StopTimeMapper extends MapReduceBase implements
		Mapper<Text,  ArrayListWriteable<StopTimeImpl>, Text, IntWritable> {
	
	private Map<String,StopCounter> stopIds = new HashMap<String,StopCounter>();

	/**
	 * 
	 */
	@Override
	public void map(Text key, ArrayListWriteable<StopTimeImpl> value,
				    OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		
			for ( StopTimeImpl item : value ) {
				if ( stopIds.containsKey(item.getStopId()) ) {
					stopIds.get(item.getStopId()).add();
				}
			}
			
			for ( StopCounter entry : stopIds.values()) {
				output.collect(new Text(entry.getId()),  new IntWritable(entry.getCount()));
			}
	}

	/**
	 * 
	 */
	@Override
	public void configure(JobConf job) {
		
		 String data[] = job.get(HadoopUtils.STOP_ID_LIST).split(",");
		 
		 for ( String id : data) {
			 stopIds.put(id, new StopCounter(id));
		 }
		
	}
	
	/**
	 * 
	 * @author markeverline
	 *
	 */
	public class StopCounter {
		
		private String id = null;
		private int count = 0;
		
		/**
		 * 
		 * @param aId
		 */
		public StopCounter( String aId) {
			id = aId;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getId() {
			return id;
		}

		/**
		 * 
		 * @return
		 */
		public int getCount() {
			return count;
		}

		/**
		 * 
		 */
		public void add() {
			this.count++;
		}
	
	}
	
}
