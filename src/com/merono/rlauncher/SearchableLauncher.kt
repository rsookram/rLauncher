package com.merono.rlauncher

import rx.Observable

interface SearchableLauncher : Launcher {

  val searches: Observable<CharSequence>
}
