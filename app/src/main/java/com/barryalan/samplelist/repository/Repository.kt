package com.barryalan.samplelist.repository

import com.barryalan.samplelist.api.RetrofitInstance
import com.barryalan.samplelist.model.ListItem
import retrofit2.Response

class Repository {

    suspend fun getListItem(): Response<List<ListItem>> {
        return RetrofitInstance.api.getListItem()
    }
}