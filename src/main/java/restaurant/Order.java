package restaurant;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Order {
    protected final int tableNumber;
    protected Map<String,Object> other = new HashMap<>();

     private long timestamp;

    @JsonCreator
    public Order(@JsonProperty("tableNumber") int tableNumber)
    {
        this.tableNumber = tableNumber;
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
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

    public void setDodgy(boolean dodgy) {
        set("dodgy", dodgy);
    }

    public boolean getDodgy() {
        return this.get("dodgy") != null;
    }

    public void setPaid(boolean paid) {
        set("paid", paid);
    }

    public void setPaymentMethod(String paymentMethod) {
        set("paymentMethod", paymentMethod);
    }

    public void setItems(List<MenuItem> items) {
        set("lineItems", items.toArray());

    }

    @JsonIgnore
    public Object[] getLineItems() {
        return (Object[]) get("lineItems");
    }

    public void setIngredients(Set<String> ingredients) {
        set("ingredients", ingredients);
    }
}
