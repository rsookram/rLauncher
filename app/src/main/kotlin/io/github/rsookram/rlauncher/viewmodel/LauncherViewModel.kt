package io.github.rsookram.rlauncher.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.rsookram.lifecycle.LiveEvent
import io.github.rsookram.rlauncher.entity.App
import io.github.rsookram.rlauncher.interactor.searchFilter

class LauncherViewModel(
    private val search: (List<App>, CharSequence) -> List<App> = ::searchFilter
) : ViewModel() {

    private val appLaunchData = MutableLiveData<LiveEvent<App>>()
    val appLaunches: LiveData<LiveEvent<App>> = appLaunchData

    private val appData = MutableLiveData<List<App>>()
    val apps: LiveData<List<App>> = appData

    private val queryData = MutableLiveData<CharSequence>()
    val queries: LiveData<CharSequence> = queryData

    private val scrollToStartData = MutableLiveData<LiveEvent<Unit>>()
    val scrollsToStart: LiveData<LiveEvent<Unit>> = scrollToStartData

    private var installedApps = emptyList<App>()

    fun onAppsChanged(newApps: List<App>) {
        installedApps = newApps

        val query = queryData.value ?: ""
        appData.value = search(installedApps, query)
    }

    fun onQueryChanged(query: CharSequence) {
        val newAppData = search(installedApps, query)
        appData.value = newAppData

        if (newAppData.size == 1) {
            appLaunchData.value = LiveEvent(newAppData.first())
            queryData.value = ""
        } else {
            queryData.value = query
        }

        if (queryData.value.isNullOrEmpty()) {
            // Without posting, the list would try to scroll to the start of
            // the filtered list, instead of the original. This is because it
            // takes time to set the list back to the original.
            scrollToStartData.postValue(LiveEvent(Unit))
        }
    }

    fun onAppSelected(app: App) {
        appLaunchData.value = LiveEvent(app)
        queryData.value = ""
    }
}
