package com.barryalan.samplelist.api

import com.barryalan.samplelist.model.ListItem
import retrofit2.Response
import retrofit2.http.GET

interface ListApi {
    @GET("hiring.json")
    suspend fun getListItem(): Response<List<ListItem>>
}

