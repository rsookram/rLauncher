package com.merono.rlauncher.presenter

import com.merono.rlauncher.entity.App
import com.merono.rlauncher.interactor.searchFilter
import com.merono.rlauncher.router.Router
import com.merono.rlauncher.view.SearchableLauncher
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/**
 * Presenter for the given [SearchableLauncher]
 */
class LauncherPresenter(
        launcher: SearchableLauncher,
        installedApps: Observable<List<App>>,
        destroys: Observable<Unit>,
        router: Router,
        search: (List<App>, CharSequence) -> List<App> = ::searchFilter
) {

    init {
        launcher.selects
                .subscribe { launchApp(it, launcher, router) }

        Observable
                .combineLatest(installedApps, launcher.searches, BiFunction(search))
                .takeUntil(destroys)
                .subscribe {
                    launcher.apps = it

                    if (it.size == 1) {
                        launchApp(it.first(), launcher, router)
                    }
                }
    }

    private fun launchApp(
            app: App,
            launcher: SearchableLauncher,
            router: Router
    ) {
        router.start(app)
        launcher.clearQuery()
    }
}
