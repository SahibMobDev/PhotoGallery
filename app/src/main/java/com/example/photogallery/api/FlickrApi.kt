package com.example.photogallery.api

import com.example.photogallery.FlickrResponse
import retrofit2.http.GET

interface FlickrApi {
    @GET("https://api.flickr.com/services/rest/?method=flickr.interestingness.getList" +
            "&api_key=9e60eace59806f3550c0d7335311612b" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    suspend fun fetchPhotos(): FlickrResponse
}