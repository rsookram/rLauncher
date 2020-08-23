package io.github.rsookram.rlauncher.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.github.rsookram.rlauncher.entity.App

/**
 * ViewModel for [io.github.rsookram.rlauncher.LauncherActivity]
 */
class LauncherViewModel(
    private val search: (List<App>, CharSequence) -> List<App>
) : ViewModel() {

    private val _appLaunches = eventLiveData<App>()
    val appLaunches: LiveData<App> = _appLaunches

    private val _apps = MutableLiveData<List<App>>()
    val apps: LiveData<List<App>> = _apps

    private val _queries = MutableLiveData<CharSequence>()
    val queries: LiveData<CharSequence> = _queries

    private val _scrollsToStart = eventLiveData<Unit>()
    val scrollsToStart: LiveData<Unit> = _scrollsToStart

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
            _appLaunches.setValue(newAppData.first())
            _queries.value = ""
        } else {
            _queries.value = query
        }

        if (_queries.value.isNullOrEmpty()) {
            // Without posting, the list would try to scroll to the start of
            // the filtered list, instead of the original. This is because it
            // takes time to set the list back to the original.
            _scrollsToStart.postValue(Unit)
        }
    }

    fun onAppSelected(app: App) {
        _appLaunches.setValue(app)
        _queries.value = ""
    }
}

private fun <T : Any> eventLiveData() = object : MutableLiveData<T>() {

    private var pending = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, { t ->
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        })
    }

    override fun setValue(value: T) {
        pending = true
        super.setValue(value)
    }
}
