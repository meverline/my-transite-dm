package me.transit.database;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import me.transit.database.TransitStop.LocationType;
import me.transit.database.TransitStop.WheelChariBoardingType;
import org.locationtech.jts.geom.Point;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TransitStop.class)
public abstract class TransitStop_ extends me.transit.database.TransitData_ {

	public static volatile SingularAttribute<TransitStop, Integer> parentStation;
	public static volatile SingularAttribute<TransitStop, WheelChariBoardingType> wheelchairBoarding;
	public static volatile SingularAttribute<TransitStop, String> code;
	public static volatile SingularAttribute<TransitStop, String> name;
	public static volatile SingularAttribute<TransitStop, String> zoneId;
	public static volatile SingularAttribute<TransitStop, LocationType> locationType;
	public static volatile SingularAttribute<TransitStop, Point> location;
	public static volatile SingularAttribute<TransitStop, String> id;
	public static volatile SingularAttribute<TransitStop, Long> uuid;
	public static volatile SingularAttribute<TransitStop, String> url;
	public static volatile SingularAttribute<TransitStop, String> desc;

	public static final String PARENT_STATION = "parentStation";
	public static final String WHEELCHAIR_BOARDING = "wheelchairBoarding";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String ZONE_ID = "zoneId";
	public static final String LOCATION_TYPE = "locationType";
	public static final String LOCATION = "location";
	public static final String ID = "id";
	public static final String UUID = "uuid";
	public static final String URL = "url";
	public static final String DESC = "desc";

}

