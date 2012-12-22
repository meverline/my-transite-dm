/**
 * 
 */
package org.transiteRepositry.server.tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.transiteRepositry.server.request.CircleQueryRequest;
import org.transiteRepositry.server.request.ClusterQueryRequest;
import org.transiteRepositry.server.request.GenerateHeatMapRequest;
import org.transiteRepositry.server.request.RouteGeometryRequest;
import org.transiteRepositry.server.request.RouteRequest;
import org.transiteRepositry.server.request.TransiteStopQueryRequest;
import org.transiteRepositry.server.request.utils.Address;
import org.transiteRepositry.server.request.utils.Cluster;
import org.transiteRepositry.server.request.utils.Distance;
import org.transiteRepositry.server.request.utils.LatLonBox;
import org.transiteRepositry.server.request.utils.Metric;
import org.transiteRepositry.server.request.utils.RouteGeometryTuple;
import org.transiteRepositry.server.request.utils.RouteTuple;
import org.transiteRepositry.server.response.ClusterQueryReponse;
import org.transiteRepositry.server.response.GenerateHeatMapReponse;
import org.transiteRepositry.server.response.RouteGeometryResponse;
import org.transiteRepositry.server.response.RouteResponse;
import org.transiteRepositry.server.response.TransiteStopQueryResponse;

import me.datamining.sample.TransitStopSpatialSample;
import me.math.Vertex;
import me.math.grid.SpatialGridPoint;
import me.transit.database.Route;
import me.transit.database.RouteGeometry;
import me.transit.database.RouteStopData;
import me.transit.database.ServiceDate;
import me.transit.database.StopTime;
import me.transit.database.TransitStop;
import me.transit.database.Trip;
import me.transit.database.impl.RouteGeometryImpl;
import me.transit.database.impl.RouteImpl;
import me.transit.database.impl.RouteStopDataImpl;
import me.transit.database.impl.ServiceDateImpl;
import me.transit.database.impl.StopTimeImpl;
import me.transit.database.impl.TransitStopImpl;
import me.transit.database.impl.TripImpl;
import me.utils.GradientParameters;


import com.thoughtworks.xstream.XStream;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * @author meverline
 *
 */
public class GenerateExampleXml {
	
	private static GeometryFactory factory = new GeometryFactory();
	public String directory = "example-xml//";
	
	private void initStream(XStream stream )
	{
		stream.processAnnotations(SpatialGridPoint.class);
		stream.processAnnotations(Vertex.class);
		stream.processAnnotations(Address.class);
		stream.processAnnotations(Distance.class);
		stream.processAnnotations(LatLonBox.class);
		stream.processAnnotations(Metric.class);
		stream.processAnnotations(RouteGeometryTuple.class);
		stream.processAnnotations(RouteTuple.class);
		stream.processAnnotations(TransitStopSpatialSample.class);
	}
	
	private void writeObject(Object obj)
	{
		StringBuilder fileName = new StringBuilder();
		
		fileName.append(directory);
		fileName.append( obj.getClass().getSimpleName());
		fileName.append(".xml");
		
		System.out.println("write: " + fileName.toString());
		try {
			PrintStream ps = new PrintStream(new FileOutputStream(fileName.toString()));
			XStream stream = new XStream();
			
			this.initStream(stream);
			stream.processAnnotations(obj.getClass());
			ps.println(stream.toXML(obj));
			ps.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void writeCircleQuery()
	{
		CircleQueryRequest request = new CircleQueryRequest();
		Address address = new Address();
		
		address.setNumber(231);
		address.setStreet("N Barton St.");
		address.setCity("Arlington");
		address.setState("VA");
		address.setZipCode(22201);
		
		List<Address> rtn = new ArrayList<Address>();
		rtn.add(address);
		request.setAddress(rtn);
		request.setAgency("WMTA");
		
		this.writeObject(request);
	}
	
	public void writeRouteRequest()
	{
		RouteRequest request = new RouteRequest();
		RouteTuple tuple = new RouteTuple();
		
		tuple.setAgency("WMATA");
		tuple.setShortName("4B");
		
		List<RouteTuple> data = new ArrayList<RouteTuple>();
		data.add(tuple);
		
		request.setRouteTuples(data);
		
		this.writeObject(request);
	}
	
	public void writeRouteGeometryRequesst()
	{
		RouteGeometryRequest request = new RouteGeometryRequest();
		RouteGeometryTuple tuple = new RouteGeometryTuple();
		
		tuple.setAgency("WMATA");
		tuple.setShapeId(1000L);
		
		List<RouteGeometryTuple> data = new ArrayList<RouteGeometryTuple>();
		data.add(tuple);
		
		request.setRouteGeometryTuple(data);
		
		this.writeObject(request);
		
	}
	
	public void writeClusterQueryResponse()
	{
		ClusterQueryReponse obj = new ClusterQueryReponse();
		
		SpatialGridPoint pt = new SpatialGridPoint(10,10,new Vertex(-39.0, -40.0), 10, null);
		
		List<SpatialGridPoint> aList = new ArrayList<SpatialGridPoint>();
		aList.add(pt);
		
		TransitStopSpatialSample sample = new TransitStopSpatialSample();
		pt.setData(sample);
		sample.setInterpolationValue(10);
		
		obj.setClusters(aList);
		
		this.writeObject(obj);
		 
	}
	
	public void writeGenerateHeatMapResponse()
	{
		GenerateHeatMapReponse obj = new GenerateHeatMapReponse();
		
		obj.setUrl("http://this/is/a/url.html");
		
		this.writeObject(obj);
		 
	}
	
	public void writeTransiteStopQueryRequest()
	{
		TransiteStopQueryRequest request = new TransiteStopQueryRequest();
		LatLonBox box = new LatLonBox();
		
		box.setEast(170.0);
		box.setWest(-170.0);
		box.setNorth(89.9);
		box.setSouth(-89.9);
		
		request.setLatLonBox(box);
		request.setAgency("WAMTA");
		
		this.writeObject(request);
	}
	
	public void writeGenerateHeatMapRequest()
	{
		GenerateHeatMapRequest request = new GenerateHeatMapRequest();
		Distance distance = new Distance();
		
		distance.setUnit("ft");
		distance.setValue(100.0);
		request.setDistance(distance);
		
		Metric metric = new Metric();
		metric.setMetricType("aType");
		metric.setMetricData("Data");
		request.setMetric(metric);
		
		LatLonBox box = new LatLonBox();
		
		box.setEast(170.0);
		box.setWest(-170.0);
		box.setNorth(89.9);
		box.setSouth(-89.9);
		
		request.setLatLonBox(box);
		request.setAgency("WAMTA");
		
		List<GradientParameters> metrics = new ArrayList<GradientParameters>();

		metrics.add(new GradientParameters("#A0A0FF", "BLUE", 0.1, 29.9, 15, 95));	
		request.setGradianteParameters(metrics);
		
		this.writeObject(request);
	}
	
	public void writeClusterQueryRequest()
	{
		ClusterQueryRequest request = new ClusterQueryRequest();
		Distance distance = new Distance();
		
		distance.setUnit("ft");
		distance.setValue(100.0);
		request.setDistance(distance);
		
		Metric metric = new Metric();
		metric.setMetricType("aType");
		metric.setMetricData("Data");
		request.setMetric(metric);
		
		Cluster cluster = new Cluster();
		cluster.setHiRange(100);
		cluster.setLowRange(0);
		
		request.setCluster(cluster);
		
		LatLonBox box = new LatLonBox();
		
		box.setEast(170.0);
		box.setWest(-170.0);
		box.setNorth(89.9);
		box.setSouth(-89.9);
		
		request.setLatLonBox(box);
		request.setAgency("WAMTA");
		
		this.writeObject(request);
	}
	
	public void writeRouteGeometryResponse()
	{
		RouteGeometryResponse obj = new RouteGeometryResponse();
		
		RouteGeometry shape = new RouteGeometryImpl();
		
		Coordinate line[] = new Coordinate[5];
		for ( int ndx = 0; ndx < 5; ndx++ ) {
		  line[ndx] = new Coordinate(-70.0,49.0);
		}
		shape.setShape(factory.createLineString(line));
	
		List<RouteGeometry> aList = new ArrayList<RouteGeometry>();
		aList.add(shape);
		obj.setShapes(aList);
		
		this.writeObject(obj);
	}
	
	public void writeRouteResponse()
	{
		RouteResponse obj = new RouteResponse();
		
		Route shape = new RouteImpl();
		
		shape.setColor("blue");
		shape.setDesc("Desc");
		shape.setId(-1L);
		shape.setLongName("longName");
		shape.setShortName("shortName");
		shape.setTextColor("textColor");
		shape.setType(Route.RouteType.BUS);
		
		Trip trip = new TripImpl();
		
		trip.setShape(new RouteGeometryImpl());
		trip.setDirectionId(Trip.DirectionType.IN_BOUND);
		trip.setHeadSign("headSign");
		trip.setId(-1L);
		trip.setShortName("shortName");
		
		List<Trip> tlist = new ArrayList<Trip>();
		tlist.add(trip);
		
		shape.setTripList(tlist);
		
		ServiceDate date = new ServiceDateImpl();
		
		date.setStartDate(Calendar.getInstance());
		date.setEndDate(Calendar.getInstance());
		date.setServiceDayFlag(1010101);
		trip.setService(date);
		
		StopTime stop = new StopTimeImpl();
		stop.setArrivalTime(102013);
		stop.setDepartureTime(102014);
		stop.setDropOffType(StopTime.PickupType.REGULAR);
		stop.setDropOffType(StopTime.PickupType.REGULAR);
		stop.setShapeDistTravel(0.2);
		stop.setStopHeadSign("headSign");
		stop.setStopId(105678);
		
		trip.addStopTime(stop);
		
		List<Route> aList = new ArrayList<Route>();
		aList.add(shape);
		
		obj.setRoutes(aList);
		
		this.writeObject(obj);
	}
	
	public void writeTransiteStopQueryResponse()
	{
		TransiteStopQueryResponse obj = new TransiteStopQueryResponse();
		
		TransitStop stop = new TransitStopImpl();
		
		stop.setCode(01);
		stop.setDesc("desc");
		stop.setId(-1L);
		stop.setName("Barton");
		stop.setParentStation(21);
		stop.setUrl("http://");
		
		Coordinate coord = new Coordinate(-70.0,49.0);
		stop.setLocation(factory.createPoint(coord));
		
		List<TransitStop> aList = new ArrayList<TransitStop>();
		aList.add(stop);
		
		RouteStopData rs = new RouteStopDataImpl();
		
		rs.setRouteShortName("42");
		rs.setTripHeadSign("Balston");
		
		List<RouteStopData> rsList = new ArrayList<RouteStopData>();
		rsList.add(rs);
		stop.setRoutes(rsList);
		
		obj.setStops(aList);
		
		this.writeObject(obj);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GenerateExampleXml xml = new GenerateExampleXml();
		
		xml.writeCircleQuery();
		xml.writeRouteRequest();
		xml.writeRouteGeometryRequesst();
		xml.writeTransiteStopQueryRequest();
		xml.writeGenerateHeatMapRequest();
		xml.writeClusterQueryRequest();
		xml.writeClusterQueryResponse();
		xml.writeGenerateHeatMapResponse();
		xml.writeRouteResponse();
		xml.writeRouteGeometryResponse();
		xml.writeTransiteStopQueryResponse();
		
		System.out.println("Done");
	}

}
