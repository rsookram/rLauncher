package com.merono.rlauncher

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class LauncherView(context: Context, attrs: AttributeSet) :
    RecyclerView(context, attrs), Launcher {

  private val appAdapter = AppAdapter()

  override var apps: List<App>
    get() = appAdapter.apps
    set(value) {
      appAdapter.apps = value
    }

  override val selects = appAdapter.selects

  init {
    layoutManager = LinearLayoutManager(context, VERTICAL, true)
    adapter = appAdapter
  }
}
