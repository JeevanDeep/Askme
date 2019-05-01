package com.example.askme.ui.home.question

import android.arch.lifecycle.LiveData
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.text.Editable
import com.example.askme.base.ParentViewModel
import com.example.askme.model.QuestionListModel
import com.example.askme.network.ApiInterface
import com.example.askme.utils.Constants
import com.example.askme.utils.SingleLiveEvent
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AskQuestionViewModel : ParentViewModel() {

    val questionEditText = ObservableField<String>()
    val askEnabled = ObservableBoolean(false)
    private val refreshFeed = SingleLiveEvent<Boolean>()
    val showProgress = ObservableBoolean(false)
    val contentText = ObservableField<String>("")

    fun onAskClicked() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            // todo show user not logged in
            return
        }

        createPost(user)
    }

    fun onCloseClicked() {
        super.finishActivity()
    }


    private fun createPost(user: FirebaseUser) {
        showProgress.set(true)

        val re = Regex("[^A-Za-z0-9 ]")
        var query = questionEditText.get().toString() + " " + contentText.get().toString()
        query = re.replace(query, " ")

        val d = ApiInterface.getClient().getTags(query, query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val questionModel = QuestionListModel()
                questionModel.answers = mutableListOf()
                questionModel.author_id = user.uid
                questionModel.title = questionEditText.get() ?: ""
                questionModel.author = user.displayName ?: ""
                questionModel.content = contentText.get().toString()
                questionModel.date_posted = Timestamp.now()
                questionModel.tags = it.tags ?: emptyList()

                addQuestionToDb(questionModel)
            }, {
                it.printStackTrace()
            })

    }

    private fun addQuestionToDb(questionModel: QuestionListModel) {
        val db = FirebaseFirestore.getInstance()

        db.collection(Constants.QUESTIONS_DOC).add(questionModel).addOnCompleteListener {
            showProgress.set(false)
            if (it.isSuccessful) {
                refreshFeed.value = true
            } else {
                showToast(it.exception?.message ?: "")
            }
        }
    }

    fun refreshFeedObserver(): LiveData<Boolean> = refreshFeed
    fun afterTextChanged(e: Editable?) {
        e?.let {
            if (it.isNotBlank()) {
                if (!askEnabled.get())
                    askEnabled.set(true)
            } else {
                askEnabled.set(false)
            }
        }
    }
}