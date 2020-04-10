package me.transit.parser.message;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ParserMessage {

	private List<MessageAgency> agencys = new ArrayList<>();
	
	@JsonGetter("agencys")
	public List<MessageAgency> getAgencys() {
		return agencys;
	}

	@JsonSetter("agencys")
	public void setAgencys(List<MessageAgency> agencys) {
		this.agencys = agencys;
	}
	
}
