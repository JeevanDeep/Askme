package com.example.askme.ui.home.question

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.example.askme.base.ParentViewModel
import com.example.askme.model.QuestionListModel
import com.example.askme.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class QuestionListViewModel : ParentViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val questionObserver = MutableLiveData<List<QuestionListModel>>()
    private val showShimmer = MutableLiveData<Boolean>()

    init {
        getQuestionsList()
    }

    fun getQuestionsList() {
        showShimmer.value = true
        db.collection(Constants.QUESTIONS_DOC)
            .orderBy("date_posted", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                showShimmer.value = false
                it?.let { questionSnapshot ->

                    val document = questionSnapshot.documents
                    val questionsList = mutableListOf<QuestionListModel>()

                    document.forEach { documentSnapshot ->
                        val question =
                            documentSnapshot.toObject(QuestionListModel::class.java) ?: return@addOnSuccessListener
                        question.id = documentSnapshot.id
                        question.answers.sortByDescending {
                            it.date_posted?.seconds
                        }
                        questionsList.add(question)
                    }

                    questionObserver.value = questionsList
                }
            }
            .addOnFailureListener {
                showToast(it.message ?: "Something went wrong")
                showShimmer.value = false
            }
    }

    fun getShimmerObersver(): LiveData<Boolean> = showShimmer
    fun getQuestionListObserver(): LiveData<List<QuestionListModel>> = questionObserver
}