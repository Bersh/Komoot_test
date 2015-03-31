package com.example.komoottest.api;

import com.example.komoottest.Constants;
import com.example.komoottest.GeoLocation;
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
     * Get url of first image for given location
     *
     * @param lat      latitude
     * @param lon      longitude
     * @param callback callback
     */
    public void getImagesForLocation(double lat, double lon, Callback<PhotosDto> callback) {
        GeoLocation geoLocation = GeoLocation.fromDegrees(lat, lon);
        GeoLocation[] geoLocations = geoLocation.boundingCoordinates(1, 6371); //target bound radius 1 km. Earth radius 6371 km
        imagesAdapter.getPhotos(geoLocations[0].getLongitudeInDegrees(), geoLocations[0].getLatitudeInDegrees(),
                geoLocations[1].getLongitudeInDegrees(), geoLocations[1].getLatitudeInDegrees(), callback);
    }
}
