package com.example.askme.ui.usersignup

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.askme.R
import com.example.askme.base.ParentFragment
import com.example.askme.ui.home.HomeActivity
import com.example.askme.utils.gone
import com.example.askme.utils.toText
import com.example.askme.utils.visible
import kotlinx.android.synthetic.main.user_signup_fragment.*

class UserSignUpFragment : ParentFragment() {

    private lateinit var viewModel: UserSignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_signup_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()

        addClickListeners()

        addObservers()
    }

    private fun addObservers() {
        viewModel.getSignUpObserver().observe(this, Observer {
            it?.let {
                if (it) {
                    startActivity(Intent(context, HomeActivity::class.java))
                    activity?.finish()
                }
            }
        })

        viewModel.getProgressObserver().observe(this, Observer {
            it?.let {
                if (it) {
                    progress.visible()
                } else {
                    progress.gone()
                }
            }
        })
    }

    private fun addClickListeners() {
        signUpButton.setOnClickListener {
            viewModel.checkValidation(
                nameEditText.toText(),
                emailEditText.toText(),
                passwordEditText.toText()
            )
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(UserSignUpViewModel::class.java)
        super.setupViewModel(viewModel)
    }

    companion object {
        fun newInstance() = UserSignUpFragment()
    }

}
