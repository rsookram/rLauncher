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

    private val _appLaunches = MutableLiveData<LiveEvent<App>>()
    val appLaunches: LiveData<LiveEvent<App>> = _appLaunches

    private val _apps = MutableLiveData<List<App>>()
    val apps: LiveData<List<App>> = _apps

    private val _queries = MutableLiveData<CharSequence>()
    val queries: LiveData<CharSequence> = _queries

    private val _scrollsToStart = MutableLiveData<LiveEvent<Unit>>()
    val scrollsToStart: LiveData<LiveEvent<Unit>> = _scrollsToStart

    private var installedApps = emptyList<App>()

    fun onAppsChanged(newApps: List<App>) {
        installedApps = newApps

        val query = _queries.value ?: ""
        _apps.value = search(installedApps, query)
    }

    fun onQueryChanged(query: CharSequence) {
        val newAppData = search(installedApps, query)
        _apps.value = newAppData

        if (newAppData.size == 1) {
            _appLaunches.value = LiveEvent(newAppData.first())
            _queries.value = ""
        } else {
            _queries.value = query
        }

        if (_queries.value.isNullOrEmpty()) {
            // Without posting, the list would try to scroll to the start of
            // the filtered list, instead of the original. This is because it
            // takes time to set the list back to the original.
            _scrollsToStart.postValue(LiveEvent(Unit))
        }
    }

    fun onAppSelected(app: App) {
        _appLaunches.value = LiveEvent(app)
        _queries.value = ""
    }
}
