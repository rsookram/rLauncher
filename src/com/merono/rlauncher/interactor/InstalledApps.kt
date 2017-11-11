package com.merono.rlauncher.interactor

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.merono.rlauncher.entity.App
import io.reactivex.Observable
import java.util.Collections

fun installedApps(context: Context): Observable<List<App>> =
        context.installedAppChanges()
                .startWith(Unit)
                .map { loadInstalledApps(context.packageManager) }

private fun loadInstalledApps(pm: PackageManager): List<App> {
    val mainIntent = Intent(Intent.ACTION_MAIN)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    val apps = pm.queryIntentActivities(mainIntent, 0)
    Collections.sort(apps, ResolveInfo.DisplayNameComparator(pm))

    return apps.map {
        val info = it.activityInfo
        val packageName = info.applicationInfo.packageName
        App(packageName, info.name, getAppName(pm, packageName))
    }
}

private fun Context.installedAppChanges(): Observable<Unit> =
        broadcasts(newIntentFilter()).map {}

/** Creates intent filter for when apps are (un)installed */
private fun newIntentFilter(): IntentFilter =
        IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply {
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }

private fun getAppName(pm: PackageManager, packageName: String): CharSequence {
    val info = pm.getApplicationInfo(packageName, 0)
    return pm.getApplicationLabel(info)
}
