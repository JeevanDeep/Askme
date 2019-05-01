package com.example.askme.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AnswerModel(
    var author: String = "",
    var author_id: String = "",
    var content: String = "",
    var date_posted: Timestamp?
) : Parcelable {
    constructor() : this("", "", "", null)
}