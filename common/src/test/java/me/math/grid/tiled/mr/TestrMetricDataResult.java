package me.math.grid.tiled.mr;

import static org.easymock.EasyMock.expect;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Rule;
import org.junit.Test;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import me.math.Vertex;
import me.math.grid.AbstractSpatialGridPoint;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class TestrMetricDataResult extends EasyMockSupport {

	@Rule
    public EasyMockRule rule = new EasyMockRule(this);
	
	@Mock
	private AbstractSpatialGridPoint pt;
		
	@Mock
	private Configuration config;
	
	@Mock
	private LongWritable longWirte;
	
	@Mock
	private Text text;
    	
	private final Vertex ul = new Vertex(38.941, -77.286);
	
	
	@Test
	public void test() {
		ClusterMetricDataResult  testSubject = new ClusterMetricDataResult(ul);
		
		assertEquals( 0, testSubject.getValue().size());
		testSubject.setValue(10);
		assertEquals( 1, testSubject.getValue().size());
		testSubject.addDataToGridPoint(pt);
		assertEquals( ul, testSubject.getLocation());
	}
	
	@Test
	public void testDensityEstimateMetricDataResult() {
		
		DensityEstimateMetricDataResult testSubject = new DensityEstimateMetricDataResult(ul);
		
		testSubject.setValue(10);
		assertEquals( 10, testSubject.getValue());
		testSubject.addDataToGridPoint(pt);
		assertEquals( ul, testSubject.getLocation());
	}
	
	
	@Test
	public void testPopulateGridDataMap() throws IOException {
		
		PopulateGridData x = new PopulateGridData();
		
		Mapper.Context cmock = this.createMock(Mapper.Context.class);
		DensityEstimateMetricDataResult testSubject = new DensityEstimateMetricDataResult(ul);
		
		expect(cmock.getConfiguration()).andReturn(config);
		expect(config.get(EasyMock.anyString())).andReturn("FileName");
		replayAll();
		
		PopulateGridData.Map mapper = new PopulateGridData.Map();
		mapper.map(longWirte, text, cmock);
		
	}
	
	@Test
	public void testPopulateGridDataReduce() throws IOException {
		
		Reducer.Context cmock = this.createMock(Reducer.Context.class);
				
		PopulateGridData.Reduce reduce = new PopulateGridData.Reduce();
		List<LongWritable> values = new ArrayList<>();
		reduce.reduce(text, values.iterator(), cmock);
	}
	
	

}
