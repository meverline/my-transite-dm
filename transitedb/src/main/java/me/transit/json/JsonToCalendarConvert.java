package me.transit.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import me.database.hibernate.AbstractHibernateDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JsonToCalendarConvert extends StdConverter<String, Calendar> implements CalendarSerailizer{

    private Log log = LogFactory.getLog(JsonToCalendarConvert.class);
    private final SimpleDateFormat formatter = new SimpleDateFormat(CalendarSerailizer.DATE_TIME_FORMAT);

    @Override
    public Calendar convert(String value) {
        Calendar rtn = Calendar.getInstance();
        try {
            rtn.setTime(formatter.parse(value));
        } catch (ParseException e) {
            log.error("Invalid time unable to convert: " + value);
        }
        return rtn;
    }

}
