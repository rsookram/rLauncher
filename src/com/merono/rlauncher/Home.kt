package com.merono.rlauncher

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import rx.subjects.PublishSubject
import java.util.*

class Home : Activity() {

  private val appsList by lazy { findViewById(R.id.apps_list) as RecyclerView }

  private val destroys = PublishSubject.create<Void>()

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.home)

    appsList.layoutManager = LinearLayoutManager(this)
    // TODO: stack from end? or reverse layout?

    val adapter = AppAdapter()
    appsList.adapter = adapter

    installedAppChanges().takeUntil(destroys).subscribe {
      val apps = loadApplications()
      adapter.apps = apps
    }

    val apps = loadApplications()
    adapter.apps = apps

    adapter.selects.subscribe {
      startActivity(newIntent(it))
    }
  }

  /** Loads the list of installed applications in mApplications. */
  private fun loadApplications(): List<AppInfo> {
    val mainIntent = Intent(Intent.ACTION_MAIN)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    val apps = packageManager.queryIntentActivities(mainIntent, 0)
    apps ?: return emptyList()

    Collections.sort(apps, ResolveInfo.DisplayNameComparator(packageManager))

    return apps.map {
      val info = it.activityInfo
      AppInfo(info.applicationInfo.packageName, info.name)
    }
  }

  override fun onBackPressed() {
    // Intentionally don't call super so that the launcher isn't closed
    // when back is pressed
  }

  public override fun onDestroy() {
    super.onDestroy()
    destroys.onNext(null)
  }
}

private fun newIntent(appInfo: AppInfo): Intent =
    Intent(Intent.ACTION_MAIN).apply {
      addCategory(Intent.CATEGORY_LAUNCHER)
      component = ComponentName(appInfo.packageName, appInfo.className)
      flags =
          Intent.FLAG_ACTIVITY_NEW_TASK or
          Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
    }
