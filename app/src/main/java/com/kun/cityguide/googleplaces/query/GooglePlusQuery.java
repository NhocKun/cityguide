package com.kun.cityguide.googleplaces.query;

import com.kun.cityguide.MainActivity;
import com.kun.cityguide.googleplaces.Constants;

/**
 * Created by dobrikostadinov on 6/11/15.
 */
public class GooglePlusQuery extends Query {

    private String googlePlacePersonId;

    public void setField() {

        mQueryBuilder.addParameter("fields", "image");
    }

    public void setGooglePlacePersonId(String googlePlacePersonId) {
        this.googlePlacePersonId = googlePlacePersonId;
    }

    @Override
    public String toString() {
        return getUrl();
    }

    @Override
    public String getUrl() {
        setField();
        setKey(MainActivity.getApiKey());

        return Constants.GOOGLE_PLUS_URL + googlePlacePersonId + mQueryBuilder.toString();
    }
}
