package ru.mobiskif.geo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Storage {

    public static void setCurrentUser(Context c, String user) {
        SharedPreferences defsettings = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor eddef = defsettings.edit();
        eddef.putString("currentUser", user);
        eddef.apply();
    }

    public static String getCurrentUser(Context c) {
        SharedPreferences defsettings = PreferenceManager.getDefaultSharedPreferences(c);
        return defsettings.getString("currentUser","0");
    }

    public static void store(Context c, String key, String value) {
        String currentUser = getCurrentUser(c);
        SharedPreferences settings = c.getSharedPreferences(currentUser, 0);
        SharedPreferences.Editor ed = settings.edit();
        ed.putString(key, value);
        ed.apply();
    }

    public static String restore(Context c, String key) {
        String currentUser = getCurrentUser(c);
        SharedPreferences settings = c.getSharedPreferences(currentUser, 0);
        return settings.getString(key, "");
    }
}
