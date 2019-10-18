package me.transit.json;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.util.StdConverter;

import me.factory.DaoBeanFactory;
import me.transit.dao.AgencyDao;
import me.transit.database.Agency;

public class StringToAgency extends StdConverter<String, Agency> {

	private final Log log = LogFactory.getLog(getClass().getName());
	
	@Override
	public Agency convert(String value) {
		Agency rtn = null;
		String name = value.substring(value.indexOf(' ')+1);
		
		try {
			AgencyDao dao = AgencyDao.class.cast(DaoBeanFactory.create().getDaoBean(AgencyDao.class));
			rtn = dao.findByName(name);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return rtn;
	}

}