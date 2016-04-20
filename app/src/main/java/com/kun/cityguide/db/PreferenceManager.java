package com.kun.cityguide.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.kun.cityguide.util.Constant;

/**
 * Created by kunmyt on 4/20/16.
 */
public class PreferenceManager implements IPreferenceManager {
    private Context context;


    public PreferenceManager(Context context) {
        this.context = context;
    }

    @Override
    public SharedPreferences getPreferences() {
        return context.getSharedPreferences(Constant.CONFIG, Context.MODE_PRIVATE);
    }

    @Override
    public SharedPreferences.Editor editPreferences() {
        return getPreferences().edit();
    }

    @Override
    public void SetValue(String key, String value) {
        editPreferences().putString(key, value).apply();
    }

    @Override
    public String getStringValue(String key) {
        return getPreferences().getString(key, "");
    }

}
