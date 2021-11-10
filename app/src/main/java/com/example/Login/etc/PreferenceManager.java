package com.example.Login.etc;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREFERENCES_NAME = "User_Token";
    private static final String DEFAULUT_VALUE_STRING = "";



    private static SharedPreferences getPreference(Context context){
        return context.getSharedPreferences(PREFERENCES_NAME,Context.MODE_PRIVATE);
    }

    public static void setString(Context context,String key,String value){
        SharedPreferences prefs = getPreference(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getString(Context context,String key){
        SharedPreferences prefs = getPreference(context);
        String value = prefs.getString(key,DEFAULUT_VALUE_STRING);

        return value;
    }
    public static void removeKey(Context context, String key) {
        SharedPreferences prefs = getPreference(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }







}
