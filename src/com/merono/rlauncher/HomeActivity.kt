package com.merono.rlauncher

import android.app.Activity
import android.os.Bundle
import rx.subjects.PublishSubject

class HomeActivity : Activity() {

  private val destroys = PublishSubject.create<Unit>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val view = LauncherView(this)
    setContentView(view)

    LauncherPresenter(
        view,
        installedApps(this),
        destroys.asObservable(),
        AppRouter(this)
    )
  }

  override fun onBackPressed() {
    // Intentionally don't call super so that the launcher isn't closed
    // when back is pressed
  }

  override fun onDestroy() {
    super.onDestroy()
    destroys.onNext(null)
  }
}
