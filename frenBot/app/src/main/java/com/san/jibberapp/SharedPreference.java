package com.san.jibberapp;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    private static final String SharedPreference_name = "my_SharedPreference";

    private static SharedPreference instance;
    private Context context;

    SharedPreference(Context context) {
        this.context = context;
    }

    public static synchronized SharedPreference getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreference(context);

        }
        return instance;
    }

    public void saveName(String name){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("flag", name);
        editor.apply();

    }
    public String getName(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);

        return sharedPreferences.getString("flag", "mybot");
    }





}
