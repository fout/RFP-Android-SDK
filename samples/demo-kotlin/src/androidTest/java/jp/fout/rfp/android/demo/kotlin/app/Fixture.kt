package jp.fout.rfp.android.demo.kotlin.app

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

object Fixture {
    fun asString(obj: Any, name: String): String {
        val loader = obj.javaClass.classLoader ?: throw RuntimeException("no class loader")
        val stream = loader.getResourceAsStream(name)
        val reader = BufferedReader(InputStreamReader(stream))
        val result = StringBuilder()
        try {
            reader.forEachLine { line ->
                result.append(line)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        return result.toString()
    }
}
