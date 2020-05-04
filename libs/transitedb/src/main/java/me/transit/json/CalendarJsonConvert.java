package me.transit.json;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fasterxml.jackson.databind.util.StdConverter;

public class CalendarJsonConvert extends StdConverter<Calendar, String> implements CalendarSerailizer {


    private final SimpleDateFormat formatter = new SimpleDateFormat(CalendarSerailizer.DATE_TIME_FORMAT);

    @Override
    public String convert(Calendar value) {
        return formatter.format(value.getTime());
    }

}
