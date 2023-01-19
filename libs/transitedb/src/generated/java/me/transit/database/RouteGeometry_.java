package me.transit.database;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.locationtech.jts.geom.Geometry;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(RouteGeometry.class)
public abstract class RouteGeometry_ extends me.transit.database.TransitData_ {

	public static volatile SingularAttribute<RouteGeometry, Geometry> shape;
	public static volatile SingularAttribute<RouteGeometry, String> id;
	public static volatile SingularAttribute<RouteGeometry, Long> uuid;

	public static final String SHAPE = "shape";
	public static final String ID = "id";
	public static final String UUID = "uuid";

}

