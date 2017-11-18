package com.merono.rlauncher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.merono.rlauncher.interactor.installedApps
import com.merono.rlauncher.presenter.LauncherPresenter
import com.merono.rlauncher.router.AppRouter
import com.merono.rlauncher.view.SearchableLauncherView
import io.reactivex.subjects.PublishSubject

class HomeActivity : AppCompatActivity() {

    private val destroys = PublishSubject.create<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = SearchableLauncherView(this)
        setContentView(view)

        LauncherPresenter(
                view,
                installedApps(this),
                destroys.hide(),
                AppRouter(this)
        )
    }

    override fun onBackPressed() {
        // Intentionally don't call super so that the launcher isn't closed
        // when back is pressed
    }

    override fun onDestroy() {
        super.onDestroy()
        destroys.onNext(Unit)
    }
}
