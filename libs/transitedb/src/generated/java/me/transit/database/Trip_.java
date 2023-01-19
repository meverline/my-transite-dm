package me.transit.database;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import me.transit.database.Trip.DirectionType;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Trip.class)
public abstract class Trip_ {

	public static volatile SingularAttribute<Trip, String> headSign;
	public static volatile SingularAttribute<Trip, RouteGeometry> shape;
	public static volatile SingularAttribute<Trip, ServiceDate> service;
	public static volatile SingularAttribute<Trip, DirectionType> directionId;
	public static volatile SingularAttribute<Trip, Integer> routeTripIndex;
	public static volatile SingularAttribute<Trip, String> id;
	public static volatile SingularAttribute<Trip, String> shortName;
	public static volatile SingularAttribute<Trip, Long> uuid;
	public static volatile SingularAttribute<Trip, String> version;

	public static final String HEAD_SIGN = "headSign";
	public static final String SHAPE = "shape";
	public static final String SERVICE = "service";
	public static final String DIRECTION_ID = "directionId";
	public static final String ROUTE_TRIP_INDEX = "routeTripIndex";
	public static final String ID = "id";
	public static final String SHORT_NAME = "shortName";
	public static final String UUID = "uuid";
	public static final String VERSION = "version";

}

