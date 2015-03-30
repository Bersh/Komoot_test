package com.example.komoottest;

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
    public static final float LOCATION_MIN_DISTANCE = 100;//meters


    public static class Urls {
        public static final String IMAGES_SERVER_URL = "http://www.panoramio.com/map";
        public static final String GET_IMAGES_URL = "/get_panoramas.php";
    }

    public abstract static class SharedPreferences {
        public static final String SHARED_PREFERENCES_NAME = "KomootTest";
        public static final String SHARED_PREFERENCES_KEY_LOCATION_TRACING_STARTED = "location_tracking_started";
    }

}
