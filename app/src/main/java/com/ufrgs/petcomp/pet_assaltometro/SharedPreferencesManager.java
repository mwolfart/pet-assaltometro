package com.ufrgs.petcomp.pet_assaltometro;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by GuiPC on 17/01/2017.
 * SharedPreferences hold some userdata on the file system
 * It makes storing private data safe (to a certain degree)
 */

public class SharedPreferencesManager {

    //set the given data on sharedpreferences
    public static void setDefaultPreferences(String key, String value, Context context) {
        android.content.SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //get the requested data of sharedpreferences
    public static String getDefaultPreferences(String key, Context context) {
        android.content.SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    //clear all references
    public static void clearDefaultPreferences(Context context) {
        android.content.SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().clear().commit();
    }
}
