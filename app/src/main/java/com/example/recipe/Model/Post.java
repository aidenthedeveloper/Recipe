package com.example.recipe.Model;

import java.util.List;
import java.util.Map;

public class Post {
    private String postid;
    private String postimage;
    private String caption;
    private String publisher;
    private String title;
    private String serving;
    private String cookTime;
    private String type;
    private String lowerTitle;
    private String date;
    private String year;
    private String month;
    private String cooktime;
    List<String> ingredients;
    List<String> steps;



    public Post(String postid, String postimage, String caption, String publisher,String title, String serving, String cookTime,List<String> ingredients
            ,String type,String lowerTitle,List<String> steps,String date,String year,String Ingredients,String month,
                String cooktime) {
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
        this.date = date;
        this.year = year;
        this.month = month;
        this.cooktime = cooktime;
    }

    public Post() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
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

    public String getLowerTitle() {
        return lowerTitle;
    }

    public void setLowerTitle(String lowerTitle) {
        this.lowerTitle = lowerTitle;
    }
}