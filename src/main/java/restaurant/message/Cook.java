package restaurant.message;

import com.google.common.collect.ImmutableMap;
import restaurant.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Cook implements MessageHandler {
    // Database of recipes
    ImmutableMap<String, Recipe> cookBook = ImmutableMap.<String, Recipe>builder()
            .put("pizza", new Recipe("pizza", new String[] {"flour", "salt", "yeast", "water", "tomato sauce"}))
            .put("cake", new Recipe("cake", new String[] {"flour", "egg", "sugar", "chocolate"}))
            .put("coke", new Recipe("coke", new String[] {"sugar", "water"}))
            .put("razor blade pizza", new Recipe("razor blade pizza", new String[] {"foo", "bar", "baz"}))
            .build();
    private String name;
    private MessageBasedPubSub bus = null;

    public void setName(String name) {
        this.name = name;
    }

    private int sleepTime;
    public String getName() {
        return name;
    }

    public Cook(String name, MessageBasedPubSub bus) {
        this.name = name;
        this.sleepTime = Math.toIntExact(Math.round(500 * Math.random()));
        this.bus = bus;
    }

    @Override
    public void handle(OrderMessage message) {
        System.out.println(getClass().getSimpleName() + " handle, cook: " + name);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("Help");
        }
        message.getOrder().setCookTime(sleepTime);
        // Add ingredients
        Set<String> ingredients = new HashSet<>();
        for (Object item : message.getOrder().getLineItems()) {
            Recipe recipe = cookBook.get(((MenuItem) item).getName());
            if (recipe != null) {
                Collections.addAll(ingredients, recipe.getIngredients());
            }
            else {
                throw new RuntimeException("Don't know how to cook " + ((MenuItem) item).getName());
            }
        }
        message.getOrder().setIngredients(ingredients);
        bus.publish(new OrderCookedMessage(message.getOrder()));
    }

}

