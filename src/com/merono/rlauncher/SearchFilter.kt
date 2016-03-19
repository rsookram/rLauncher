package com.merono.rlauncher

fun searchFilter(apps: List<App>, query: CharSequence): List<App> {
  return apps.filter {
    val normalizedName = "${it.displayName} ${it.packageName}".toLowerCase()
    val normalizedQuery = query.toString().toLowerCase()

    normalizedName.contains(normalizedQuery)
  }
}
