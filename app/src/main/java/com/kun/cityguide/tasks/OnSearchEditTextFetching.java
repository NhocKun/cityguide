package com.kun.cityguide.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.kun.cityguide.callbacks.CallbackSearchDone;
import com.kun.cityguide.db.DatabaseManager;
import com.kun.cityguide.db.IPreferenceManager;
import com.kun.cityguide.db.PreferenceManager;
import com.kun.cityguide.googleplaces.Constants;
import com.kun.cityguide.googleplaces.GooglePlaces;
import com.kun.cityguide.googleplaces.models.Place;
import com.kun.cityguide.googleplaces.models.PlacesResult;
import com.kun.cityguide.util.Constant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dobrikostadinov on 6/15/15.
 */
public class OnSearchEditTextFetching extends AsyncTask<Void, Void, Void> {

    private CallbackSearchDone callbackSearchDone;
    List<String> types;
    String keyword;
    private Set<Place> filteredResultItems;
    private Context context;

    public OnSearchEditTextFetching(Context context, String keyword, List<String> types, CallbackSearchDone callbackSearchDone) {
        this.context = context;
        this.keyword = keyword;
        this.types = types;
        this.callbackSearchDone = callbackSearchDone;
    }

    @Override
    protected Void doInBackground(Void... params) {
        IPreferenceManager manager = new PreferenceManager(context);
        double l = Double.parseDouble(manager.getStringValue(Constant.LATITUDE));
        double llong = Double.parseDouble(manager.getStringValue(Constant.LONGITUDE));
        GooglePlaces googlePlaces = new GooglePlaces(new LatLng(l, llong));

        try {
            if (types.get(0).equalsIgnoreCase(Constants.PLACE_TYPES.GOOGLE_PLACES_FAV.getType())) {

                filteredResultItems = new HashSet<>();

                Set<Place> favItemsAsSet = DatabaseManager.getInstance().getRealFavPlace();

                List<Place> favItems = new ArrayList<>(favItemsAsSet);

                for (int i = 0; i < favItems.size(); i++) {
                    if (favItems.get(i).getName().toLowerCase().indexOf(keyword.toLowerCase()) > -1) {
                        filteredResultItems.add(favItems.get(i));
                    }
                }
            } else {
                PlacesResult placesResult = googlePlaces.getPlacesSearch(keyword, types);
                filteredResultItems = placesResult.getPlaces();
            }

        } catch (Exception e) {
            Log.i("Search_Query", "Exception - " + e.getMessage());
        }

        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        callbackSearchDone.showSearchLoading();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (filteredResultItems != null) {

            callbackSearchDone.getSearchResult(filteredResultItems);
        }
        callbackSearchDone.hideSearchLoading();
    }
}