package com.example.askme.ui.userlogin

import android.arch.lifecycle.MutableLiveData
import com.example.askme.base.ParentViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class UserLoginViewModel : ParentViewModel() {

    private val loginObserver = MutableLiveData<Boolean>()

    fun checkValidation(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            login(email, password)
        } else {
            showToast("Email or password cannot be blank")
        }
    }

    private fun login(email: String, password: String) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    loginObserver.value = true
                } else {
                    showToast(it.exception?.message ?: "Something went wrong")
                }
            }
    }


    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        val auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    loginObserver.value = true
                } else {
                    // If sign in fails, display a message to the user.
                    showToast(task.exception?.message.toString())

                }
            }
    }

    fun getLoginObserver() = loginObserver

}
