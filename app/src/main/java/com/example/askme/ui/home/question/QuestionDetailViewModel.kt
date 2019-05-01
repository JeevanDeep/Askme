package com.example.askme.ui.home.question

import android.arch.lifecycle.LiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.example.askme.adapter.AnswerListAdapter
import com.example.askme.base.ParentViewModel
import com.example.askme.model.AnswerModel
import com.example.askme.model.QuestionListModel
import com.example.askme.utils.Constants
import com.example.askme.utils.SingleLiveEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class QuestionDetailViewModel : ParentViewModel(), AnswerListAdapter.AnswerListener {

    val questionField = ObservableField<QuestionListModel>()
    val adapterField = ObservableField<AnswerListAdapter>()
    private val openAnswerActivity = SingleLiveEvent<Boolean>()
    private val refreshScreen = SingleLiveEvent<Boolean>()
    val isAuthor = ObservableBoolean()
    val isUserLoggedIn = ObservableBoolean()
    val refreshScreenAndFinish = SingleLiveEvent<Boolean>()
    val showProgress = ObservableBoolean(false)

    fun setDetails(questionListModel: QuestionListModel) {
        questionField.set(questionListModel)
        adapterField.set(AnswerListAdapter(questionListModel.answers, this))
        adapterField.get()?.notifyDataSetChanged()

        val user = FirebaseAuth.getInstance().currentUser

        isUserLoggedIn.set(user != null)
        isAuthor.set(user != null && user.uid == questionListModel.author_id)
    }

    fun onAnswerClick() {
        openAnswerActivity.value = true
    }

    fun onDeleteClick() {
        deleteQuestion(questionField.get()!!)
    }

    private fun deleteQuestion(questionModel: QuestionListModel) {
        showProgress.set(true)
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.QUESTIONS_DOC).document(questionModel.id)
            .delete()
            .addOnSuccessListener {
                showProgress.set(false)
                refreshScreenAndFinish.value = true
            }
            .addOnFailureListener {
                showProgress.set(false)
                showToast(it.message ?: "")
            }
    }

    fun openAnswerActivity(): LiveData<Boolean> = openAnswerActivity
    fun refreshScreen(): LiveData<Boolean> = refreshScreen
    fun refreshScreenAndFinish(): LiveData<Boolean> = refreshScreenAndFinish

    override fun onDeleteClicked(answerList: MutableList<AnswerModel>, answerModel: AnswerModel, position: Int) {
        val id = questionField.get()?.id
        id?.let {
            showProgress.set(true)
            val questionRef =
                FirebaseFirestore.getInstance().collection(Constants.QUESTIONS_DOC).document(id)

            questionRef.update("answers", FieldValue.arrayRemove(answerModel)).addOnCompleteListener {
                showProgress.set(false)
                if (it.isSuccessful) {
                    refreshScreen.value = true
                    adapterField.get()!!.removeAnswer(position)
                } else {
                    showToast(it.exception?.message ?: "")
                }
            }

        }
    }

}