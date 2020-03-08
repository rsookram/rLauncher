package io.github.rsookram.rlauncher

import android.content.Context
import android.content.pm.PackageManager
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import io.github.rsookram.rlauncher.interactor.InstalledAppsReceiver
import io.github.rsookram.rlauncher.router.Router
import io.github.rsookram.rlauncher.view.AppAdapter
import io.github.rsookram.rlauncher.view.LauncherView
import io.github.rsookram.rlauncher.viewmodel.LauncherViewModel
import io.github.rsookram.rlauncher.viewmodel.ViewModelFactory

@Component(modules = [LauncherModule::class])
interface LauncherComponent {

    fun installedAppsReceiver(): InstalledAppsReceiver
    fun router(): Router
    fun view(): LauncherView
    fun viewModel(): LauncherViewModel

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance activity: ComponentActivity): LauncherComponent
    }
}

@Module
class LauncherModule {
    @Provides fun context(activity: ComponentActivity): Context = activity
    @Provides fun lifecycleOwner(activity: ComponentActivity): LifecycleOwner = activity

    @Provides fun packageManager(context: Context): PackageManager = context.packageManager

    @Provides fun container(activity: ComponentActivity): ViewGroup =
        activity.findViewById(android.R.id.content)

    @Provides fun appAdapter(viewModel: LauncherViewModel) = AppAdapter(viewModel::onAppSelected)

    @Provides fun installedAppsReceiver(context: Context, viewModel: LauncherViewModel) =
        InstalledAppsReceiver(context, viewModel::onAppsChanged)

    @Provides fun viewModelProvider(activity: ComponentActivity, factory: ViewModelFactory) =
        ViewModelProvider(activity, factory)

    @Provides fun viewModel(provider: ViewModelProvider) = provider.get<LauncherViewModel>()
}
