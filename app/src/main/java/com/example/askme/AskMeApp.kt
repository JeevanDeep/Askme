package com.example.askme

import android.app.Application

class AskMeApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: AskMeApp
            private set
    }
}