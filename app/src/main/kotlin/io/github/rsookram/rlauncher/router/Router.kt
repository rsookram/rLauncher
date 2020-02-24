package io.github.rsookram.rlauncher.router

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import io.github.rsookram.rlauncher.entity.App
import javax.inject.Inject

class Router @Inject constructor(private val context: Context) {

    fun start(app: App) {
        val intent = newIntent(app)
        context.startActivity(intent)
    }

    private fun newIntent(app: App) =
        Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)
            .setComponent(ComponentName(app.packageName, app.className))
            .setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            )
}
