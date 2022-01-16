package org.dm.transit.metric;

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;
import me.database.nsstore.DocumentSession;
import me.datamining.DataMiningJob;
import me.transit.dao.AgencyDao;
import me.transit.dao.TransiteStopDao;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

public class ServiceFrequencyAtStopMetric extends  DataMiningMetric {

    private Log logger = LogFactory.getLog(StopQueryMetric.class.getSimpleName());

    // Factory method.
    protected ServiceFrequencyAtStopMetric() {
    }

    public ServiceFrequencyAtStopMetric(DataMiningJob job,
                           AgencyDao agencyDao,
                           TransiteStopDao transiteStopDao,
                           DocumentSession documentSession)
    {
        super(job, agencyDao, transiteStopDao, documentSession);
    }

    protected int computeMetric(TransitStop stop, Trip trip) {
        if (this.coversServiceDate(trip.getService())) {
            return this.countStopTimes(stop, trip.getStopTimes());
        }
        return 0;
    }

}
