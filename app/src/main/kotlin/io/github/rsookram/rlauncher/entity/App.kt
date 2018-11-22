package io.github.rsookram.rlauncher.entity

/**
 * Represents an app installed on the device and an Activity contained which
 * can be launched
 *
 * @param packageName The package `className` is part of
 * @param className The name of the Activity class which can be launched
 */
data class App(
    val packageName: String,
    val className: String,
    val displayName: String
)
