package com.example.askme.ui.home.question

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.askme.R
import com.example.askme.base.ParentActivity
import com.example.askme.databinding.ActivityAskQuestionBinding

class AskQuestionActivity : ParentActivity() {

    private val askQuestionViewModel: AskQuestionViewModel by lazy {
        ViewModelProviders.of(this)[AskQuestionViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAskQuestionBinding>(
            this,
            R.layout.activity_ask_question
        )
        binding.viewmodel = askQuestionViewModel
        super.setupViewModel(askQuestionViewModel)

        addObserver()
    }

    private fun addObserver() {
        askQuestionViewModel.refreshFeedObserver().observe(this, Observer {
            setResult(Activity.RESULT_OK)
            finish()
        })
    }
}
