package io.github.rsookram.rlauncher.interactor

import io.github.rsookram.rlauncher.entity.App
import java.util.*

/**
 * Returns a filtered version of the given list of apps which contain the given
 * query string.
 */
fun searchFilter(apps: List<App>, query: CharSequence): List<App> {
    val defaultLocale = Locale.getDefault()
    val normalizedQuery = query.toString().toLowerCase(defaultLocale)

    return apps.filter {
        normalizedQuery in "${it.displayName} ${it.packageName}".toLowerCase(defaultLocale)
    }
}
