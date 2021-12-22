package me.datamining;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ComputeTile.class, name = "ComputeTile"),
        @JsonSubTypes.Type(value = PopulateTile.class, name = "PopulateTile")
})
@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TileJob {

    private String jobNumber;
    private int  tileNumber;

    public TileJob() {}

    public TileJob(String jobNumber, int tileNumber) {
        this.jobNumber = jobNumber;
        this.tileNumber = tileNumber;
    }
}
