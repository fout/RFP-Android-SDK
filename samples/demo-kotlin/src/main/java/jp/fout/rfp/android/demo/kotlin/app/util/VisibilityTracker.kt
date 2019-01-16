package jp.fout.rfp.android.demo.kotlin.app.util

import android.app.Activity
import android.graphics.Rect
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import timber.log.Timber
import java.util.*

class VisibilityTracker(activity: Activity) {
    companion object {
        const val VISIBILITY_CHECK_DELAY_MILLIS: Long = 100
    }

    private val mTrackedViews = WeakHashMap<View, TrackingInfo>()
    private var mVisibilityTrackerListener: VisibilityTrackerListener? = null
    private var mIsVisibilityCheckScheduled: Boolean = false
    private var mVisibilityChecker: VisibilityChecker
    private var mVisibilityHandler: Handler
    private var mVisibilityRunnable: Runnable

    init {
        val rootView = activity.window.decorView
        val viewTreeObserver = rootView.viewTreeObserver

        mVisibilityHandler = Handler()
        mVisibilityChecker = VisibilityChecker()
        mVisibilityRunnable = VisibilityRunnable()

        if (viewTreeObserver.isAlive) {
            val mOnPreDrawListener = ViewTreeObserver.OnPreDrawListener {
                scheduleVisibilityCheck()
                true
            }
            viewTreeObserver.addOnPreDrawListener(mOnPreDrawListener)
        } else {
            Timber.d("Visibility tracker root view is not alive")
        }
    }

    fun addView(view: View, minVisiblePercentageViewed: Int) {
        var trackingInfo: TrackingInfo? = mTrackedViews[view]
        if (trackingInfo == null) {
            // view is not yet being tracked
            trackingInfo = TrackingInfo()
            mTrackedViews[view] = trackingInfo
            scheduleVisibilityCheck()
        }

        trackingInfo.mRootView = view
        trackingInfo.mMinVisiblePercent = minVisiblePercentageViewed
    }

    fun setVisibilityTrackerListener(listener: VisibilityTrackerListener) {
        mVisibilityTrackerListener = listener
    }

    fun removeVisibilityTrackerListener() {
        mVisibilityTrackerListener = null
    }

    private fun scheduleVisibilityCheck() {
        if (mIsVisibilityCheckScheduled) {
            return
        }
        mIsVisibilityCheckScheduled = true
        mVisibilityHandler.postDelayed(mVisibilityRunnable, VISIBILITY_CHECK_DELAY_MILLIS)
    }

    interface VisibilityTrackerListener {
        fun onVisibilityChanged(visibleViews: List<View>, invisibleViews: List<View>)
    }

    private class VisibilityChecker {
        private val mClipRect = Rect()

        internal fun isVisible(view: View?, minPercentageViewed: Int): Boolean {
            if (view == null || view.visibility != View.VISIBLE || view.parent == null) {
                return false
            }

            if (!view.getGlobalVisibleRect(mClipRect)) {
                return false
            }

            val visibleArea = mClipRect.height().toLong() * mClipRect.width()
            val totalViewArea = view.height.toLong() * view.width

            return totalViewArea > 0 && 100 * visibleArea >= minPercentageViewed * totalViewArea
        }
    }

    private inner class VisibilityRunnable internal constructor() : Runnable {
        private val mVisibleViews: MutableList<View>
        private val mInvisibleViews: MutableList<View>

        init {
            mVisibleViews = ArrayList()
            mInvisibleViews = ArrayList()
        }

        override fun run() {
            mIsVisibilityCheckScheduled = false
            for ((view, value) in mTrackedViews) {
                val minPercentageViewed = value.mMinVisiblePercent

                if (mVisibilityChecker.isVisible(view, minPercentageViewed)) {
                    mVisibleViews.add(view)
                } else {
                    mInvisibleViews.add(view)
                }

                if (mVisibilityTrackerListener != null) {
                    mVisibilityTrackerListener!!.onVisibilityChanged(mVisibleViews, mInvisibleViews)
                }

                mVisibleViews.clear()
                mInvisibleViews.clear()
            }
        }
    }

    private inner class TrackingInfo {
        internal var mRootView: View? = null
        internal var mMinVisiblePercent: Int = 0
    }
}
