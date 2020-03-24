package io.github.rsookram.rlauncher

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import io.github.rsookram.rlauncher.interactor.InstalledAppsReceiver
import io.github.rsookram.rlauncher.router.Router
import io.github.rsookram.rlauncher.view.AppAdapter
import io.github.rsookram.rlauncher.view.LauncherView
import io.github.rsookram.rlauncher.viewmodel.LauncherViewModel
import io.github.rsookram.rlauncher.viewmodel.ViewModelFactory

class Dependencies(activity: ComponentActivity) {

    val viewModel: LauncherViewModel = ViewModelProvider(activity, ViewModelFactory()).get()

    val view = LauncherView(
        activity.findViewById(android.R.id.content),
        viewModel,
        activity,
        AppAdapter(viewModel::onAppSelected)
    )

    val installedAppsReceiver = InstalledAppsReceiver(activity, viewModel::onAppsChanged)

    val router = Router(activity)
}
