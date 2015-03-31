package com.example.komoottest.api;

import android.content.Context;

import com.example.komoottest.Constants;
import com.example.komoottest.model.PhotosDto;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * @author <a href="mailto:iBersh20@gmail.com">Iliya Bershadskiy</a>
 * @since 30.03.2015
 */
public class ApiManager {

    private IImages imagesAdapter;

    public ApiManager() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setLongSerializationPolicy(LongSerializationPolicy.STRING);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.Urls.IMAGES_SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new GsonConverter(gsonBuilder.create()))
                .build();

        imagesAdapter = restAdapter.create(IImages.class);
    }

    /**
     * Get url of first image foe given location
     *
     * @param lat      latitude
     * @param lon      longitude
     * @param callback callback
     */
    public void getImagesForLocation(double lat, double lon, Callback<PhotosDto> callback) {
        imagesAdapter.getPhotos(lat, lon, callback);
    }
}
