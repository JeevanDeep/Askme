package com.example.askme.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.EditText
import android.widget.Toast

fun Context.toast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: CharSequence) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}


fun Context.open(activity: Class<out AppCompatActivity>, bundle: Bundle = Bundle()) {
    val intent = Intent(this, activity)
    intent.putExtras(bundle)
    startActivity(intent)
}

fun EditText.toText(): String {
    return this.text.toString().trim()
}

fun FragmentManager.replace(res: Int, fragment: Fragment, addToBackStack: Boolean = true) {
    if (addToBackStack)
        beginTransaction().replace(res, fragment).addToBackStack(null).commit()
    else
        beginTransaction().replace(res, fragment).commit()
}

fun FragmentManager.add(res: Int, fragment: Fragment, addToBackStack: Boolean = true) {
    if (addToBackStack)
        beginTransaction().add(res, fragment).addToBackStack(null).commit()
    else
        beginTransaction().add(res, fragment).commit()
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisble() {
    visibility = View.INVISIBLE

}