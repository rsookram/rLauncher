package io.github.rsookram.rlauncher.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.textChanges
import io.github.rsookram.rlauncher.databinding.ViewLauncherBinding
import io.github.rsookram.rlauncher.entity.App
import io.reactivex.Observable

class LauncherView(container: ViewGroup, private val appAdapter: AppAdapter) {

    private val binding: ViewLauncherBinding = ViewLauncherBinding.inflate(
        LayoutInflater.from(container.context), container, true
    )

    val searches: Observable<CharSequence>
        get() = binding.searchBox.textChanges()

    init {
        binding.root.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsetBottom)

            insets
        }

        binding.launcher.apply {
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
        if (query.toString() != binding.searchBox.text.toString()) {
            binding.searchBox.setText(query)
        }
    }

    fun scrollToStart() {
        binding.launcher.scrollToPosition(0)
    }
}
