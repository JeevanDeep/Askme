package com.example.askme.ui.userlogin

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.askme.R
import com.example.askme.base.ParentFragment
import com.example.askme.ui.home.HomeActivity
import com.example.askme.ui.usersignup.UserSignUpFragment
import com.example.askme.utils.replace
import com.example.askme.utils.toText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.user_login_fragment.*

class UserLoginFragment : ParentFragment() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var viewModel: UserLoginViewModel
    private lateinit var gso: GoogleSignInOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.user_login_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGoogleSignIn()
        setupViewModel()
        addClickListeners()
        addObservers()

    }

    private fun setupGoogleSignIn() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(UserLoginViewModel::class.java)
        super.setupViewModel(viewModel)
    }

    private fun addObservers() {
        viewModel.getLoginObserver().observe(this, Observer {
            it?.let { login() }
        })
    }

    private fun login() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            startActivity(Intent(context, HomeActivity::class.java))
            activity?.finish()
        }
    }


    private fun addClickListeners() {
        loginButton.setOnClickListener { viewModel.checkValidation(emailEditText.toText(), passwordEditText.toText()) }
        signUp.setOnClickListener { fragmentManager?.replace(R.id.container, UserSignUpFragment.newInstance()) }
        googleSignIn.setOnClickListener {
            openGoogleSignIn()
        }
        googleSignUpText.setOnClickListener { openGoogleSignIn() }

    }

    private fun openGoogleSignIn() {
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val intent = mGoogleSignInClient.signInIntent
        activity?.startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account ?: return
                viewModel.firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login activity", "Google sign in failed", e)
            }
        }
    }


    companion object {
        fun newInstance() = UserLoginFragment()
        const val RC_SIGN_IN = 1
    }
}
