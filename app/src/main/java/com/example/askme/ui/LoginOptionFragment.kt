package com.example.askme.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.askme.R
import com.example.askme.ui.home.HomeActivity
import com.example.askme.ui.userlogin.UserLoginFragment
import com.example.askme.ui.usersignup.UserSignUpFragment
import com.example.askme.utils.open
import com.example.askme.utils.replace
import kotlinx.android.synthetic.main.fragment_login_option.*
import kotlinx.android.synthetic.main.fragment_login_option.loginButton
import kotlinx.android.synthetic.main.user_login_fragment.*


class LoginOptionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_option, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addClickListeners()
    }

    private fun addClickListeners() {
        signUpButton.setOnClickListener { openSignUp() }
        loginButton.setOnClickListener { openLogin() }
        skip.setOnClickListener { openHome() }
    }

    private fun openHome() {
        activity?.open(HomeActivity::class.java)
        activity?.finish()
    }

    private fun openLogin() {
        fragmentManager?.let {
            it.replace(
                R.id.container,
                UserLoginFragment.newInstance(), addToBackStack = true
            )
        }
    }

    private fun openSignUp() {
        fragmentManager?.let {
            it.replace(R.id.container, UserSignUpFragment.newInstance(), addToBackStack = true)
        }
    }
}
