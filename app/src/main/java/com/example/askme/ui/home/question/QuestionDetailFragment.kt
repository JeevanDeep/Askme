package com.example.askme.ui.home.question

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.askme.R
import com.example.askme.adapter.AnswerListAdapter
import com.example.askme.adapter.QuestionTagsAdapter
import com.example.askme.base.ParentFragment
import com.example.askme.databinding.FragmentQuestionDetailBinding
import com.example.askme.model.AnswerModel
import com.example.askme.model.QuestionListModel
import com.example.askme.ui.home.HomeActivity
import com.example.askme.ui.home.answer.AnswerActivity
import kotlinx.android.synthetic.main.fragment_question_detail.*

class QuestionDetailFragment : ParentFragment() {

    private val viewModel: QuestionDetailViewModel by lazy {
        ViewModelProviders.of(this@QuestionDetailFragment)[QuestionDetailViewModel::class.java]
    }

    private lateinit var questionListModel: QuestionListModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentQuestionDetailBinding>(
            inflater,
            R.layout.fragment_question_detail,
            container,
            false
        )
        binding.viewmodel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionListModel = arguments?.getParcelable("question") as QuestionListModel
        setupViewModel(viewModel)
        viewModel.setDetails(questionListModel)

        (activity as HomeActivity).supportActionBar?.title = questionListModel.title

        addObserver()

        setupRecyclerView(questionListModel.tags)

        questionContent.setOnClickListener {
            if (questionContent.maxLines == 4)
                questionContent.maxLines = Int.MAX_VALUE
            else
                questionContent.maxLines = 4
        }
    }

    private fun setupRecyclerView(tags: List<String>) {
        tagsRecyclerView.apply {
            adapter = QuestionTagsAdapter(tags)
            layoutManager = GridLayoutManager(
                this@QuestionDetailFragment.context,
                3
            )
        }
    }

    private fun addObserver() {
        viewModel.openAnswerActivity().observe(requireActivity(), Observer {
            it?.let { if (it) openAnswerActivity() }
        })

        // delete answer
        viewModel.refreshScreen().observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    refreshQuestionScreen()
                }
            }
        })


        // delete question
        viewModel.refreshScreenAndFinish().observe(requireActivity(), Observer {
            it?.let {
                if (it) {
                    refreshQuestionScreen()
                    fragmentManager?.popBackStackImmediate()
                }
            }
        })
    }

    private fun refreshQuestionScreen() {
        targetFragment?.onActivityResult(
            QuestionListFragment.REFRESH_SCREEN_REQUEST_CODE,
            Activity.RESULT_OK,
            null
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == OPEN_ANSWER_ACTIVITY && resultCode == Activity.RESULT_OK) {
            refreshQuestionScreen()

            data?.let {
                val answerModel = it.getParcelableExtra<AnswerModel>("model")
                val adapter = answersRecyclerView.adapter as AnswerListAdapter
                adapter.addAnswer(answerModel)
            }
        }
    }

    private fun openAnswerActivity() {
        val bundle = Bundle()
        bundle.putString("id", questionListModel.id)
        val intent = Intent(context, AnswerActivity::class.java)
        intent.putExtras(bundle)
        startActivityForResult(intent, OPEN_ANSWER_ACTIVITY)
    }

    override fun onDestroy() {
        super.onDestroy()

        (activity as HomeActivity).supportActionBar?.title = "Home"
    }

    companion object {
        const val OPEN_ANSWER_ACTIVITY = 100
    }
}
