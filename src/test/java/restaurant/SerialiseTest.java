package restaurant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SerialiseTest {

    @Before
    public void setup() {

    }

    @Test
    public void testCanReadJson() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("order1.json").getFile());
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonObj = mapper.readTree(file);
        assertEquals(12, jsonObj.at("/tableNumber").asInt());

    }


}
