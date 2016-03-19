package com.merono.rlauncher

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import rx.subjects.PublishSubject
import java.util.*

// android.R.dimen.app_icon_size

class Home : Activity() {

  private val grid by lazy { findViewById(R.id.left_drawer) as GridView }

  private val destroys = PublishSubject.create<Void>()

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.home)

    installedAppChanges().takeUntil(destroys).subscribe {
      val apps = loadApplications()
      bindApplications(apps)
    }

    val apps = loadApplications()
    bindApplications(apps)

    grid.onItemClickListener = OnItemClickListener { parent, view, position, id ->
      val app = parent.getItemAtPosition(position) as AppInfo
      startActivity(app.intent)
    }
  }

  /** Creates a new applications adapter for the grid view and registers it. */
  private fun bindApplications(apps: List<AppInfo>) {
    grid.adapter = AppAdapter(this, apps)
  }

  /** Loads the list of installed applications in mApplications. */
  private fun loadApplications(): List<AppInfo> {
    val mainIntent = Intent(Intent.ACTION_MAIN)
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

    val apps = packageManager.queryIntentActivities(mainIntent, 0)
    apps ?: return emptyList()

    Collections.sort(apps, ResolveInfo.DisplayNameComparator(packageManager))

    return apps.map {
      AppInfo(it, packageManager)
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
