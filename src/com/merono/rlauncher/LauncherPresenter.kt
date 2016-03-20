package com.merono.rlauncher

import rx.Observable

class LauncherPresenter(launcher: SearchableLauncher,
                        installedApps: Observable<List<App>>,
                        destroys: Observable<Unit>,
                        router: Router,
                        search: (List<App>, CharSequence) -> List<App> = ::searchFilter) {

  init {
    launcher.selects
        .takeUntil(destroys)
        .subscribe { launchApp(it, launcher, router) }

    Observable.combineLatest(installedApps, launcher.searches, search)
        .takeUntil(destroys)
        .subscribe {
          launcher.apps = it

          if (it.size == 1) {
            launchApp(it[0], launcher, router)
          }
        }
  }

  private fun launchApp(app: App, launcher: SearchableLauncher, router: Router) {
    router.start(app)
    launcher.clearQuery()
  }
}
