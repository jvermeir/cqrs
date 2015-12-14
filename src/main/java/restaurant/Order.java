package restaurant;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Order {
    protected final int tableNumber;
    protected Map<String,Object> other = new HashMap<String,Object>();

    @JsonCreator
    public Order(@JsonProperty("tableNumber") int tableNumber)
    {
        this.tableNumber = tableNumber;
    }

    public int getTableNumber() { return tableNumber; }

    public Object get(String name) {
        return other.get(name);
    }

    // "any getter" needed for serialization
    @JsonAnyGetter
    public Map<String,Object> any() {
        return other;
    }

    @JsonAnySetter
    public void set(String name, Object value) {
        other.put(name, value);
    }

    public double getTotal() {
        return (Double) this.get("total");
    }

    public void setCookTime(int cookTime) {
        set("timeToCook", cookTime);
    }

    public void setTableNumber(int tableNumber) {
        set("tableNumber", tableNumber);
    }

    public void addSubTotal(double subTotal) {
        set("subTotal", subTotal);
    }

    public void addTotal(double total) {
        set("total", total);
    }

    public void addTax(double tax) {
        set("tax", tax);
    }

    public void setPaid(boolean paid) {
        set("paid", paid);
    }

    public void setPaymentMethod(String paymentMethod) {
        set("paymentMethod", paymentMethod);
    }
}
