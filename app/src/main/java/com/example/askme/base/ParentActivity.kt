package com.example.askme.base

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import com.example.askme.utils.toast

abstract class ParentActivity : AppCompatActivity() {
    private lateinit var parentViewModel: ParentViewModel


    open fun setupViewModel(viewModel: ParentViewModel) {
        parentViewModel = viewModel
        addObservers()

    }

    private fun addObservers() {
        parentViewModel.getToastObserver().observe(this, Observer { it?.let { toast(it) } })
        parentViewModel.getFinishActivityObserver().observe(this, Observer { it?.let { if (it) finish() } })
    }
}