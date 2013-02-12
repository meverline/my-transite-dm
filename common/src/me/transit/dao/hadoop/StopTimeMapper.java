/**
 * 
 */
package me.transit.dao.hadoop;

import java.io.IOException;

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

	@Override
	public void map(Text key, ArrayListWriteable<StopTimeImpl> value,
				    OutputCollector<Text, IntWritable> output, Reporter reporter)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void configure(JobConf job) {
		// TODO Auto-generated method stub
		super.configure(job);
	}
	
}
