package me.transit.parser.data;

import lombok.extern.apachecommons.CommonsLog;
import me.database.neo4j.IGraphDatabaseDAO;
import me.transit.dao.AgencyDao;
import me.transit.database.Agency;
import me.transit.parser.data.converters.DataConverterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Objects;

@Component(value = "agencyDataFileHandler")
@CommonsLog
public class AgencyDataFileHandler extends AbstractDefaultFileHandler {

	private final AgencyDao agencyDao;

	@Autowired
	public AgencyDataFileHandler(Blackboard blackboard, AgencyDao agencyDao, IGraphDatabaseDAO graphDatabase, DataConverterFactory dataConverterFactory) {
		super(blackboard, graphDatabase, dataConverterFactory);
		this.agencyDao = Objects.requireNonNull(agencyDao, "agencyDao can not be null");
	}

	/*
	 * 
	 */
	@Override
	public String handlesFile() {
		return "agency.txt";
	}
	
	public void deleteAgencyAndData(Agency saved)
	{
		
	}
	
	/**
	 * 
	 */
	@Override
	public void save(Object obj) throws SQLException {

		Agency current = Agency.class.cast(obj);

		Agency saved = agencyDao.findByName(current.getName());
		if (saved != null) {
			getBlackboard().resetMBR();
			getBlackboard().setAgency(saved);
		} else {
			agencyDao.save(current);
			getGraphDatabase().addNode(current);
			getBlackboard().setAgency(current);
		}
		log.info(current.getName());
		

	}

	@Override
	protected void setAgency(Object obj, Agency agency) {

	}

	@Override
	public void endProcess() {
		Agency agency = this.getBlackboard().getAgency();
		log.info(agency.getName() + " " + agency.getUUID() + " saving mbr " + this.getBlackboard().getMBR());
		agency.setMBR(this.getBlackboard().getMBR().toPolygon());
		try {
			this.save(agency);
		} catch (SQLException e) {
			log.error("Unable to save agency MBR" + e.getLocalizedMessage());
		}
	}
}
