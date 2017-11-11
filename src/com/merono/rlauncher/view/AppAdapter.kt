package com.merono.rlauncher.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.merono.rlauncher.R
import com.merono.rlauncher.entity.App
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class AppAdapter : RecyclerView.Adapter<Holder>() {

    var apps = emptyList<App>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val selectSubject = PublishSubject.create<App>()
    val selects: Observable<App> = selectSubject.hide()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.item_app, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val context = holder.itemView.context
        val pm = context.packageManager

        val app = apps[position]

        val info = pm.getApplicationInfo(app.packageName, 0)
        val icon = info.loadIcon(pm)
        val size = context.resources.getDimensionPixelSize(
                android.R.dimen.app_icon_size
        )
        icon.setBounds(0, 0, size, size)
        holder.text.setCompoundDrawables(icon, null, null, null)

        holder.text.text = app.displayName

        holder.itemView.setOnClickListener {
            selectSubject.onNext(app)
        }
    }

    override fun getItemCount(): Int = apps.size
}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val text = itemView as TextView
}
