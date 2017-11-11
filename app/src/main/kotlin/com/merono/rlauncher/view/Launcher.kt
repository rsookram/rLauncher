package com.merono.rlauncher.view

import com.merono.rlauncher.entity.App
import io.reactivex.Observable

interface Launcher {

    var apps: List<App>

    val selects: Observable<App>
}
