package me.database.neo4j.visitors;

import me.database.neo4j.AbstractGraphNode;
import me.database.neo4j.FIELD;
import me.transit.database.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RouteVisitor extends AbstractGraphNode {

    private final Route route;

    public RouteVisitor(Route route) {
        this.route = Objects.requireNonNull(route, "route is required");
    }

    @Override
    public String getId() {
        return this.route.getId();
    }

    /*
     * (non-Javadoc)
     * @see me.database.neo4j.IGraphNode#getProperties()
     */
    @Override
    public Map<String, String> getProperties(String agencyName) {
        Map<String, String> node = new HashMap<>();
        node.put(FIELD.route.name(), makeKey(agencyName));
        node.put(FIELD.db_name.name(), route.getShortName());
        node.put(FIELD.db_id.name(), route.getId());
        node.put(FIELD.className.name(), route.getClass().getSimpleName());
        return node;
    }

    /*
     * (non-Javadoc)
     * @see me.database.neo4j.AbstractGraphNode#makeKey(me.transit.database.TransitData)
     */
    @Override
    public String makeKey(String agencyName) {
        return route.getShortName() + "@" + route.getAgency().getName();
    }

}
