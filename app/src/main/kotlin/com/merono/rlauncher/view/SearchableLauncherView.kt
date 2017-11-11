package com.merono.rlauncher.view

import android.content.Context
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.widget.textChanges
import com.merono.rlauncher.R
import com.merono.rlauncher.entity.App
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_searchable_launcher.view.*

class SearchableLauncherView(context: Context) :
        LinearLayout(context), SearchableLauncher {

    override var apps: List<App>
        get() = launcher.apps
        set(value) {
            launcher.apps = value
        }

    override val selects: Observable<App>
        get() = launcher.selects

    override val searches: Observable<CharSequence>
        get() = search_box.textChanges()

    init {
        orientation = VERTICAL

        setOnApplyWindowInsetsListener { v, insets ->
            v.setPadding(
                    paddingLeft,
                    paddingTop,
                    paddingRight,
                    insets.systemWindowInsetBottom
            )

            insets
        }

        inflate(context, R.layout.view_searchable_launcher, this)

        search_box.requestFocus()
    }

    override fun clearQuery() {
        search_box.setText("")
    }
}
