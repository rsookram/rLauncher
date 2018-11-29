package io.github.rsookram.rlauncher.view

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.rsookram.rlauncher.R
import io.github.rsookram.rlauncher.entity.App

class AppAdapter(
    private val onAppSelected: (App) -> Unit
) : ListAdapter<App, Holder>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_app, parent, false) as TextView

        val holder = Holder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onAppSelected(getItem(position))
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val context = holder.itemView.context
        val pm = context.packageManager

        val app = getItem(position)

        val info = pm.getApplicationInfo(app.packageName, 0)
        val icon = info.loadIcon(pm)
        val size = context.resources.getDimensionPixelSize(
            android.R.dimen.app_icon_size
        )
        icon.setBounds(0, 0, size, size)
        holder.label.setCompoundDrawablesRelative(icon, null, null, null)

        holder.label.text = app.displayName
    }
}

class Holder(val label: TextView) : RecyclerView.ViewHolder(label)

private class Diff : DiffUtil.ItemCallback<App>() {
    override fun areItemsTheSame(oldItem: App, newItem: App): Boolean =
        oldItem.packageName == newItem.packageName

    // This doesn't handle a change in only the app icon, but that's ok,
    // because it's an infrequent event, and the user will see the update
    // when the view for this app is bound next.
    override fun areContentsTheSame(oldItem: App, newItem: App): Boolean =
        oldItem.displayName == newItem.displayName
}
