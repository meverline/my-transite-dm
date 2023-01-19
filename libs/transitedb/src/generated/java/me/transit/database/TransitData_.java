package me.transit.database;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TransitData.class)
public abstract class TransitData_ {

	public static volatile SingularAttribute<TransitData, Agency> agency;
	public static volatile SingularAttribute<TransitData, String> version;

	public static final String AGENCY = "agency";
	public static final String VERSION = "version";

}

