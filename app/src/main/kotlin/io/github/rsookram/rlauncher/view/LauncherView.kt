package io.github.rsookram.rlauncher.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updatePadding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.rsookram.rlauncher.databinding.ViewLauncherBinding
import io.github.rsookram.rlauncher.entity.App
import io.github.rsookram.rlauncher.viewmodel.LauncherViewModel
import javax.inject.Inject

class LauncherView @Inject constructor(
    container: ViewGroup,
    vm: LauncherViewModel,
    lifecycleOwner: LifecycleOwner,
    private val appAdapter: AppAdapter
) {

    private val binding: ViewLauncherBinding = ViewLauncherBinding.inflate(
        LayoutInflater.from(container.context), container, true
    )

    init {
        binding.lifecycleOwner = lifecycleOwner
        binding.vm = vm

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

    fun scrollToStart() {
        binding.launcher.scrollToPosition(0)
    }
}
