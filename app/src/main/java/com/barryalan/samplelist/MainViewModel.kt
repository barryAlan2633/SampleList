package com.barryalan.samplelist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.barryalan.samplelist.model.ListItem
import com.barryalan.samplelist.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val myResponse: MutableLiveData<Response<List<ListItem>>> = MutableLiveData()

    fun getListItem(){
        viewModelScope.launch{
            val response = repository.getListItem()
            myResponse.value = response
        }
    }
}