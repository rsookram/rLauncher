package io.github.rsookram.rlauncher

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import io.github.rsookram.rlauncher.interactor.InstalledAppsReceiver
import io.github.rsookram.rlauncher.router.Router
import io.github.rsookram.rlauncher.view.AppAdapter
import io.github.rsookram.rlauncher.view.LauncherView
import io.github.rsookram.rlauncher.viewmodel.LauncherViewModel
import io.github.rsookram.rlauncher.viewmodel.ViewModelFactory

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val vm = ViewModelProvider(this, ViewModelFactory()).get<LauncherViewModel>()

        val routeTo = Router(this)::start

        val contentView = findViewById<ViewGroup>(android.R.id.content)
        val view = LauncherView(contentView, vm, AppAdapter(vm::onAppSelected))

        lifecycle.addObserver(InstalledAppsReceiver(this, vm::onAppsChanged))

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
}
