package jp.fout.rfp.android.demo.kotlin.app

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.fout.rfp.android.sdk.video.VideoAdActivity
import jp.fout.rfp.android.sdk.video.VideoTrackingEvent
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep

@LargeTest
@RunWith(AndroidJUnit4::class)
class VideoAdActivityTest {
    @Suppress("BooleanLiteralArgument")
    @get:Rule
    var rule = IntentsTestRule(VideoAdActivity::class.java, false, false)

    @Before
    fun setUp() {
        val vast = Fixture.asString(this, "vast/sample.xml")
        val event = VideoTrackingEvent(hashMapOf())

        val intent = Intent().apply {
            putExtra("vast_xml", vast)
            putExtra("current_position", 0)
            putExtra("tracking_event", event)
        }
        rule.launchActivity(intent)
    }

    @Test
    fun testVideoAdActivity() {
        sleep(1000)

        // 開いた直後はコントロールパネルは表示されていない
        onView(withId(R.id.controlPanel))
                .check(matches(not(isDisplayed())))

        // 動画部分をクリックするとコントロールパネルが表示される
        onView(withId(R.id.videoView))
                .check(matches(isDisplayed()))
                .perform(click())
        onView(withId(R.id.controlPanel))
                .check(matches(isDisplayed()))
                .check(matches(hasChildCount(3)))
                .check(matches(withChild(withId(R.id.buttonSwitch))))
                .check(matches(withChild(withId(R.id.seekBar))))
                .check(matches(withChild(withId(R.id.textDuration))))
        onView(withId(R.id.buttonSwitch))
                .check(matches(isDisplayed()))
        onView(withId(R.id.seekBar))
                .check(matches(isDisplayed()))
        onView(withId(R.id.textDuration))
                .check(matches(isDisplayed()))

        // 3秒経過するとコントロールパネルが消える
        sleep(3000)

        onView(withId(R.id.controlPanel))
                .check(matches(not(isDisplayed())))

        // 広告をクリックするとブラウザでLPが表示される
        onView(withId(R.id.buttonOpen))
                .check(matches(isDisplayed()))
                .check(matches(isClickable()))
                .perform(click())

        intended(allOf(
//                toPackage("com.android.chrome"),
                hasAction(Intent.ACTION_VIEW)
        ))
    }
}
