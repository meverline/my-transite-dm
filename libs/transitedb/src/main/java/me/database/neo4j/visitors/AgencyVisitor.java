package me.database.neo4j.visitors;

import me.database.neo4j.AbstractGraphNode;
import me.database.neo4j.FIELD;
import me.transit.database.Agency;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgencyVisitor extends AbstractGraphNode {

    private final Agency agency;

    public AgencyVisitor(Agency agency) {
        this.agency = Objects.requireNonNull(agency, "agency is required");
    }

    public String getName() {
        return this.agency.getName();
    }

    @Override
    public String getId() {
        return this.agency.getId();
    }

    @Override
    public Map<String, String> getProperties(String agencyName) {
        Map<String, String> node = new HashMap<>();
        node.put(FIELD.agency.name(), agency.getName());
        node.put(FIELD.db_id.name(), agency.getId());
        node.put(FIELD.className.name(), agency.getClass().getName());
        return node;
    }
}
