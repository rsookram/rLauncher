package io.github.rsookram.rlauncher.view

import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import io.github.rsookram.rlauncher.R
import io.github.rsookram.rlauncher.entity.App
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_searchable_launcher.view.*

class LauncherView(
        context: Context,
        private val appAdapter: AppAdapter
) : LinearLayout(context), LauncherUi {

    override var apps: List<App>
        get() = appAdapter.apps
        set(value) {
            appAdapter.apps = value
        }

    override val selects = appAdapter.selects

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

        launcher.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
            adapter = appAdapter

            setOnApplyWindowInsetsListener { v, insets ->
                v.setPadding(
                        paddingLeft,
                        insets.systemWindowInsetTop,
                        paddingRight,
                        paddingBottom
                )

                insets.consumeSystemWindowInsets()
            }
        }

        search_box.requestFocus()
    }

    override fun clearQuery() {
        search_box.setText("")
    }
}
