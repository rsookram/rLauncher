package com.merono.rlauncher.router

import com.merono.rlauncher.entity.App

interface Router {

    fun start(app: App)
}
