package jp.fout.rfp.android.demo.kotlin.app.vo

enum class AdStatus {
    SUCCESS,
    ERROR,
}

data class AdResponse<out T>(val status: AdStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): AdResponse<T> {
            return AdResponse(AdStatus.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): AdResponse<T> {
            return AdResponse(AdStatus.ERROR, data, msg)
        }
    }
}
