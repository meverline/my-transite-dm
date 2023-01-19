package me.transit.database;

import java.util.Calendar;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import me.transit.database.ServiceDate.ServiceDays;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ServiceDate.class)
public abstract class ServiceDate_ extends me.transit.database.TransitData_ {

	public static volatile SingularAttribute<ServiceDate, Calendar> endDate;
	public static volatile SingularAttribute<ServiceDate, ServiceDays> service;
	public static volatile SingularAttribute<ServiceDate, String> id;
	public static volatile SingularAttribute<ServiceDate, Long> uuid;
	public static volatile SingularAttribute<ServiceDate, Integer> serviceDayFlag;
	public static volatile SingularAttribute<ServiceDate, Calendar> startDate;

	public static final String END_DATE = "endDate";
	public static final String SERVICE = "service";
	public static final String ID = "id";
	public static final String UUID = "uuid";
	public static final String SERVICE_DAY_FLAG = "serviceDayFlag";
	public static final String START_DATE = "startDate";

}

