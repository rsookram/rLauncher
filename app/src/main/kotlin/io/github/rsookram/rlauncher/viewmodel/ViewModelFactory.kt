package io.github.rsookram.rlauncher.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.rsookram.rlauncher.interactor.searchFilter
import javax.inject.Inject

class ViewModelFactory @Inject constructor() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(LauncherViewModel::class.java.isAssignableFrom(modelClass)) {
            "Unsupported ViewModel type: $modelClass"
        }
        @Suppress("UNCHECKED_CAST")
        return LauncherViewModel(::searchFilter) as T
    }
}
