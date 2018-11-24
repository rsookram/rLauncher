package io.github.rsookram.rlauncher.view

import io.github.rsookram.rlauncher.entity.App
import io.reactivex.Observable

interface LauncherUi {

    val selects: Observable<App>
    val searches: Observable<CharSequence>

    fun setApps(apps: List<App>)
    fun clearQuery()
}
