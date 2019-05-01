package com.example.askme.ui.home.answer


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.example.askme.R
import com.example.askme.base.ParentActivity
import com.example.askme.databinding.ActivityAnswerBinding

class AnswerActivity : ParentActivity() {

    private val viewModel: AnswerViewModel by lazy {
        ViewModelProviders.of(this)[AnswerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityAnswerBinding>(
            this,
            R.layout.activity_answer
        )
        binding.viewmodel = viewModel
        setupViewModel(viewModel)
        val id = intent?.extras?.getString("id") ?: ""
        viewModel.setQuestionId(id)

        viewModel.getAnswerSuccessObserver().observe(this, Observer {
            it?.let {
                val intent = Intent()
                intent.putExtra("model", it)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })
    }
}
