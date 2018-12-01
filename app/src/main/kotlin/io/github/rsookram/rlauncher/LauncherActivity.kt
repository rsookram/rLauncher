package io.github.rsookram.rlauncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.rsookram.lifecycle.observeNonNull

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

        vm.appLaunches.observeNonNull(this) {
            it.getContentIfNotHandled()?.let { app ->
                router.start(app)
            }
        }

        vm.apps.observeNonNull(this, view::setApps)

        vm.scrollsToStart.observeNonNull(this) {
            if (it.getContentIfNotHandled() != null) {
                view.scrollToStart()
            }
        }
    }

    override fun onBackPressed() {
        // Intentionally don't call super so that the launcher isn't closed
        // when back is pressed
    }
}
