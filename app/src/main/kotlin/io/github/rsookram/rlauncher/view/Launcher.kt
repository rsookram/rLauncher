package io.github.rsookram.rlauncher.view

import io.github.rsookram.rlauncher.entity.App
import io.reactivex.Observable

interface Launcher {

    var apps: List<App>

    val selects: Observable<App>
}
