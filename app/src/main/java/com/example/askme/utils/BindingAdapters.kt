package com.example.askme.utils

import android.databinding.BindingAdapter
import android.view.View
import android.widget.TextView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapters {
    @BindingAdapter("posted_date")
    @JvmStatic
    fun postedDate(textView: TextView, timestamp: Timestamp?) {

        if (timestamp == null) {
            return
        }
        val date = timestamp.toDate()

        val sdf = SimpleDateFormat("dd MMMM YY", Locale.getDefault())
        val dateString = sdf.format(date)

        textView.text = "Answered on: $dateString"
    }


    @BindingAdapter("set_visibility")
    @JvmStatic
    fun setVisibility(view: View, visibility: Boolean) {
        if (visibility)
            view.visible()
        else view.gone()
    }
}