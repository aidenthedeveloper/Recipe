package com.example.recipe.Model;

import java.io.Serializable;
import java.util.List;

public class Ingredients implements Serializable {

    public String ingredients;
    public String amount;
    public String totalIng;

    public Ingredients(){}

    public Ingredients(String ingredients, String amount, String totalIng){
        this.ingredients = ingredients;
        this.amount = amount;
        this.totalIng = totalIng;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalIng() {
        return totalIng;
    }

    public void setTotalIng(String totalIng) {
        this.totalIng = totalIng;
    }

    public String toString(){
        return this.totalIng;
    }

}
