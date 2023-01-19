package org.dm.transit.metric;

import me.database.nsstore.DocumentSession;
import me.datamining.DataMiningJob;
import me.transit.dao.AgencyDao;
import me.transit.dao.TransiteStopDao;
import me.transit.database.TransitStop;
import me.transit.database.Trip;

public class ServiceFrequencyAtStopMetric extends  DataMiningMetric {

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
