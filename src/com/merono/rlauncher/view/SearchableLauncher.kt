package com.merono.rlauncher.view

import rx.Observable

interface SearchableLauncher : Launcher {

  val searches: Observable<CharSequence>

  fun clearQuery(): Unit
}
