package me.transit.parser.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import me.factory.DaoBeanFactory;
import me.transit.dao.ServiceDateDao;
import me.transit.database.ServiceDate;

public class ServiceDateFileHandler extends FileHandler {

	private Log log = LogFactory.getLog(getClass().getName());

	/**
	 * 
	 * @param blackboard
	 */
	public ServiceDateFileHandler(Blackboard blackboard) {
		super(blackboard);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	private Calendar convertDate(String date) {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6)) - 1;
		int day = Integer.parseInt(date.substring(6));

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal;
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public boolean convertToBoolean(String data) {
		if (data.trim().compareTo("1") == 0) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see me.transit.parser.data.FileHandler#parse(java.lang.String)
	 */
	@Override
	public void parse(String shapeFile) {
		try {

			File fp = new File(shapeFile);
			if (!fp.exists()) {
				return;
			}

			BufferedReader inStream = new BufferedReader(new FileReader(shapeFile));
			if (!inStream.ready()) {
				inStream.close();
				return;
			}
			List<String> header = new ArrayList<String>();
			Map<String, Integer> indexMap = processHeader(inStream.readLine(), "service", header);

			while (inStream.ready()) {
				String line = inStream.readLine();
				if (line.trim().length() > 0 && line.indexOf(',') != -1) {
					String data[] = line.split(",");

					String id = null;
					if (indexMap.get(FileHandler.ID) != null) {
						id = data[indexMap.get(FileHandler.ID)].replace('"', ' ').trim();
					} else {
						if (indexMap.get("serviceId") == null) {
							id = data[0].replace('"', ' ').trim();
						} else {
							id = data[indexMap.get("serviceId")].replace('"', ' ').trim();
						}
					}

					ServiceDate sd = new ServiceDate();

					sd.setId(id.trim());
					sd.setStartDate(this.convertDate(data[indexMap.get("StartDate")].replace('"', ' ').trim()));
					sd.setEndDate(this.convertDate(data[indexMap.get("EndDate")].replace('"', ' ').trim()));
					sd.setAgency(getBlackboard().getAgency());

					int serviceFlag = 0;
					boolean weekday = false, sat = false, sun = false;
					if (this.convertToBoolean(data[indexMap.get("Sunday")])) {
						serviceFlag |= ServiceDate.WeekDay.SUNDAY.getBit();
						sun = true;
					}
					if (this.convertToBoolean(data[indexMap.get("Monday")])) {
						serviceFlag |= ServiceDate.WeekDay.MONDAY.getBit();
						weekday |= true;
					}
					if (this.convertToBoolean(data[indexMap.get("Tuesday")])) {
						serviceFlag |= ServiceDate.WeekDay.TUESDAY.getBit();
						weekday |= true;
					}
					if (this.convertToBoolean(data[indexMap.get("Wednesday")])) {
						serviceFlag |= ServiceDate.WeekDay.WENSDAY.getBit();
						weekday |= true;
					}
					if (this.convertToBoolean(data[indexMap.get("Thursday")])) {
						serviceFlag |= ServiceDate.WeekDay.THURSDAY.getBit();
						weekday |= true;
					}
					if (this.convertToBoolean(data[indexMap.get("Friday")])) {
						serviceFlag |= ServiceDate.WeekDay.FRIDAY.getBit();
						weekday |= true;
					}
					if (this.convertToBoolean(data[indexMap.get("Saturday")])) {
						serviceFlag |= ServiceDate.WeekDay.SATURDAY.getBit();
						sat = true;
					}

					if (sat && sun && weekday) {
						sd.setService(ServiceDate.ServiceDays.ALL_WEEK);
					} else if (weekday) {
						sd.setService(ServiceDate.ServiceDays.WEEKDAY_SERVICE);
					} else if (sat && weekday) {
						sd.setService(ServiceDate.ServiceDays.WEEKDAY_SAT_SERVICE);
					} else if (sat && sun) {
						sd.setService(ServiceDate.ServiceDays.WEEKEND_SERVICE);
					} else if (sat) {
						sd.setService(ServiceDate.ServiceDays.SATURDAY_SERVICE);
					} else if (sun) {
						sd.setService(ServiceDate.ServiceDays.SUNDAY_SERVICE);
					}

					sd.setServiceDayFlag(serviceFlag);

					ServiceDateDao serviceDao = ServiceDateDao.class
							.cast(DaoBeanFactory.create().getDaoBean(ServiceDateDao.class));
					serviceDao.save(sd);
					getBlackboard().getService().put(sd.getId(), sd);
				}
			}
			inStream.close();

		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}

	}

}