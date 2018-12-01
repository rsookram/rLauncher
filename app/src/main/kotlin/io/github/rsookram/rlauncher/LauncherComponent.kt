package io.github.rsookram.rlauncher

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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

    @Component.Builder
    interface Builder {
        @BindsInstance fun activity(activity: AppCompatActivity): Builder
        fun build(): LauncherComponent
    }
}

@Module
class LauncherModule {
    @Provides fun context(activity: AppCompatActivity): Context = activity
    @Provides fun lifecycleOwner(activity: AppCompatActivity): LifecycleOwner = activity

    @Provides fun container(activity: AppCompatActivity): ViewGroup =
        activity.findViewById(android.R.id.content)

    @Provides fun appAdapter(viewModel: LauncherViewModel) = AppAdapter(viewModel::onAppSelected)

    @Provides fun installedAppsReceiver(context: Context, viewModel: LauncherViewModel) =
        InstalledAppsReceiver(context, viewModel::onAppsChanged)

    @Provides fun viewModelProvider(activity: AppCompatActivity, factory: ViewModelFactory) =
        ViewModelProviders.of(activity, factory)

    @Provides fun viewModel(provider: ViewModelProvider) = provider.get<LauncherViewModel>()
}
