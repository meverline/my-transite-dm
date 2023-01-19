package me.transit.omd.data;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.locationtech.jts.geom.Point;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Location.class)
public abstract class Location_ {

	public static volatile SingularAttribute<Location, String> name;
	public static volatile SingularAttribute<Location, Integer> pid;
	public static volatile SingularAttribute<Location, Point> location;
	public static volatile SingularAttribute<Location, Integer> id;
	public static volatile SingularAttribute<Location, String> title;
	public static volatile SingularAttribute<Location, Long> uuid;

	public static final String NAME = "name";
	public static final String PID = "pid";
	public static final String LOCATION = "location";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String UUID = "uuid";

}

