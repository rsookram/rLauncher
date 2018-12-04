package io.github.rsookram.lifecycle

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents
 * an event.
 *
 * Based off https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af
 */
open class LiveEvent<out T : Any>(private val content: T) {

    private var handled = false

    /** Returns the content and prevents its use again. */
    fun getContentIfNotHandled(): T? {
        if (handled) {
            return null
        }

        handled = true
        return content
    }
}
