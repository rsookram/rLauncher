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
        .subscribe {
          router.start(it)
          launcher.clearQuery()
        }

    Observable.combineLatest(installedApps, launcher.searches, search)
        .takeUntil(destroys)
        .subscribe { launcher.apps = it }
  }
}
