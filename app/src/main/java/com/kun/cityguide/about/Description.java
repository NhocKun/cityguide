package com.kun.cityguide.about;

import org.simpleframework.xml.Element;

/**
 * Created by dobrikostadinov on 6/18/15.
 */
public class Description {

    @Element
    private String content;

    public String getContent() {

        if (content == null) {
            content = "";
        }

        return content;
    }
}
