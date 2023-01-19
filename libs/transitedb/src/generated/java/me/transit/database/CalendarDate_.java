package me.transit.database;

import java.util.Calendar;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import me.transit.database.CalendarDate.ExceptionType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CalendarDate.class)
public abstract class CalendarDate_ extends me.transit.database.TransitData_ {

	public static volatile SingularAttribute<CalendarDate, Calendar> date;
	public static volatile SingularAttribute<CalendarDate, ExceptionType> exceptionType;
	public static volatile SingularAttribute<CalendarDate, String> id;
	public static volatile SingularAttribute<CalendarDate, Long> uuid;

	public static final String DATE = "date";
	public static final String EXCEPTION_TYPE = "exceptionType";
	public static final String ID = "id";
	public static final String UUID = "uuid";

}

