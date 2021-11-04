package com.example.recipe.Model;

import java.util.List;

public class Recipe {
    private String postid;
    private String postimage;
    private String caption;
    private String publisher;
    private String title;
    private String ingredients;
    private String steps;
    private List<Ingredients> ingList;


    public Recipe(String postid, String postimage, String caption, String publisher,String title, String ingredients, String steps, List<Ingredients> ingList) {
        this.postid = postid;
        this.postimage = postimage;
        this.caption = caption;
        this.publisher = publisher;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = steps;
        this.ingList = ingList;

    }

    public Recipe() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public List<Ingredients> getIngList() {
        return ingList;
    }

    public void setIngList(List<Ingredients> ingList) {
        this.ingList = ingList;
    }
}