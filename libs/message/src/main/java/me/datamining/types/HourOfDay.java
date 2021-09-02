package me.datamining.types;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonRootName("HourOfDay")
public class HourOfDay {

    private int hour;
    private int minute;

    public HourOfDay() {}

    public HourOfDay(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @JsonGetter("hour")
    public int getHour() {
        return hour;
    }

    @JsonSetter("hour")
    public void setHour(int hour) {
        this.hour = hour;
    }

    @JsonGetter("minute")
    public int getMinute() {
        return minute;
    }

    @JsonSetter("minute")
    public void setMinute(int minute) {
        this.minute = minute;
    }
}
