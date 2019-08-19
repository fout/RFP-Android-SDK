package jp.fout.rfp.android.demo.kotlin.app.util

import jp.fout.rfp.android.demo.kotlin.app.Fixture
import jp.fout.rfp.android.demo.kotlin.app.vo.Article
import jp.fout.rfp.android.sdk.model.InstreamInfoModelImpl
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import java.util.*

object TestUtil {
    fun createArticle(id: Int) = Article(
            id = id,
            date = Date(),
            link = "https://example.com/$id",
            title = "Title $id",
            excerpt = "Excerpt $id",
            content = "Content $id",
            featuredMediaUrl = "https://picsum.photos/200?random=$id"
    )

    fun createArticles(count: Int, id: Int): List<Article> {
        return (0 until count).map { n ->
            createArticle(id + n)
        }
    }

    private fun createInstreamInfoModel(position: Int): InstreamInfoModelImpl {
        return InstreamInfoModelImpl().apply {
            setTitle("Ad Title $position")
            setContent("Ad Content $position")
            setPosition(position)
            setCreativeUrl("https://picsum.photos/200?random=ad$position")
            setCreativeWidth(200.0)
            setCreativeHeight(200.0)
            setDisplayedAdvertiser("Advertiser $position")
            setCtaText("CTA Text $position")
            setVastXml("")
            //estimatedCpc
            setAdId("AD_ID_$position")
            setAdvertiserId("ADVERTISER_ID_$position")
            setDat("")
            setUrlScheme("")
            setRedirectUrl("https://example.com")
            setSessionId("SESSION_ID_$position")
            setConvType("")
            setTpImpUrls(arrayOf())
        }
    }

    fun createAd(position: Int): RFPInstreamInfoModel = createInstreamInfoModel(position)

    fun createAds(positions: List<Int>): List<RFPInstreamInfoModel> {
        return positions.map { position ->
            createAd(position)
        }
    }

    fun createVideoAd(position: Int): RFPInstreamInfoModel {
        return createInstreamInfoModel(position).apply {
            val vast = Fixture.asString(this, "vast/sample.xml")
            setVastXml(vast)
        }
    }

    fun createVideoAds(positions: List<Int>): List<RFPInstreamInfoModel> {
        return positions.map { position ->
            createVideoAd(position)
        }
    }

    fun withRecyclerView(recyclerViewId: Int) = RecyclerViewMatcher(recyclerViewId)
}
