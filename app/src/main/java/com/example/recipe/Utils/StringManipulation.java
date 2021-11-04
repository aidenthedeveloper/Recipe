package com.example.recipe.Utils;

public class StringManipulation {

    public static String expandUsername(String username){
        return username.replace(".","");
    }

    public static String expandPhoneNumber(String phoneNo){
        return phoneNo.replace(".","");
    }


    public static String condenseUsername(String username){
        return username.replace("",".");
    }
}
