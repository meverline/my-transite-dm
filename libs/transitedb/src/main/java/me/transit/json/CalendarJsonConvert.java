package me.transit.json;

import com.fasterxml.jackson.databind.util.StdConverter;
import me.transit.database.Agency;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarJsonConvert extends StdConverter<Calendar, String> implements CalendarSerailizer {


    private final SimpleDateFormat formatter = new SimpleDateFormat(CalendarSerailizer.DATE_TIME_FORMAT);

    @Override
    public String convert(Calendar value) {
        return formatter.format(value.getTime());
    }

}
