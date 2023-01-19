package me.math;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import me.math.kdtree.MinBoundingRectangle;

@SuppressWarnings("unused")
public class TestLocalDownFrame {

	@Test
	public void testConstructor() {
		
		LocalDownFrame ldf = new LocalDownFrame(new Vertex(38.9, -75.0));
		ldf = new LocalDownFrame(new VectorMath(38.9, -75.0, 0));
		ldf = new LocalDownFrame(38.0, -75.0);
	}
	
	@Test
	public void testAngleInRadiansForArcLength()
	{
		LocalDownFrame ldf = new LocalDownFrame(new Vertex(38.9, -75.0));
		assertEquals(2.0, ldf.angleInRadiansForArcLength(20.0, 10), 0.01);
		
	}
	
	@Test
	public void testJson()
	{
		LocalDownFrame ldf = new LocalDownFrame(new Vertex(38.9, -75.0));
		assertEquals(2.0, ldf.angleInRadiansForArcLength(20.0, 10), 0.01);
		
		ObjectMapper json = new ObjectMapper();
		
		try {
			String str = json.writeValueAsString(ldf);
			LocalDownFrame dup = json.readValue(str, LocalDownFrame.class);
			
		} catch ( IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testRotationQuaternion() {
		
		LocalDownFrame ldf = new LocalDownFrame(new Vertex(38.9, -75.0));
		
		Quaternion q  = ldf.northRotationQuaternion(Math.toRadians(10.0));
		assertEquals(0.9961946980917455, q.getX0(), 0.1);
	    assertEquals(-0.08418598282936919, q.getX1(), 0.1);
	    assertEquals(-0.022557566113149845, q.getX2(), 0.1);
	    assertEquals(4.838115616337856E-18, q.getX3(), 0.1);
		
		q  = ldf.eastRotationQuaternion(Math.toRadians(10.0));
		assertEquals(0.9961946980917455, q.getX0(), 0.1);
	    assertEquals(-0.014165318189540659, q.getX1(), 0.1);
	    assertEquals(0.05286568718874533, q.getX2(), 0.1);
	    assertEquals(0.06782835964806141, q.getX3(), 0.1);
		
		q = LocalDownFrame.eastRotationAtLatitudeQuaternion(Math.toRadians(-29.0), 100.0);
		assertEquals(0.9999999999598317, q.getX0(), 0.1);
	    assertEquals(0.0, q.getX1(), 0.1);
	    assertEquals(0.0, q.getX2(), 0.1);
	    assertEquals(8.96307235057832E-6, q.getX3(), 0.1);
		
	}
	
	@Test
	public void testGetRelativePosition() {
		
		LocalDownFrame ldf = new LocalDownFrame(new Vertex(38.9, -75.0));
		
		VectorMath vec = ldf.getRelativePosition(2000.0, 
												2000.0, 
												LocalDownFrame.RelativePositionOrder.EAST_THEN_NORTH);
		
		assertEquals(1286316.9934751804, vec.getX(), 0.01);
		assertEquals(-4792874.923373449, vec.getY(), 0.01);
		assertEquals(4006790.5051255105, vec.getZ(), 0.01);
		
		vec = ldf.getRelativePosition(2000.0, 
				2000.0, 
				LocalDownFrame.RelativePositionOrder.NORTH_THEN_EAST);
		
		assertEquals(1286317.441154856, vec.getX(), 0.01);
		assertEquals(-4792874.638650793, vec.getY(), 0.01);
		assertEquals(4006790.7019867986, vec.getZ(), 0.01);
		
	}
	
	@Test
	public void testEnum() {
		
		LocalDownFrame.RelativePositionOrder.values();
		assertNotNull(LocalDownFrame.RelativePositionOrder.valueOf("NORTH_THEN_EAST"));
		assertNotNull(LocalDownFrame.RelativePositionOrder.valueOf("EAST_THEN_NORTH"));
	}
}
