package io.github.rsookram.rlauncher.view

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import io.github.rsookram.rlauncher.entity.App
import javax.inject.Inject

class AppIconLoader @Inject constructor(private val packageManager: PackageManager) {

    fun load(app: App): Drawable {
        val info = packageManager.getApplicationInfo(app.packageName, 0)
        return info.loadIcon(packageManager)
    }
}
