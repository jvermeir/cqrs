package restaurant;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Cook implements HandleOrder{
    private HandleOrder handler;
    // Database of recipes
    ImmutableMap<String, Recipe> cookBook = ImmutableMap.<String, Recipe>builder()
            .put("pizza", new Recipe("pizza", new String[] {"flour", "salt", "yeast", "water", "tomato sauce"}))
            .put("cake", new Recipe("cake", new String[] {"flour", "egg", "sugar", "chocolate"}))
            .put("coke", new Recipe("coke", new String[] {"sugar", "water"}))
            .put("razor blade pizza", new Recipe("razor blade pizza", new String[] {"foo", "bar", "baz"}))
            .build();
    private String name;
    private TopicBasedPubSub bus = null;

    public void setName(String name) {
        this.name = name;
    }

    private int sleepTime;
    public String getName() {
        return name;
    }

    public Cook (HandleOrder handler, String name) {
        this.handler = handler;
        this.name = name;
        this.sleepTime = Math.toIntExact(Math.round(500 * Math.random()));
    }

    public Cook (String name, TopicBasedPubSub bus) {
        this.name = name;
        this.sleepTime = Math.toIntExact(Math.round(500 * Math.random()));
        this.bus = bus;
    }


    @Override
    public void handle(Order order) {
        System.out.println(getClass().getSimpleName() + " handle, cook: " + name);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("Help");
        }
        order.setCookTime (sleepTime);
        // Add ingredients
        Set<String> ingredients = new HashSet<>();
        for (Object item : order.getLineItems()) {
            Recipe recipe = cookBook.get(((MenuItem) item).getName());
            if (recipe != null) {
                Collections.addAll(ingredients, recipe.getIngredients());
            }
            else {
                throw new RuntimeException("Don't know how to cook " + ((MenuItem) item).getName());
            }
        }
        order.setIngredients(ingredients);
        if (bus == null) {
            handler.handle(order);
        }
        else {
            bus.publish("orderCooked", order);
        }
    }
}
