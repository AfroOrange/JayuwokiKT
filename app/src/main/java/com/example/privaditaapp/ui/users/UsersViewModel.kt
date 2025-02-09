package com.example.privaditaapp.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UsersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "You haven't selected a server yet"
    }
    val text: LiveData<String> = _text
}