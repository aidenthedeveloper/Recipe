package com.example.recipe.Databases;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.recipe.HomeActivity;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences usersSession;
    SharedPreferences.Editor editor;
    Context context;


    public static final String SESSION_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "rememberMe";

    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_FULLNAME = "fullName";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PHONENO = "phoneNo";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_BIO = "bio";

    private static final String IS_REMEMBERME = "IsRememberMe";
    public static final String KEY_SESSIONPHONENUMBER = "phoneNo";
    public static final String KEY_SESSIONPASSWORD = "password";


    public SessionManager(Context _context,String sessionName){
        context = _context;
        usersSession = context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }


    public void createLoginSession(String fullName, String username, String phoneNo, String password, String bio){

        editor.putBoolean(IS_LOGIN,true);

        editor.putString(KEY_FULLNAME,fullName);
        editor.putString(KEY_USERNAME,username);
        editor.putString(KEY_PHONENO,phoneNo);
        editor.putString(KEY_PASSWORD,password);
        editor.putString(KEY_BIO,bio);

        editor.commit();
    }

    public HashMap<String, String> getUsersDetailFromSession(){
        HashMap<String,String> userData = new HashMap<String,String>();

        userData.put(KEY_FULLNAME, usersSession.getString(KEY_FULLNAME,null));
        userData.put(KEY_USERNAME, usersSession.getString(KEY_USERNAME,null));
        userData.put(KEY_PHONENO, usersSession.getString(KEY_PHONENO,null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_PASSWORD,null));
        userData.put(KEY_BIO, usersSession.getString(KEY_BIO,null));

        return userData;
    }

    public boolean checkLogin(){
        if(usersSession.getBoolean(IS_LOGIN,false)){
            return true;
        }
        else {
            return false;
        }
    }

    public void logoutUserFromSession(){
        editor.clear();
        editor.commit();
    }

    public void createRememberMeSession(String phoneNo, String password){

        editor.putBoolean(IS_REMEMBERME,true);

        editor.putString(KEY_SESSIONPHONENUMBER,phoneNo);
        editor.putString(KEY_SESSIONPASSWORD,password);

        editor.commit();
    }

    public HashMap<String,String> getRememberMeDetailsFromSession(){
        HashMap<String,String> userData = new HashMap<String,String>();

        userData.put(KEY_SESSIONPHONENUMBER, usersSession.getString(KEY_PHONENO,null));
        userData.put(KEY_SESSIONPASSWORD, usersSession.getString(KEY_PASSWORD,null));

        return userData;
    }

    public boolean checkRememberMe(){
        if(usersSession.getBoolean(IS_REMEMBERME,true)){
            return true;
        }
        else {
            return false;
        }
    }

}
