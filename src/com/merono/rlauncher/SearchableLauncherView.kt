package com.merono.rlauncher

import android.content.Context
import android.widget.TextView
import com.jakewharton.rxbinding.widget.textChanges
import rx.Observable

class SearchableLauncherView(context: Context) :
    InsetLinearLayout(context), SearchableLauncher {

  private val launcher by lazy { findViewById(R.id.launcher) as Launcher }
  private val searchBox by lazy { findViewById(R.id.search_box) as TextView }

  override var apps: List<App>
    get() = launcher.apps
    set(value) {
      launcher.apps = value
    }

  override val selects: Observable<App>
    get() = launcher.selects

  override val searches: Observable<CharSequence>
    get() = searchBox.textChanges()

  init {
    orientation = VERTICAL
    clipToPadding = false
    clipChildren = false

    inflate(context, R.layout.view_searchable_launcher, this)

    searchBox.requestFocus()
  }
}
