package com.merono.rlauncher

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import com.jakewharton.rxbinding.widget.textChanges
import rx.Observable

class SearchableLauncherView(context: Context) :
    LinearLayout(context), SearchableLauncher {

  private val launcher by lazy { findViewById(R.id.launcher) as Launcher }

  override var apps: List<App>
    get() = launcher.apps
    set(value) {
      launcher.apps = value
    }

  override val selects: Observable<App>
    get() = launcher.selects

  override val searches: Observable<CharSequence>
    get() = (findViewById(R.id.search_box) as EditText).textChanges()

  init {
    orientation = VERTICAL

    inflate(context, R.layout.view_searchable_launcher, this)
  }
}
