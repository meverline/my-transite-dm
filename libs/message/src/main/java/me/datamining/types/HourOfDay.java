package me.datamining.types;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@JsonRootName("HourOfDay")
@Data
@Jacksonized
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
