package jp.fout.rfp.android.demo.kotlin.app.util

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner
import jp.fout.rfp.android.demo.kotlin.app.TestApp

@Suppress("unused")
class DemoKotlinTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}