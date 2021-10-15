package com.example.gconnect.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserPostViewModel :ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is userpostprofile Fragment"
    }
    val text: LiveData<String> = _text

}