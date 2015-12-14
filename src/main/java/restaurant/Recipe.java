package restaurant;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class Recipe {
    protected final String name;
    protected final String[] ingredients;
    protected Map<String,Object> other = new HashMap<>();

    @JsonCreator
    public Recipe(@JsonProperty("name") String name,
                  @JsonProperty("ingredients") String[] ingredients)
    {
        this.name = name;
        this.ingredients = ingredients;
    }

    public String getName() { return name; }

    public String[] getIngredients() { return ingredients; }

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

}
