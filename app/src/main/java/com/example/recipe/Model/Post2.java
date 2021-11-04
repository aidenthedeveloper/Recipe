package com.example.recipe.Model;

import java.util.List;

public class Post2 {
    private String postid;
    private String postimage;
    private String caption;
    private String publisher;
    private String title;
    private String serving;
    private String cooktime;
    private String type;
    private String ingredients;
    private String steps;



    public Post2(String postid, String postimage, String caption, String publisher,String title, String serving, String cookTime,String ingredients
            ,String type,String steps) {
        this.postid = postid;
        this.postimage = postimage;
        this.caption = caption;
        this.publisher = publisher;
        this.title = title;
        this.serving = serving;
        this.cooktime = cooktime;
        this.ingredients = ingredients;
        this.type = type;
        this.steps = steps;
    }

    public Post2() {

    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
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

    public String getServing() {
        return serving;
    }

    public void setServing(String serving) {
        this.serving = serving;
    }

    public String getCookTime() {
        return cooktime;
    }

    public void setCookTime(String cooktime) {
        this.cooktime = cooktime;
    }

}