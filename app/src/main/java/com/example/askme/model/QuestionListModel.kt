package com.example.askme.model

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.android.parcel.Parcelize

@Parcelize
class QuestionListModel : Parcelable {
    var author: String = ""

    var author_id: String = ""

    var content: String = ""

    var title: String = ""

    var answers: MutableList<AnswerModel> = mutableListOf()

    var date_posted: Timestamp = Timestamp.now()

    var id: String = ""

    var tags: List<String> = emptyList()

}