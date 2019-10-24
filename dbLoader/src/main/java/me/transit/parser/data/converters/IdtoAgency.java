package me.transit.parser.data.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.transit.dao.AgencyDao;
import me.transit.database.Agency;

@Service(value="idToAgency")
public class IdtoAgency extends DataConverter {
	
	private final AgencyDao dao;
		
	@Override
	public Class<?> getType() {
		return Agency.class;
	}

	@Autowired
	public IdtoAgency(AgencyDao agencyDao) {
		this.dao = agencyDao;
		DataConverterFactory.create().register(this);
	}

	@Override
	public Object convert(String data) {
		return dao.findByName(data);
	}
	
	public DataConverter clone() {
		return new IdtoAgency(dao);
	}

}
