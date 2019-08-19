package jp.fout.rfp.android.demo.kotlin.app.util

import android.support.test.espresso.IdlingResource

class LoadingIdlingResource : IdlingResource {
    private var _idle: Boolean = false
    var idle: Boolean
        get() = _idle
        set(newValue) {
            _idle = newValue
        }

    var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String = LoadingIdlingResource::class.java.simpleName

    override fun isIdleNow(): Boolean {
        if (idle) {
            resourceCallback?.onTransitionToIdle()
        }
        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }
}