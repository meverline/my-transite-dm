package me.database.neo4j.visitors;

import me.database.neo4j.AbstractGraphNode;
import me.database.neo4j.FIELD;
import me.transit.database.TransitStop;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TransiteStopVisitor extends AbstractGraphNode {

    private  final TransitStop stop;

    public TransiteStopVisitor(TransitStop stop) {
        this.stop = Objects.requireNonNull(stop, "stop is required");
    }

    @Override
    public String getId() {
        return this.stop.getId();
    }

    /*
     * (non-Javadoc)
     * @see me.database.neo4j.IGraphNode#getProperties()
     */
    @Override
    public Map<String, String> getProperties(String agencyName) {
        Map<String, String> node = new HashMap<>();

        node.put( FIELD.stop.name(), makeKey(agencyName));
        node.put( FIELD.db_name.name(), stop.getName());
        node.put( FIELD.db_id.name(), stop.getId());
        node.put(FIELD.className.name(), stop.getClass().getSimpleName());
        node.put(FIELD.coordinate.name(),this.makeCoordinateKey());
        return node;
    }

    public String makeCoordinateKey() {
        return  stop.getLocation().getX() + ","+  stop.getLocation().getY();
    }

}
