package com.example.recipegenius.PresentationClasses;
public class Recipe {
    private String id;
    private String name;
    private String description;
    private String ingredients;
    private String method;
    private String cuisine;
    private String type;
    private String imageUrl;
    private int likes;
    private boolean recommended;

    // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    public Recipe() {
    }

    public Recipe(String id, String name, String description, String ingredients, String method, String cuisine, String type, String imageUrl, int likes, boolean recommended) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.method = method;
        this.cuisine = cuisine;
        this.type = type;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.recommended = recommended;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }
}
