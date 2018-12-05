package io.github.rsookram.rlauncher.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.github.rsookram.rlauncher.entity.App
import org.junit.Rule
import org.junit.Test

class LauncherViewModelTest {

    @JvmField @Rule val rule = InstantTaskExecutorRule()

    private val vm = LauncherViewModel()

    private val appBrowser = App("com.android.browser", "", "Browser")
    private val appCalculator = App("com.android.calculator2", "", "Calculator")
    private val appCamera = App("com.android.camera", "", "Camera")

    private val apps = listOf(appBrowser, appCalculator, appCamera)

    @Test
    fun initialAppChangeSetsApps() {
        vm.onAppsChanged(apps)

        assertThat(vm.apps.value).isEqualTo(apps)
    }

    @Test
    fun queryToFilterList() {
        vm.onAppsChanged(apps)

        vm.onQueryChanged("Ca")

        assertThat(vm.apps.value).isEqualTo(listOf(appCalculator, appCamera))
        assertThat(vm.queries.value).isEqualTo("Ca")
    }

    @Test
    fun queryToLaunchApp() {
        vm.onAppsChanged(apps)

        vm.onQueryChanged("Camera")

        assertThat(vm.apps.value).isEqualTo(listOf(appCamera))
        assertThat(vm.appLaunches.value?.getContentIfNotHandled()).isEqualTo(appCamera)
        assertThat(vm.queries.value).isEqualTo("")
    }

    @Test
    fun appSelectionTriggersAnEventAndClearsQuery() {
        vm.onAppSelected(appBrowser)

        assertThat(vm.appLaunches.value?.getContentIfNotHandled()).isEqualTo(appBrowser)
        assertThat(vm.queries.value).isEqualTo("")
    }
}
