package com.merono.rlauncher

import rx.Observable

interface Launcher {

  var apps: List<App>

  val selects: Observable<App>
}
