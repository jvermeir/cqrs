package restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

/**
 * User: mickdudley
 * Date: 14/12/2015
 */
public class Order {
    private JsonNode order;
    ObjectMapper mapper;

    public Order(String json) throws IOException {
        mapper = new ObjectMapper();

        order = mapper.readTree(json);
    }

    public Order(int tableNumber) {
        mapper = new ObjectMapper();

        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

        order = nodeFactory.objectNode();


        this.setTableNumber(tableNumber);

    }

    public double getTotal() {
        return order.at("/total").asDouble();
    }

    public void setCookTime(int cookTime) {
        ((ObjectNode) order).put("timeToCook", cookTime);
    }

    public void setTableNumber(int tableNumber) {
        ((ObjectNode) order).put("tableNumber", tableNumber);
    }

    public void addSubTotal(double subTotal) {
        ((ObjectNode) order).put("subTotal", subTotal);
    }

    public void addTotal(double total) {
        ((ObjectNode) order).put("total", total);
    }

    public void addTax(double tax) {
        ((ObjectNode) order).put("tax", tax);
    }

    public void setPaid(boolean paid) {
        ((ObjectNode) order).put("paid", paid);
    }

    public void setPaymentMethod(String paymentMethod) {
        ((ObjectNode) order).put("paymentMethod", paymentMethod);
    }

    public String print() throws JsonProcessingException {
        return mapper.writeValueAsString(order);
    }
}
