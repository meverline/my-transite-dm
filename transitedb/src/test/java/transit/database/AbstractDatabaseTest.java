package transit.database;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.math.Vertex;
import me.math.kdtree.MinBoundingRectangle;
import me.transit.database.Agency;
import org.junit.Before;

public class AbstractDatabaseTest {

    protected final Vertex ul = new Vertex(38.941, -77.286);
    protected final Vertex lr = new Vertex(38.827, -77.078);
    protected final ObjectMapper mapper = new ObjectMapper();

    protected MinBoundingRectangle mbr = new MinBoundingRectangle(ul);

    @Before
    public void setUp() {
        mbr = new MinBoundingRectangle(ul);
        mbr.extend(lr);

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public Agency createAgency() {
        Agency object = new Agency();
        object.setFareUrl("https://www.coursera.org/learn/blockchain-basics?authMode=login");
        object.setId("id");
        object.setLang("lang");
        object.setName("name");
        object.setPhone("703-123-4567");
        object.setTimezone("EST");
        object.setUUID(100L);
        object.setVersion("0.5");
        object.setMBR(mbr.toPolygon());
        return object;
    }
}
