package com.example.komoottest.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author <a href="mailto:iBersh20@gmail.com">Iliya Bershadskiy</a>
 * @since 31.03.2015
 */
public class Photo {
    @SerializedName("photo_file_url")
    private String photoFileUrl;
    //other fields not needed

    public String getPhotoFileUrl() {
        return photoFileUrl;
    }

    public void setPhotoFileUrl(String photoFileUrl) {
        this.photoFileUrl = photoFileUrl;
    }
}
