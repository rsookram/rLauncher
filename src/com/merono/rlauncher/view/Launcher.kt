package com.merono.rlauncher.view

import com.merono.rlauncher.entity.App
import rx.Observable

interface Launcher {

  var apps: List<App>

  val selects: Observable<App>
}
