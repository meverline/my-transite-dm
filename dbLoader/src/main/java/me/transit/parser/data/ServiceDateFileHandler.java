package me.transit.parser.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.transit.dao.ServiceDateDao;
import me.transit.database.ServiceDate;

@Component(value="serviceDateFileHandler")
public class ServiceDateFileHandler extends AbstractFileHandler {

	private Log log = LogFactory.getLog(getClass().getName());
	private ServiceDateDao serviceDao;

	/**
	 * 
	 * @param blackboard
	 */
	@Autowired
	public ServiceDateFileHandler(ServiceDateDao serviceDao, Blackboard blackboard) {
		super(blackboard);
		this.serviceDao = Objects.requireNonNull(serviceDao, "serviceDao can not be null");
	}
	
	/*
	 * 
	 */
	@Override
	public String handlesFile() {
		return "calendar.txt";
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
	
	/**
	 * 
	 * @param sat
	 * @param sun
	 * @param weekday
	 * @return
	 */
	private ServiceDate.ServiceDays getServiceDate(boolean sat, boolean sun, boolean weekday)
	{
		ServiceDate.ServiceDays rtn = ServiceDate.ServiceDays.ALL_WEEK;
		if (sat && sun && weekday) {
			rtn =  ServiceDate.ServiceDays.ALL_WEEK;
		} else if (weekday) {
			rtn = ServiceDate.ServiceDays.WEEKDAY_SERVICE;
		} else if (sat && weekday) {
			rtn = ServiceDate.ServiceDays.WEEKDAY_SAT_SERVICE;
		} else if (sat && sun) {
			rtn =  ServiceDate.ServiceDays.WEEKEND_SERVICE;
		} else if (sat) {
			rtn =  ServiceDate.ServiceDays.SATURDAY_SERVICE;
		} else if (sun) {
			rtn = ServiceDate.ServiceDays.SUNDAY_SERVICE;
		}
		return rtn;
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
					if (indexMap.get(AbstractFileHandler.ID) != null) {
						id = data[indexMap.get(AbstractFileHandler.ID)].replace('"', ' ').trim();
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

					sd.setService(this.getServiceDate(sat, sun, weekday));
					sd.setServiceDayFlag(serviceFlag);

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
