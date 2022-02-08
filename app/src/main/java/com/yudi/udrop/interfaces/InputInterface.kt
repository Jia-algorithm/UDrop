package com.yudi.udrop.interfaces

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

interface InputInterface {

    fun hideKeyboard(activity: Activity, view: View) {
        (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).let {
            it.hideSoftInputFromWindow(view.windowToken, 0)
        }
        activity.window.decorView.rootView.clearFocus()
    }

    fun shouldHideInput(view: View, ev: MotionEvent): Boolean {
        var leftTop = intArrayOf(0, 0)
        view.getLocationInWindow(leftTop)
        return !(ev.x > leftTop[0] && ev.x < leftTop[0] + view.width && ev.y > leftTop[1] && ev.y < leftTop[1] + view.height)
    }
}