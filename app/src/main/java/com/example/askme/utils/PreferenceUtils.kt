package com.example.askme.utils

import android.preference.PreferenceManager
import com.example.askme.AskMeApp

object PreferenceUtils {
    val prefs = PreferenceManager.getDefaultSharedPreferences(AskMeApp.instance)

    const val USER_LOGGED_IN = "user_logged_in"

    var userLoggedIn: Boolean
        get() = prefs.getBoolean(USER_LOGGED_IN, false)
        set(value) {
            prefs.edit().putBoolean(USER_LOGGED_IN, value).apply()
        }
}