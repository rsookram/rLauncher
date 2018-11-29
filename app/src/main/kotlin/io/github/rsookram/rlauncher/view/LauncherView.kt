package io.github.rsookram.rlauncher.view

import android.content.Context
import android.widget.LinearLayout
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import io.github.rsookram.rlauncher.R
import io.github.rsookram.rlauncher.entity.App
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_launcher.view.*

class LauncherView(
    context: Context,
    private val appAdapter: AppAdapter
) : LinearLayout(context) {

    val searches: Observable<CharSequence>
        get() = search_box.textChanges()

    init {
        orientation = VERTICAL

        setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsetBottom)

            insets
        }

        inflate(context, R.layout.view_launcher, this)

        launcher.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            adapter = appAdapter
            itemAnimator = null

            setOnApplyWindowInsetsListener { v, insets ->
                v.updatePadding(top = insets.systemWindowInsetTop)

                insets.consumeSystemWindowInsets()
            }
        }
    }

    fun setApps(apps: List<App>) {
        appAdapter.submitList(apps)
    }

    fun setQuery(query: CharSequence) {
        if (query.toString() != search_box.text.toString()) {
            search_box.setText(query)
        }
    }

    fun scrollToStart() {
        launcher.scrollToPosition(0)
    }
}
