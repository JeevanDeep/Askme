package com.example.askme.ui.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.askme.R
import com.example.askme.ui.home.question.QuestionListFragment
import com.example.askme.ui.userlogin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var questionListFragment: QuestionListFragment
    private val fm = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        setupToolbar()
        setupFragments()
    }

    private fun setupFragments() {
        questionListFragment = QuestionListFragment()
        fm.beginTransaction().add(R.id.container, questionListFragment, "1").commit()

        supportFragmentManager.addOnBackStackChangedListener {
            val backstack = supportFragmentManager.backStackEntryCount
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(backstack > 0)
                setDisplayShowHomeEnabled(backstack > 0)

            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        } else if (item?.itemId == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (FirebaseAuth.getInstance().currentUser != null)
            menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar.apply {
            title = "Home"
        }
    }

}
