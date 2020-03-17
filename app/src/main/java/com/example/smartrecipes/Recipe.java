package com.example.smartrecipes;

public class Recipe {
    private String title;
    private String description;
    private String category;

    public Recipe(){

    }

    public Recipe(String title, String descripion, String category) {
        this.title = title;
        this.description = descripion;
        this.category = category;
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

    public void setDescription(String descripion) {
        this.description = descripion;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
