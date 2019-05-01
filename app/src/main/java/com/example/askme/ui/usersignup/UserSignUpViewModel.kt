package com.example.askme.ui.usersignup

import android.arch.lifecycle.MutableLiveData
import com.example.askme.base.ParentViewModel
import com.example.askme.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class UserSignUpViewModel : ParentViewModel() {

    private val signUpObserver = MutableLiveData<Boolean>()
    private val progressObserver = MutableLiveData<Boolean>()

    fun checkValidation(name: String, email: String, password: String) {
        if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
            signUp(name, email, password)
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        progressObserver.value = true
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                user?.let {
                    updateUserName(it, name)
                }
            } else {
                progressObserver.value = false
                showToast(it.exception?.message.toString())
            }
        }
    }

    private fun updateUserName(firebaseUser: FirebaseUser, name: String) {
        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(name).build()

        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener {
            progressObserver.value = false
            if (it.isSuccessful) {
                saveUserToDb()
            } else {
                showToast(it.exception?.localizedMessage.toString())
            }
        }
    }

    private fun saveUserToDb() {
        val map = mutableMapOf<String, String>()
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            map["email"] = user.email!!
            map["id"] = user.uid
            map["name"] = user.displayName ?: ""
        }

        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.USER_DOC)
            .document().set(map)
            .addOnSuccessListener {
                signUpObserver.value = true
            }
            .addOnFailureListener {
                showToast(it.localizedMessage.toString())
            }
    }

    fun getSignUpObserver() = signUpObserver

    fun getProgressObserver() = progressObserver
}
