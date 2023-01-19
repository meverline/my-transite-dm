package me.transit.database;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import me.transit.database.IRoute.RouteType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Route.class)
public abstract class Route_ extends me.transit.database.TransitData_ {

	public static volatile SingularAttribute<Route, String> color;
	public static volatile ListAttribute<Route, Trip> trips;
	public static volatile SingularAttribute<Route, Integer> sortOrder;
	public static volatile SingularAttribute<Route, String> id;
	public static volatile SingularAttribute<Route, String> shortName;
	public static volatile SingularAttribute<Route, RouteType> type;
	public static volatile SingularAttribute<Route, Long> uuid;
	public static volatile SingularAttribute<Route, String> textColor;
	public static volatile SingularAttribute<Route, String> url;
	public static volatile SingularAttribute<Route, String> longName;
	public static volatile SingularAttribute<Route, String> desc;

	public static final String COLOR = "color";
	public static final String TRIPS = "trips";
	public static final String SORT_ORDER = "sortOrder";
	public static final String ID = "id";
	public static final String SHORT_NAME = "shortName";
	public static final String TYPE = "type";
	public static final String UUID = "uuid";
	public static final String TEXT_COLOR = "textColor";
	public static final String URL = "url";
	public static final String LONG_NAME = "longName";
	public static final String DESC = "desc";

}

