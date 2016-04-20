package com.kun.cityguide.callbacks;

import com.kun.cityguide.weather.cmn.ForecastDay;

import java.util.List;

/**
 * Created by dobrikostadinov on 6/17/15.
 */
public interface CallbackForecastLoaded {

    void doneLoadingForecast(List<ForecastDay> result);
    void showLoadingWeather();
}
