package jp.fout.rfp.android.demo.kotlin.app.binding

import androidx.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import jp.fout.rfp.android.demo.kotlin.app.R
import jp.fout.rfp.android.sdk.model.RFPInstreamInfoModel
import jp.fout.rfp.android.sdk.video.VideoAdView
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("imageUrl")
fun ImageView.bindImageUrl(url: String) {
    val metrics = context.resources.displayMetrics
    val width = (120 * metrics.density).toInt()
    val height = (120 * metrics.density).toInt()
    val option = RequestOptions()
            .centerCrop()
            .override(width, height)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.icon)
    Glide.with(context)
            .load(url)
            .apply(option)
            .into(this)
}

@BindingAdapter("dateText")
fun TextView.dateText(date: Date) {
    text = SimpleDateFormat("MM/dd HH:mm", Locale.JAPAN).format(date)
}

@BindingAdapter("adModel")
fun VideoAdView.showAd(ad: RFPInstreamInfoModel) {
    processAd(ad)
}
