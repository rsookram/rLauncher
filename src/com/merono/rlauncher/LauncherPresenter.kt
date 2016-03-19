package com.merono.rlauncher

import rx.Observable

class LauncherPresenter(launcher: Launcher,
                        installedApps: Observable<List<App>>,
                        destroys: Observable<Unit>,
                        router: Router) {

  init {
    installedApps
        .takeUntil(destroys)
        .subscribe { launcher.apps = it }

    launcher.selects
        .takeUntil(destroys)
        .subscribe { router.start(it) }
  }
}
