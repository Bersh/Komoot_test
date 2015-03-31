package com.example.komoottest;

import android.os.Environment;

import java.io.File;

/**
 * @author <a href="mailto:iBersh20@gmail.com">Iliya Bershadskiy</a>
 * @since 30.03.2015
 */
public class Constants {
    private Constants() {
    }

    public static final String LOG_TAG = "KomootTest";

    //location tracking
    public static final long LOCATION_MIN_TIME = 1000; //msec
    public static final float LOCATION_MIN_DISTANCE = 1;//meters

    public static final String IMAGES_FOLDER_PATH = Environment.getExternalStorageDirectory() + File.separator + "KomootTest";
    public static final File IMAGES_FOLDER = new File(Constants.IMAGES_FOLDER_PATH);

    public static class Urls {
        public static final String IMAGES_SERVER_URL = "http://www.panoramio.com/map";
        public static final String GET_TOP_IMAGE_URL = "/get_panoramas.php?set=public&from=0&to=1&size=medium&mapfilter=true";
    }

    public abstract static class SharedPreferences {
        public static final String SHARED_PREFERENCES_NAME = "KomootTest";
        public static final String SHARED_PREFERENCES_KEY_LOCATION_TRACING_STARTED = "location_tracking_started";
    }

}
