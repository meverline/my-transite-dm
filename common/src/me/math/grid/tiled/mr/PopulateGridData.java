package me.math.grid.tiled.mr;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class PopulateGridData {

	public static final String QUERY_FILE = "queryFile";
	public static class Map extends Mapper<LongWritable, Text, Text, LongWritable> {

		public void map(LongWritable tileIndex, 
						Text tileFile, 
						Context context) throws IOException {
			
			Configuration conf = context.getConfiguration();
			String param = conf.get(PopulateGridData.QUERY_FILE);
			
		}
		
	}
	
	public static class Reduce extends Reducer<Text, LongWritable, Text, LongWritable> {
		
	    public void reduce(Text key, 
	    		  		   Iterator<LongWritable> values,
	    		  		   Context context) throws IOException {
	    }
	}
}
