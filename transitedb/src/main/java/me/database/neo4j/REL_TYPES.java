package me.database.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum REL_TYPES implements RelationshipType {
	HAS_STOP, AGENCY, LOCATION, HAS_A, HEAD_SIGN
}
