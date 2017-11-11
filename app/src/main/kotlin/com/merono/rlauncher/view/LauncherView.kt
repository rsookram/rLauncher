package com.merono.rlauncher.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.merono.rlauncher.entity.App

class LauncherView(context: Context, attrs: AttributeSet) :
        RecyclerView(context, attrs), Launcher {

    private val appAdapter = AppAdapter()

    override var apps: List<App>
        get() = appAdapter.apps
        set(value) {
            appAdapter.apps = value
        }

    override val selects = appAdapter.selects

    init {
        layoutManager = LinearLayoutManager(context, VERTICAL, true)
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
}
