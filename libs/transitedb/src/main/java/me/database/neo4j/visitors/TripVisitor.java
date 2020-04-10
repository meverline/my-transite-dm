package me.database.neo4j.visitors;

import me.database.neo4j.AbstractGraphNode;
import me.database.neo4j.FIELD;
import me.transit.database.Trip;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TripVisitor extends AbstractGraphNode {

    private final Trip trip;

    public TripVisitor(Trip trip) {
        this.trip = Objects.requireNonNull(trip, "trip is required");
    }

    @Override
    public String getId() {
        return this.trip.getId();
    }

    /*
     * (non-Javadoc)
     * @see me.database.neo4j.IGraphNode#getProperties()
     */
    @Override
    public Map<String, String> getProperties(String agencyName) {
        Map<String, String> node = new HashMap<>();
        node.put(FIELD.trip.name(), makeKey(agencyName));
        node.put(FIELD.db_name.name(), trip.getHeadSign());
        node.put(FIELD.className.name(), trip.getClass().getSimpleName());
        node.put(FIELD.direction.name(), trip.getDirectionId().name());
        node.put(FIELD.trip_headSign.name(), this.makeHeadSignKey(agencyName));
        if (trip.getShortName() != null) {
            node.put(FIELD.db_id.name(), trip.getShortName());
        }

        return node;
    }

    public String makeHeadSignKey(String agencyName) {
        return trip.getHeadSign() + "@" + agencyName;
    }

}
