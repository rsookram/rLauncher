package io.github.rsookram.rlauncher.view

import io.reactivex.Observable

interface SearchableLauncher : Launcher {

    val searches: Observable<CharSequence>

    fun clearQuery()
}
