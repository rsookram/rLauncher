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
        holder.label.text = getItem(position).displayName
    }
}

class Holder(val label: TextView) : RecyclerView.ViewHolder(label)

private class Diff : DiffUtil.ItemCallback<App>() {
    override fun areItemsTheSame(oldItem: App, newItem: App): Boolean =
        oldItem.packageName == newItem.packageName

    override fun areContentsTheSame(oldItem: App, newItem: App): Boolean =
        oldItem.displayName == newItem.displayName
}
