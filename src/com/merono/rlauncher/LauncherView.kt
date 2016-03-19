package com.merono.rlauncher

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

// TODO: Scrollbar
class LauncherView(context: Context) : RecyclerView(context), Launcher {

  private val appAdapter = AppAdapter()

  override var apps: List<App>
    get() = appAdapter.apps
    set(value) {
      appAdapter.apps = value
    }

  override val selects = appAdapter.selects

  init {
    // TODO: stack from end? or reverse layout?
    layoutManager = LinearLayoutManager(context)
    adapter = appAdapter

    val padding = resources.getDimensionPixelSize(R.dimen.list_top_bottom_padding)
    setPadding(0, padding, 0, padding)
    clipToPadding = false
  }
}
