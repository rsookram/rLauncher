package io.github.rsookram.rlauncher.interactor

import com.google.common.truth.Truth.assertThat
import io.github.rsookram.rlauncher.entity.App
import org.junit.Test

class SearchFilterTest {

    private val appBrowser = App("com.android.browser", "", "Browser")
    private val appCalculator = App("com.android.calculator2", "", "Calculator")
    private val appCamera = App("com.android.camera", "", "Camera")

    private val apps = listOf(appBrowser, appCalculator, appCamera)

    @Test
    fun emptyQueryFiltersNothing() {
        val result = searchFilter(apps, "")

        assertThat(result).isEqualTo(apps)
    }

    @Test
    fun overspecificQueryResultsInNothing() {
        val result = searchFilter(apps, "there is no app with a name that would match this")

        assertThat(result).isEqualTo(emptyList<App>())
    }

    @Test
    fun canSearchByPackageName() {
        val result = searchFilter(apps, "com.android.brows")

        assertThat(result).isEqualTo(listOf(appBrowser))
    }

    @Test
    fun canSearchByDisplayName() {
        val result = searchFilter(apps, "Camer")

        assertThat(result).isEqualTo(listOf(appCamera))
    }

    @Test
    fun searchIsCaseInsensitive() {
        val result = searchFilter(apps, "BROWSE")

        assertThat(result).isEqualTo(listOf(appBrowser))
    }
}
