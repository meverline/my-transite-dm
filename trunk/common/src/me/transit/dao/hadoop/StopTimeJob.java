package me.transit.dao.hadoop;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.transit.database.Agency;
import me.transit.database.impl.StopTimeImpl;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextOutputFormat;

public class StopTimeJob {
	
	
	public Map<String, Integer> findStops(List<String> stops, List<Agency> agencys) {

		JobConf conf = new JobConf(StopTimeJob.class);
		conf.setJobName("stopService");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);

		conf.setMapperClass(StopTimeMapper.class);
		conf.setCombinerClass(StopTimeReducer.class);
		conf.setReducerClass(StopTimeReducer.class);
		conf.setInputFormat(StopTimeTextInput.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
		return new HashMap<String, Integer>();
	}

	/**
	 * 
	 * @author markeverline
	 *
	 */
	public class StopTimeMapper extends MapReduceBase implements
			Mapper<Text, ArrayListWriteable<StopTimeImpl>, Text, IntWritable> {

		private Map<String, StopCounter> stopIds = new HashMap<String, StopCounter>();

		/**
		 * 
		 */
		@Override
		public void map(Text key, ArrayListWriteable<StopTimeImpl> value,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {

			for (StopTimeImpl item : value) {
				if (stopIds.containsKey(item.getStopId())) {
					stopIds.get(item.getStopId()).add();
				}
			}

			for (StopCounter entry : stopIds.values()) {
				output.collect(new Text(entry.getId()),
						new IntWritable(entry.getCount()));
			}
		}

		/**
		 * 
		 */
		@Override
		public void configure(JobConf job) {

			String data[] = job.get(HadoopUtils.STOP_ID_LIST).split(",");

			for (String id : data) {
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
			public StopCounter(String aId) {
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

	/**
	 * 
	 * @author markeverline
	 *
	 */
	public class StopTimeReducer extends MapReduceBase implements
			Reducer<Text, IntWritable, Text, IntWritable> {

		@Override
		public void reduce(Text key, Iterator<IntWritable> values,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {

			int total = 0;
			while (values.hasNext()) {
				total += values.next().get();
			}
			output.collect(key, new IntWritable(total));
		}

	}

}
