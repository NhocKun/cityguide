package com.kun.cityguide.tasks;

import android.os.AsyncTask;

import com.kun.cityguide.MainActivity;
import com.kun.cityguide.callbacks.CallbackForecastLoaded;
import com.kun.cityguide.db.IPreferenceManager;
import com.kun.cityguide.db.PreferenceManager;
import com.kun.cityguide.util.Constant;
import com.kun.cityguide.weather.WeatherManager;
import com.kun.cityguide.weather.cmn.ForecastDay;

import java.util.Date;
import java.util.List;

/**
 * Created by dobrikostadinov on 6/17/15.
 */
public class LoadForecast extends AsyncTask<Void, Void, Void> {

    public static final long ONE_DAY_IN_MILISEC = 1000 * 60 * 60 * 24;

    private MainActivity mMainActivity;

    private CallbackForecastLoaded mCallbackForecastLoaded;

    private List<ForecastDay> mResult;

    public LoadForecast(MainActivity mainActivity, CallbackForecastLoaded callbackForecastLoaded) {
        mMainActivity = mainActivity;
        mCallbackForecastLoaded = callbackForecastLoaded;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mCallbackForecastLoaded.showLoadingWeather();
        //mMainActivity.showLoadingDialog();

    }

    @Override
    protected Void doInBackground(Void... params) {

        IPreferenceManager manager = new PreferenceManager(mMainActivity);
        WeatherManager weatherManager = new WeatherManager(manager.getStringValue(Constant.TOWN), manager.getStringValue(Constant.COUNTRY));
        if (weatherManager.getResult() != null) {
            mResult = weatherManager.getResult();
        } else {
            mResult = weatherManager.getForecast();
        }

        for (int i = 0; i < mResult.size(); i++) {
            mResult.get(i).setDate(new Date(new Date().getTime() + i * ONE_DAY_IN_MILISEC));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        mCallbackForecastLoaded.doneLoadingForecast(mResult);
    }
}