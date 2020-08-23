package io.github.rsookram.rlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import io.github.rsookram.lifecycle.observeNonNull

/**
 * The entry point into the app
 */
class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dependencies = Dependencies(this)

        val vm = dependencies.viewModel
        val view = dependencies.view
        val router = dependencies.router

        lifecycle.addObserver(dependencies.installedAppsReceiver)

        vm.appLaunches.observeNonNull(this, router::start)

        vm.apps.observeNonNull(this, view::setApps)

        vm.scrollsToStart.observeNonNull(this) {
            view.scrollToStart()
        }
    }

    override fun onBackPressed() {
        // Intentionally don't call super so that the launcher isn't closed
        // when back is pressed
    }
}
