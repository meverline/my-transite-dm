package me.transit.database;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import org.locationtech.jts.geom.Polygon;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Agency.class)
public abstract class Agency_ {

	public static volatile SingularAttribute<Agency, String> phone;
	public static volatile SingularAttribute<Agency, String> timezone;
	public static volatile SingularAttribute<Agency, String> fareUrl;
	public static volatile SingularAttribute<Agency, String> name;
	public static volatile SingularAttribute<Agency, String> id;
	public static volatile SingularAttribute<Agency, String> lang;
	public static volatile SingularAttribute<Agency, Polygon> mbr;
	public static volatile SingularAttribute<Agency, Long> uuid;
	public static volatile SingularAttribute<Agency, String> version;
	public static volatile SingularAttribute<Agency, String> url;

	public static final String PHONE = "phone";
	public static final String TIMEZONE = "timezone";
	public static final String FARE_URL = "fareUrl";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String LANG = "lang";
	public static final String MBR = "mbr";
	public static final String UUID = "uuid";
	public static final String VERSION = "version";
	public static final String URL = "url";

}

