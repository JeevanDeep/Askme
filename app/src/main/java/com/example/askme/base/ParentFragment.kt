package com.example.askme.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.example.askme.utils.toast

abstract class ParentFragment : Fragment() {
    private lateinit var parentViewModel: ParentViewModel

    open fun setupViewModel(viewModel: ParentViewModel) {
        parentViewModel = viewModel
        addObservers()

    }

    private fun addObservers() {
        parentViewModel.getToastObserver().observe(this, Observer { it?.let { toast(it) } })
    }
}