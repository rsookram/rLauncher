package com.merono.rlauncher

import android.content.ComponentName
import android.content.Context
import android.content.Intent

class AppRouter(private val context: Context) : Router {

  override fun start(app: App) {
    val intent = newIntent(app)
    context.startActivity(intent)
  }

  private fun newIntent(app: App): Intent =
      Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
        component = ComponentName(app.packageName, app.className)
        flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
      }
}
