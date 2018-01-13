package io.github.rsookram.rlauncher.view

import io.github.rsookram.rlauncher.entity.App
import io.reactivex.Observable

interface LauncherUi {

    var apps: List<App>

    val selects: Observable<App>
    val searches: Observable<CharSequence>

    fun clearQuery()
}
