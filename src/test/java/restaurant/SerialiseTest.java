package restaurant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class SerialiseTest {
    private String ORDER_JSON = "{\n" +
            "  \"tableNumber\" : 12,\n" +
            "  \"ingredients\" : [\"foo\", \"bar\", \"baz\"],\n" +
            "  \"lineItems\" : [\n" +
            "    {\n" +
            "      \"test\" : \"razor blade pizza\",\n" +
            "      \"qty\" : 4,\n" +
            "      \"price\" : 9.99\n" +
            "    }\n" +
            "  ],\n" +
            "  \"subTotal\" : 9.99,\n" +
            "  \"tax\" : 1.99,\n" +
            "  \"total\" : 11.98,\n" +
            "  \"paid\" : false,\n" +
            "  \"timeToCook\" : 300,\n" +
            "  \"paymentMethod\" : \"card\"\n" +
            "\n" +
            "}\n";

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


    @Test
    public void canCreateOrder() throws IOException {
        Order order = new Order(ORDER_JSON);
        assertEquals(11.98, order.getTotal(), 0.1);
    }


}
