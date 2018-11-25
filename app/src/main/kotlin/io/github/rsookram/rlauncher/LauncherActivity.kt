package io.github.rsookram.rlauncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import io.github.rsookram.rlauncher.interactor.installedApps
import io.github.rsookram.rlauncher.router.Router
import io.github.rsookram.rlauncher.view.AppAdapter
import io.github.rsookram.rlauncher.view.LauncherView
import io.github.rsookram.rlauncher.viewmodel.LauncherViewModel
import io.github.rsookram.rlauncher.viewmodel.ViewModelFactory
import io.reactivex.subjects.PublishSubject

class LauncherActivity : AppCompatActivity() {

    private val destroys = PublishSubject.create<Unit>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = ViewModelProvider(this, ViewModelFactory()).get<LauncherViewModel>()

        val installedApps = installedApps(this)
        val routeTo = Router(this)::start

        val view = LauncherView(this, AppAdapter(vm::onAppSelected))
        setContentView(view)

        installedApps
            .takeUntil(destroys)
            .subscribe(vm::onAppsChanged)

        view.searches
            .distinctUntilChanged(CharSequence::toString)
            .subscribe(vm::onQueryChanged)

        vm.appLaunches.observe(this, Observer {
            val app = it?.getContentIfNotHandled()
            if (app != null) {
                routeTo(app)
            }
        })

        vm.apps.observe(this, Observer {
            if (it != null) {
                view.setApps(it)
            }
        })

        vm.queries.observe(this, Observer {
            if (it != null) {
                view.setQuery(it)
            }
        })

        vm.scrollsToStart.observe(this, Observer {
            if (it.getContentIfNotHandled() != null) {
                view.scrollToStart()
            }
        })
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
