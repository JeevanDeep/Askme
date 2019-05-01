package com.example.askme.utils

import android.support.transition.ChangeBounds
import android.support.transition.ChangeTransform
import android.support.transition.TransitionSet

class DetailsTransition : TransitionSet() {
    init {
        ordering = ORDERING_TOGETHER
        addTransition(ChangeBounds()).addTransition(ChangeTransform())
    }

}