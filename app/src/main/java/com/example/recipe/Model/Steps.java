package com.example.recipe.Model;

import android.app.Person;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.lang.annotation.Native;
import java.util.Comparator;

public class Steps {

    public String steps;
    public String stepsImage;
    //public int stepsPosition;
    public String stepsPosition;
    private String postid;


    public Steps() {}

    public Steps(String steps, String stepsImage,String stepsPosition,String postid){
        this.steps = steps;
        this.stepsImage = stepsImage;
        this.stepsPosition = stepsPosition;
        this.postid = postid;
    }

   public static Comparator<Steps> StepsPosComparator = new Comparator<Steps>() {
       @Override
       public int compare(Steps steps, Steps t1) {
           return steps.getStepsPosition().compareTo(t1.getStepsPosition());
       }
   };


    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getStepsImage() {
        return stepsImage;
    }

    public void setStepsImage(String stepsImage) {
        this.stepsImage = stepsImage;
    }

    public String toString2(){
        return this.stepsImage;
    }

    public String toString(){
        return this.steps;
    }

    public String toString3(){
        return this.stepsPosition;
    }

    public String getStepsPosition() {
        return stepsPosition;
    }

    public void setStepsPosition(String stepsPosition) {
        this.stepsPosition = stepsPosition;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }


}
