package com.merono.rlauncher

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

// TODO: Make this a data class of package name, name and?
/** Represents a launchable application.  */
class AppInfo(info: ResolveInfo, manager: PackageManager) {

  val intent = Intent(Intent.ACTION_MAIN)

  val icon: Drawable = info.activityInfo.loadIcon(manager)

  init {
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.component = ComponentName(
        info.activityInfo.applicationInfo.packageName,
        info.activityInfo.name
    )
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) {
      return true
    }
    if (other !is AppInfo) {
      return false
    }

    return intent.component.className == other.intent.component.className
  }

  override fun hashCode(): Int {
    val name = intent.component.className
    return if (name != null) name.hashCode() else 0
  }
}
