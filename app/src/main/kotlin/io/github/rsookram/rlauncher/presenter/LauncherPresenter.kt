package io.github.rsookram.rlauncher.presenter

import io.github.rsookram.rlauncher.entity.App
import io.github.rsookram.rlauncher.interactor.searchFilter
import io.github.rsookram.rlauncher.router.RouteTo
import io.github.rsookram.rlauncher.view.LauncherUi
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

/** Presenter for the given [LauncherUi] */
class LauncherPresenter(
    ui: LauncherUi,
    installedApps: Observable<List<App>>,
    destroys: Observable<Unit>,
    routeTo: RouteTo,
    search: (List<App>, CharSequence) -> List<App> = ::searchFilter
) {

    init {
        ui.selects
            .subscribe { launchApp(it, ui, routeTo) }

        Observable
            .combineLatest(installedApps, ui.searches, BiFunction(search))
            .takeUntil(destroys)
            .subscribe {
                ui.apps = it

                if (it.size == 1) {
                    launchApp(it.first(), ui, routeTo)
                }
            }
    }

    private fun launchApp(app: App, ui: LauncherUi, routeTo: RouteTo) {
        routeTo(app)
        ui.clearQuery()
    }
}
