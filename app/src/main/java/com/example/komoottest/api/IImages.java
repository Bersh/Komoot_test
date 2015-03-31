package com.example.komoottest.api;

import com.example.komoottest.Constants;
import com.example.komoottest.model.PhotosDto;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author <a href="mailto:iBersh20@gmail.com">Iliya Bershadskiy</a>
 * @since 30.03.2015
 */
public interface IImages {
    @GET(Constants.Urls.GET_TOP_IMAGE_URL)
    void getPhotos(@Query("minx") double minX, @Query("miny") double minY, @Query("maxx") double maxX, @Query("maxy") double maxY, Callback<PhotosDto> callback);
}
