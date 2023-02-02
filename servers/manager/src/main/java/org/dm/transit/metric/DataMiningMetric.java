package org.dm.transit.metric;

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;
import lombok.Data;
import me.database.nsstore.DocumentSession;
import me.database.nsstore.IDocument;
import me.datamining.DataMiningJob;
import me.datamining.mapreduce.DataResult;
import me.datamining.shapes.Circle;
import me.datamining.shapes.Polygon;
import me.datamining.shapes.Rectanlge;
import me.datamining.shapes.Shape;
import me.math.Vertex;
import me.transit.dao.AgencyDao;
import me.transit.dao.TransiteStopDao;
import me.transit.dao.query.StopQueryConstraint;
import me.transit.dao.query.tuple.IQueryTuple;
import me.transit.dao.query.tuple.StringTuple;
import me.transit.database.*;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Point;

@Data
public abstract class DataMiningMetric {

    private Log logger = LogFactory.getLog(StopQueryMetric.class.getSimpleName());
    private final AgencyDao agencyDao;
    private final TransiteStopDao transiteStopDao;
    private final DataMiningJob job;
    private final DocumentSession documentSession;

    protected DataMiningMetric(DataMiningJob job,
                               AgencyDao agencyDao,
                               TransiteStopDao transiteStopDao,
                               DocumentSession documentSession)
    {
        this.job = job;
        this.agencyDao = agencyDao;
        this.transiteStopDao = transiteStopDao;
        this.documentSession = documentSession;
    }

    protected abstract int computeMetric(TransitStop stop, Trip trip);

    /**
     * caclulate the metric at each of the transite stops based upon the
     * job inputs
     * @param stops list of stops from the spatial query.
     * @return DataResults.
     */
    protected  List<DataResult> results(List<TransitStop> stops){
        List<DataResult> rtn = new ArrayList<>();
        List<IQueryTuple> queryTuples = new ArrayList<>();

        for ( TransitStop stop : stops) {
            queryTuples.clear();
            queryTuples.add( new StringTuple("agency", stop.getAgency().getName(), StringTuple.MATCH.EXACT));
            queryTuples.add( new StringTuple("stop_id", stop.getId(), StringTuple.MATCH.EXACT));

            long count = 0;
            List<IDocument> documents = getDocumentSession().find(queryTuples, DocumentSession.DATABASE);
            for (IDocument doc : documents) {
                RouteDocument route = RouteDocument.class.cast(doc);
                for ( Trip trip : route.getTrips()) {
                    count += this.computeMetric(stop, trip);
                }
            }

            if ( count > 0 ) {
                rtn.add( new DataResult(new Vertex(stop.getLocation()), count));
            }
        }

        return rtn;
    }
    
    protected void setQueryShape(StopQueryConstraint query, Shape shape)
    {
    	if ( shape instanceof Circle) {
    		Circle circle = Circle.class.cast(shape);
    		query.addCircleConstriant(circle.getCenter().toPoint(), 
    								  circle.getDistanceInMeters());
    		
    	} else if ( shape instanceof Rectanlge ) {
    		Rectanlge rectangle = Rectanlge.class.cast(shape);
    		query.addRectangleConstraint(rectangle.getUpperLeft().toPoint(),
    									 rectangle.getLowerRight().toPoint());
    		
    	} else if ( shape instanceof Polygon ) { 
    		Polygon polygon = Polygon.class.cast(shape);
    		
    		List<Point> pointList = new ArrayList<>();
    		for (Vertex v : polygon.getCoordinates()) {
    			pointList.add(v.toPoint());
    		}
    		query.addPolygonConstraint(pointList);
    	} else {
    		throw new UnsupportedOperationException(shape.getClass().getCanonicalName());
    	}
    }

    /**
     * Get the data results
     * @return
     */
    public List<DataResult> findDataResults() {
        StopQueryConstraint query = new StopQueryConstraint();

        setQueryShape(query, job.getShape());

        for (String name : job.getAgencies()) {
            Agency agency = agencyDao.findByName(name);
            query.addAgency(agency);
        }

        return this.results(transiteStopDao.query(query));
    }
    
    protected long toLong(String data) {
    	return Long.parseLong(data);
    }

    protected int countStopTimes(TransitStop transitStop, List<StopTime> stopTimes  )
    {
        int count = 0;
        long startTime = 0;
        if ( getJob().getStartTime() != null ) {
            startTime = toLong(getJob().getStartTime());
        }

        long endTime = 2359;
        if ( getJob().getEndTime() != null ) {
            endTime = toLong(getJob().getEndTime());
        }

        for ( StopTime stopTime : stopTimes ) {
            if (stopTime.getStopId() == transitStop.getId() ) {
                if (getJob().getStartTime() == null && getJob().getEndTime() == null) {
                    count += stopTime.getArrivalTime().size();
                } else {

                    for (long arrival : stopTime.getArrivalTime()) {
                        if (arrival >= startTime && arrival <= endTime ) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    protected boolean coversServiceDate(ServiceDate serviceDate) {
    	@SuppressWarnings("unused")
        boolean rtn = false;

        if ( getJob().getWeekdays() == null || getJob().getWeekdays().isEmpty() ) {
            return true;
        }
        else {
            for ( DataMiningJob.Day day : getJob().getWeekdays()) {
                ServiceDate.WeekDay weekDay = ServiceDate.WeekDay.valueOf(day.toString());
                if (! serviceDate.hasService(weekDay)) {
                    return false;
                }
            }
        }
        return true;
    }

}
