package com.kun.cityguide.about;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by dobrikostadinov on 6/18/15.
 */
@Root
public class About {

    @ElementList
    private List<Photo> photos;

    @Element
    private Description description;

    public List<Photo> getPhotos() {
        return photos;
    }

    public Description getDescription() {

        if(description == null){
            description = new Description();
        }

        return description;
    }
}