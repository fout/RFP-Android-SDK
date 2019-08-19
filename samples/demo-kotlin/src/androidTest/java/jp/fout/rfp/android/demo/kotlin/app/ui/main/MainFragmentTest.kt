package jp.fout.rfp.android.demo.kotlin.app.ui.main

import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.*
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import jp.fout.rfp.android.demo.kotlin.app.R
import jp.fout.rfp.android.demo.kotlin.app.testing.SingleFragmentActivity
import jp.fout.rfp.android.demo.kotlin.app.util.TestUtil
import jp.fout.rfp.android.demo.kotlin.app.util.TestUtil.withRecyclerView
import jp.fout.rfp.android.demo.kotlin.app.util.ViewModelUtil
import jp.fout.rfp.android.demo.kotlin.app.vo.AdResponse
import jp.fout.rfp.android.demo.kotlin.app.vo.Article
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
class MainFragmentTest {
    @Suppress("BooleanLiteralArgument")
    @get:Rule
    val activityRule = IntentsTestRule(SingleFragmentActivity::class.java, true, true)

    private lateinit var articleViewModel: ArticleViewModel
    private val articleListData = MutableLiveData<List<Article>>()
    private val adListData = MutableLiveData<AdResponse<List<RFPInstreamInfoModel>>>()
    private val mainFragment = MainFragment().apply {
        arguments = MainFragmentArgs.Builder()
                .setAdMediaId(2)
                .setAdSpotId("NDQ0OjMx")
                .build()
                .toBundle()
    }

    @Before
    fun setUp() {
        articleViewModel = mock(ArticleViewModel::class.java)
        `when`(articleViewModel.articles).thenReturn(articleListData)
        `when`(articleViewModel.ads).thenReturn(adListData)

        mainFragment.viewModelFactory = ViewModelUtil.createFor(articleViewModel)
        activityRule.activity.setFragment(mainFragment)
    }

    @Test
    fun testAd() {
        val articles = TestUtil.createArticles(10, 1)
        articleListData.postValue(articles)
        val ads = TestUtil.createAds(listOf(2, 5))
        adListData.postValue(AdResponse.success(ads))

        onView(withId(R.id.listArticles))
                .check(matches(isDisplayed()))

        onView(withRecyclerView(R.id.listArticles).atPosition(ads[0].position()))
                .check(matches(isDisplayed()))

        onView(withRecyclerView(R.id.listArticles).atPositionOnView(ads[0].position(), R.id.textTitle))
                .check(matches(withText(ads[0].title())))

        onView(withId(R.id.listArticles))
                .check(matches(isDisplayed()))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(ads[0].position(), click()))

        // 広告をクリックするとブラウザでLPが表示される
        intended(allOf(
//                toPackage("com.android.chrome"),
                hasAction(Intent.ACTION_VIEW),
                hasFlag(Intent.FLAG_ACTIVITY_NEW_TASK)
        ))
    }

    @Test
    fun testVideoAd() {
        val articles = TestUtil.createArticles(10, 1)
        articleListData.postValue(articles)
        val ads = TestUtil.createVideoAds(listOf(2, 5))
        adListData.postValue(AdResponse.success(ads))
        val ad = ads[0]

        sleep(1000)

        onView(withId(R.id.listArticles))
                .check(matches(isDisplayed()))

        onView(withRecyclerView(R.id.listArticles).atPosition(ad.position()))
                .check(matches(isDisplayed()))

        onView(withRecyclerView(R.id.listArticles).atPositionOnView(ad.position(), R.id.ad_title_text))
                .check(matches(withText(ad.title())))

        onView(withId(R.id.listArticles))
                .check(matches(isDisplayed()))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(ad.position(), click()))

        // 広告をクリックすると動画が全画面で表示される
        intended(allOf(
                isInternal(),
                hasExtra("vast_xml", ad.vast_xml()),
                hasExtraWithKey("current_position"),
                hasExtraWithKey("tracking_event")
        ))
    }
}
