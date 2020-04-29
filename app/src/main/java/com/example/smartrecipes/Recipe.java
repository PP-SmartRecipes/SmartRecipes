package com.example.smartrecipes;

import java.util.HashMap;

public class Recipe {
    private String title;
    private String description;
    private String category;
    private HashMap<String, String> ingredients;
    private String imageUrl;
    private String author;

    public Recipe() {

    }

    public Recipe(String title, String description, String category, HashMap<String, String> ingredients, String imageUrl, String author) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public HashMap<String, String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(HashMap<String, String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}