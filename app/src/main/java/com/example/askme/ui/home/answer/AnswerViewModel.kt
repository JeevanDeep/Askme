package com.example.askme.ui.home.answer

import android.arch.lifecycle.LiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.Editable
import com.example.askme.base.ParentViewModel
import com.example.askme.model.AnswerModel
import com.example.askme.utils.Constants
import com.example.askme.utils.SingleLiveEvent
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class AnswerViewModel : ParentViewModel() {
    val answerEnabled = ObservableBoolean(false)
    val answerEditText = ObservableField<String>()
    val showProgress = ObservableBoolean(false)
    private val answerSuccessObersver = SingleLiveEvent<AnswerModel>()
    private var id: String = ""

    fun onCloseClicked() {
        super.finishActivity()
    }

    fun setQuestionId(id: String) {
        this.id = id

    }

    fun onAnswerClicked() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val answer = answerEditText.get() ?: ""
        val date = Timestamp.now()
        val author = user.displayName ?: "Anonymous"
        val author_id = user.uid

        if (id.isNotBlank()) {
            val answerModel = AnswerModel(author, author_id, answer, date)
            addAnswer(answerModel)
        }
    }

    private fun addAnswer(answerModel: AnswerModel) {

        showProgress.set(true)
        val questionRef =
            FirebaseFirestore.getInstance().collection(Constants.QUESTIONS_DOC).document(id)

        questionRef.update("answers", FieldValue.arrayUnion(answerModel)).addOnCompleteListener {
            showProgress.set(false)
            if (it.isSuccessful) {
                answerSuccessObersver.value = answerModel
            } else {
                showToast(it.exception?.message ?: "")
            }
        }

    }

    fun afterTextChanged(e: Editable?) {
        e?.let {
            if (it.isNotBlank()) {
                if (!answerEnabled.get())
                    answerEnabled.set(true)
            } else {
                answerEnabled.set(false)
            }
        }
    }

    fun getAnswerSuccessObserver(): LiveData<AnswerModel> = answerSuccessObersver
}