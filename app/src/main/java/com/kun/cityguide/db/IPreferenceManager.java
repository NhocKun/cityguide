package com.kun.cityguide.db;

import android.content.SharedPreferences;

/**
 * Created by kunmyt on 4/20/16.
 */
public interface IPreferenceManager {
    SharedPreferences getPreferences();

    SharedPreferences.Editor editPreferences();

    void SetValue(String key, String value);

    String getStringValue(String key);
}
