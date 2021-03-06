package me.datamining;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ComputeTile.class, name = "ComputeTile"),
        @JsonSubTypes.Type(value = PopulateTile.class, name = "PopulateTile")
})
public class TileJob {

    private String jobNumber;
    private int  tileNumber;

    public TileJob() {}

    public TileJob(String jobNumber, int tileNumber) {
        this.setJobNumber(jobNumber);
        this.setTileNumber(tileNumber);
    }

    @JsonGetter("jobNumber")
    public String getJobNumber() {
        return jobNumber;
    }

    @JsonSetter("jobNumber")
    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    @JsonGetter("tileNumber")
    public int getTileNumber() {
        return tileNumber;
    }

    @JsonSetter("tileNumber")
    public void setTileNumber(int tileNumber) {
        this.tileNumber = tileNumber;
    }
}
