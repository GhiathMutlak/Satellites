package com.satellites.app.utils

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class Utilities {

    companion object{
        @JvmStatic
        fun streamToString(stream: InputStream): String? {
            val reader = InputStreamReader(stream)
            val bufferedReader = BufferedReader(reader)
            val result = StringBuilder()
            var line: String? = ""
            try {
                while (bufferedReader.readLine().also { line = it } != null) {
                    result.append(line)
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return result.toString()
        }
    }


}