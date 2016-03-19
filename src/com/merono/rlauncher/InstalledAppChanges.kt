package com.merono.rlauncher

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import rx.Observable

fun Context.installedAppChanges(): Observable<Unit> =
    broadcasts(newIntentFilter()).map { }

/** Creates intent filter for when apps are (un)installed */
private fun newIntentFilter(): IntentFilter =
    IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply {
      addAction(Intent.ACTION_PACKAGE_REMOVED)
      addAction(Intent.ACTION_PACKAGE_CHANGED)
      addDataScheme("package")
    }
