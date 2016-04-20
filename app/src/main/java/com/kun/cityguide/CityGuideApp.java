package com.kun.cityguide;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by dobri.kostadinov on 8/2/2015.
 */
public class CityGuideApp extends Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker(getResources().getString(R.string.tracker_id));
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }

    public static Tracker getTracker() {
        return tracker;
    }

    public static GoogleAnalytics getAnalytics() {
        return analytics;
    }
}
