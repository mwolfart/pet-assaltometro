package com.ufrgs.petcomp.pet_assaltometro;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by GuiPC on 17/01/2017.
 */

public class SharedPreferencesManager {

    public static void setDefaultPreferences(String key, String value, Context context) {
        android.content.SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDefaultPreferences(String key, Context context) {
        android.content.SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static void clearDefaultPreferences(Context context) {
        android.content.SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().clear().commit();
    }
}
