package com.example.askme.ui.home.question


import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.askme.R
import com.example.askme.adapter.QuestionListAdapter
import com.example.askme.base.ParentFragment
import com.example.askme.model.QuestionListModel
import com.example.askme.utils.DetailsTransition
import com.example.askme.utils.gone
import com.example.askme.utils.visible
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_question_list.*


class QuestionListFragment : ParentFragment(), QuestionListAdapter.QuestionClickListener {

    private lateinit var questionListAdapter: QuestionListAdapter
    private val questionList = mutableListOf<QuestionListModel>()
    private lateinit var viewModel: QuestionListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        addObservers()

        questionFab.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser != null)
                openAskQuestion()
            else Toast.makeText(context, "You must log in first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openAskQuestion() {
        startActivityForResult(Intent(context, AskQuestionActivity::class.java), REFRESH_SCREEN_REQUEST_CODE)
    }

    private fun addObservers() {
        viewModel.getQuestionListObserver().observe(this, Observer {
            it?.let {
                questionList.clear()
                questionList.addAll(it)
                questionListAdapter.notifyDataSetChanged()
            }
        })

        viewModel.getShimmerObersver().observe(this, Observer {
            it?.let {
                if (it) {
                    questionsRecyclerView.gone()
                    shimmerContainer.visible()
                    shimmerContainer.startShimmer()
                } else {
                    questionsRecyclerView.visible()
                    shimmerContainer.gone()
                    shimmerContainer.stopShimmer()
                }
            }
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(requireActivity())[QuestionListViewModel::class.java]
        super.setupViewModel(viewModel)
    }

    private fun setupRecyclerView() {
        questionListAdapter = QuestionListAdapter(questionList, this)
        questionsRecyclerView.apply {
            adapter = questionListAdapter
            layoutManager = LinearLayoutManager(this@QuestionListFragment.context)
        }
    }

    override fun onQuestionClicked(questionTextView: TextView, questionListModel: QuestionListModel) {
        val bundle = Bundle()
        val fragment = QuestionDetailFragment()
        bundle.putParcelable("question", questionListModel)
        fragment.arguments = bundle
        fragment.sharedElementEnterTransition = DetailsTransition()
        fragment.setTargetFragment(this, REFRESH_SCREEN_REQUEST_CODE)

        fragmentManager?.let {
            it.beginTransaction()
                .addSharedElement(questionTextView, ViewCompat.getTransitionName(questionTextView)!!)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
//                .setTransition(android.R.transition.explode)
                .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REFRESH_SCREEN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.getQuestionsList()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val REFRESH_SCREEN_REQUEST_CODE = 1997
    }

}
