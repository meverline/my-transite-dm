package me.datamining.types;

import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@JsonRootName("HourOfDay")
@Data
public class HourOfDay {

    private int hour;
    private int minute;

    public HourOfDay() {}

    public HourOfDay(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public long toLong() {
        String formatted = String.format("%02d%02d", getHour(), getHour());
        return Long.parseLong(formatted);
    }
}
