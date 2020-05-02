package com.example.smartrecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Recipe {
    private String title;
    private String description;
    private String category;
    private HashMap<String, String> ingredients;
    private String imageUrl;
    private String author;
    private float rating;
    private int numberOfRatings;
    private ArrayList<String> usersRated;

    public Recipe() {

    }

    public Recipe(String title, String description, String category, HashMap<String, String> ingredients, String imageUrl, String author, float rating, int numberOfRatings, ArrayList<String> usersRated) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
        this.author = author;
        this.rating = rating;
        this.numberOfRatings = numberOfRatings;
        this.usersRated = usersRated;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public ArrayList<String> getUsersRated() {
        return usersRated;
    }

    public void setUsersRated(ArrayList<String> usersRated) {
        this.usersRated = usersRated;
    }
}