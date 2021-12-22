package org.dm.transit.metric;

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;
import me.database.nsstore.DocumentSession;
import me.datamining.DataMiningJob;
import me.transit.dao.AgencyDao;
import me.transit.dao.TransiteStopDao;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

public class StopQueryMetric extends  DataMiningMetric {

    private Log logger = LogFactory.getLog(StopQueryMetric.class.getSimpleName());

    public StopQueryMetric(DataMiningJob job,
                           AgencyDao agencyDao,
                           TransiteStopDao transiteStopDao,
                           DocumentSession documentSession)
    {
        super(job, agencyDao, transiteStopDao, documentSession);
    }

    protected int computeMetric(TransitStop stop, Trip trip) {
        if (this.coversServiceDate(trip.getService())) {
            return 1;
        }
        return 0;
    }

}
