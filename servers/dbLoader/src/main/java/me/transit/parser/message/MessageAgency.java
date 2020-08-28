package me.transit.parser.message;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class MessageAgency {
	
	private String location;
	private String feed;
	
	@JsonGetter("location")
	public String getLocation() {
		return location;
	}
	
	@JsonSetter("location")
	public void setLocation(String location) {
		this.location = location;
	}
	
	@JsonGetter("feed")
	public String getFeed() {
		return feed;
	}
	
	@JsonSetter("feed")
	public void setFeed(String feed) {
		this.feed = feed;
	}

	@Override
	public String toString() {
		return "MessageAgency{" +
				"location='" + location + '\'' +
				", feed='" + feed + '\'' +
				'}';
	}
}
