package io.github.rsookram.rlauncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = DaggerLauncherComponent.builder()
            .activity(this)
            .build()

        val vm = component.viewModel()
        val view = component.view()
        val router = component.router()

        lifecycle.addObserver(component.installedAppsReceiver())

        vm.appLaunches.observe(this, Observer {
            val app = it?.getContentIfNotHandled()
            if (app != null) {
                router.start(app)
            }
        })

        vm.apps.observe(this, Observer {
            if (it != null) {
                view.setApps(it)
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
