package io.github.rsookram.rlauncher.presenter

import io.github.rsookram.rlauncher.entity.App
import io.github.rsookram.rlauncher.interactor.searchFilter
import io.github.rsookram.rlauncher.router.Router
import io.github.rsookram.rlauncher.view.LauncherUi
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/** Presenter for the given [LauncherUi] */
class LauncherPresenter(
    ui: LauncherUi,
    installedApps: Observable<List<App>>,
    destroys: Observable<Unit>,
    router: Router,
    search: (List<App>, CharSequence) -> List<App> = ::searchFilter
) {

    init {
        ui.selects
            .subscribe { launchApp(it, ui, router) }

        Observable
            .combineLatest(installedApps, ui.searches, BiFunction(search))
            .takeUntil(destroys)
            .subscribe {
                ui.apps = it

                if (it.size == 1) {
                    launchApp(it.first(), ui, router)
                }
            }
    }

    private fun launchApp(app: App, ui: LauncherUi, router: Router) {
        router.start(app)
        ui.clearQuery()
    }
}
