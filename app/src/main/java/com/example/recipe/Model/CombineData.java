package com.example.recipe.Model;

import java.util.ArrayList;
import java.util.List;


public class CombineData {

    ArrayList<String> stepsList;
    ArrayList<Integer> stepsPosList;

    public CombineData(ArrayList<String> stepsList,ArrayList<Integer> stepsPosList){
        this.stepsList = stepsList;
        this.stepsPosList = stepsPosList;
    }

    public CombineData(){
    }

    public ArrayList<String> getStepsList() {
        return stepsList;
    }

    public void setStepsList(ArrayList<String> stepsList) {
        this.stepsList = stepsList;
    }

    public ArrayList<Integer> getStepsPosList() {
        return stepsPosList;
    }

    public void setStepsPosList(ArrayList<Integer> stepsPosList) {
        this.stepsPosList = stepsPosList;
    }
}
