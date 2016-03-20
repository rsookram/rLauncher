package com.merono.rlauncher.view

import android.content.Context
import android.view.WindowInsets
import android.widget.LinearLayout

open class InsetLinearLayout(context: Context) : LinearLayout(context) {

  private var topInset = 0

  init {
    fitsSystemWindows = true
  }

  override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets? {
    topInset = insets?.systemWindowInsetTop ?: 0
    setPadding(paddingLeft, topInset, paddingRight, paddingBottom)
    insets?.consumeSystemWindowInsets()
    return super.onApplyWindowInsets(insets)
  }
}
