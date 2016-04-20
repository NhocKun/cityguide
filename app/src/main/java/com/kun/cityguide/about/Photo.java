package com.kun.cityguide.about;

import android.content.Context;

import org.simpleframework.xml.Element;

/**
 * Created by dobrikostadinov on 6/18/15.
 */
public class Photo {

    @Element
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public int getDrawableId(Context context) {
        return context.getResources().getIdentifier(path, "drawable",
                context.getPackageName());
    }
}
