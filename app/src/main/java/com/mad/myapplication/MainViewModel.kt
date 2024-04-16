package com.mad.myapplication
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class MainViewModel {



    class SharedViewModel : ViewModel() {

        val data = MutableLiveData<FormData>()
    }


    data class FormData(
        var field1: Boolean = true,
        var field2: Boolean = true,
        var field3: Boolean = true,
        var field4: Boolean = true,
        var field5: Boolean = true,
        var field6: Boolean = true,
        var field7: Boolean = true,
        var field8: Boolean = true,
        var field9: Boolean = true,
        var field10: Boolean = true,
    )

}