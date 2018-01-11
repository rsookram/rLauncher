package io.github.rsookram.rlauncher.router

import io.github.rsookram.rlauncher.entity.App

interface Router {

    fun start(app: App)
}
