package org.dm.transit.metric;

import lombok.extern.apachecommons.CommonsLog;
import me.database.nsstore.DocumentSession;
import me.datamining.DataMiningJob;
import me.datamining.types.MetricTypes;
import me.transit.dao.AgencyDao;
import me.transit.dao.TransiteStopDao;
import org.springframework.stereotype.Component;

@Component(value="metricFactory")
@CommonsLog
public class MetricFactory {

    private final AgencyDao agencyDao;
    private final TransiteStopDao transiteStopDao;
    private final DocumentSession documentSession;

    /**
     *
     * @param agencyDao
     * @param transiteStopDao
     * @param documentSession
     */
    protected MetricFactory(AgencyDao agencyDao,
                            TransiteStopDao transiteStopDao,
                            DocumentSession documentSession)
    {
        this.agencyDao = agencyDao;
        this.transiteStopDao = transiteStopDao;
        this.documentSession = documentSession;
    }

    /**
     *
     * @param job
     * @return
     */
    public DataMiningMetric create(DataMiningJob job) {
        if ( job.getMetricType()  == MetricTypes.ServiceFrequencyAtStop ) {
            return new ServiceFrequencyAtStopMetric(job,
                                                    this.agencyDao,
                                                    this.transiteStopDao,
                                                    this.documentSession);
        } else if ( job.getMetricType() == MetricTypes.ServiceDateSample) {
            return new StopQueryMetric(job,
                                        this.agencyDao,
                                        this.transiteStopDao,
                                        this.documentSession);
        }

        throw new IllegalArgumentException("Unknown Metric Type: " + job.getMetricType().toString());
    }
}
