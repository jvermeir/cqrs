package restaurant;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class MenuItem {
    protected final String name;
    protected final double price;
    protected Map<String,Object> other = new HashMap<String,Object>();

    @JsonCreator
    public MenuItem(@JsonProperty("name") String name,
                    @JsonProperty("price") double price)
    {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }

    public double getPrice() { return price; }

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

    public void setQty(int qty) {
        set("qty", qty);
    }

}
