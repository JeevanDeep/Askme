package com.example.askme.ui.userlogin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.askme.R
import com.example.askme.ui.LoginOptionFragment
import com.example.askme.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginOptionFragment())
                .commitNow()
        else {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == UserLoginFragment.RC_SIGN_IN) {
            val fragment = supportFragmentManager.findFragmentById(R.id.container)
            if (fragment is UserLoginFragment) {
                fragment.onActivityResult(requestCode, resultCode, data)
            }

        }
    }
}
