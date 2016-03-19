package com.merono.rlauncher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

/** GridView adapter to show the list of all installed applications. */
class AppAdapter(context: Context, apps: List<AppInfo>) :
    ArrayAdapter<AppInfo>(context, -1, apps) {

  private val inflater = LayoutInflater.from(context)

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    var view = convertView ?: inflater.inflate(R.layout.application, parent, false)

    val ic = getItem(position).icon
    (view as ImageView).setImageDrawable(ic)

    return view
  }
}
