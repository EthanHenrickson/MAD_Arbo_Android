package com.mad.myapplication.ui.dashboard

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    fun onFilterClicked() {
        _text.value = "Hi"
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    var text: LiveData<String> = _text
}