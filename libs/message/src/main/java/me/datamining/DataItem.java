package me.datamining;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import me.math.Vertex;

@Jacksonized
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("DataItem")
public class DataItem {
	
	private Vertex location = null;
	private double value = 0.0;
	
	public DataItem() {
	}

	public DataItem(Vertex location, double value) {
		this.setLocation(location);
		this.setValue(value);
	}

}
