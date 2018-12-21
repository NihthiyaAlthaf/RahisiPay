package com.goandroytech.www.rahisipay.fonts;

import android.app.Application;


/**
 * Created by vamsi on 06-05-2017 for android custom font article
 */

public class CustomFontApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "arial.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "arial.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "arial.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "arial.ttf");
    }
}