package me.datamining;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;
import me.math.Vertex;

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
