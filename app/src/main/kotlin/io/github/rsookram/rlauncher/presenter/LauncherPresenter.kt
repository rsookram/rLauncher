package io.github.rsookram.rlauncher.presenter

import io.github.rsookram.rlauncher.entity.App
import io.github.rsookram.rlauncher.interactor.searchFilter
import io.github.rsookram.rlauncher.router.Router
import io.github.rsookram.rlauncher.view.SearchableLauncher
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
