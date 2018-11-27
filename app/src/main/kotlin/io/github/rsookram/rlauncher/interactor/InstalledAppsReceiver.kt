package io.github.rsookram.rlauncher.interactor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.github.rsookram.rlauncher.entity.App

/** Listens to changes in the launchable apps installed on the device */
class InstalledAppsReceiver(
    private val context: Context,
    onAppsChanged: (List<App>) -> Unit
) : DefaultLifecycleObserver {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            onAppsChanged(loadInstalledApps())
        }
    }

    init {
        onAppsChanged(loadInstalledApps())

        context.registerReceiver(
            receiver,
            IntentFilter(Intent.ACTION_PACKAGE_ADDED).apply {
                addAction(Intent.ACTION_PACKAGE_REMOVED)
                addAction(Intent.ACTION_PACKAGE_CHANGED)
                addDataScheme("package")
            }
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        context.unregisterReceiver(receiver)
    }

    private fun loadInstalledApps(): List<App> {
        val pm = context.packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)

        return pm.queryIntentActivities(mainIntent, 0)
            .sortedWith(ResolveInfo.DisplayNameComparator(pm))
            .map {
                val info = it.activityInfo
                val packageName = info.applicationInfo.packageName
                App(packageName, info.name, getAppName(pm, packageName))
            }
    }

    private fun getAppName(pm: PackageManager, packageName: String): String {
        val info = pm.getApplicationInfo(packageName, 0)
        return pm.getApplicationLabel(info).toString()
    }
}
