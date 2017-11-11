package com.merono.rlauncher.interactor

import com.merono.rlauncher.entity.App

fun searchFilter(apps: List<App>, query: CharSequence): List<App> =
    apps.filter {
      val normalizedName = "${it.displayName} ${it.packageName}".toLowerCase()
      val normalizedQuery = query.toString().toLowerCase()

      normalizedName.contains(normalizedQuery)
    }
