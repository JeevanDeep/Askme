package com.example.askme.base

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.askme.utils.SingleLiveEvent

open class ParentViewModel : ViewModel() {
    private var toastEvent = SingleLiveEvent<String>()
    private var finishActivityEvent = SingleLiveEvent<Boolean>()

    fun showToast(message: String) {
        toastEvent.value = message
    }

    fun finishActivity() {
        finishActivityEvent.value = true
    }

    fun getFinishActivityObserver() = finishActivityEvent

    fun getToastObserver() = toastEvent
}