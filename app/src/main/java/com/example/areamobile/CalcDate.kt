package com.example.areamobile

import android.os.AsyncTask
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

class CalcDate(private val url: String, private val nbDays: String) : AsyncTask<Void, Void, Response>() {
    override fun doInBackground(vararg p0: Void?): Response? {

        try {

            val client = OkHttpClient()
            val request = Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url("$url/timer/future/$nbDays")
                .build()

            return client.newCall(request).execute()
        } catch (se: SocketTimeoutException) {
            Log.d("yoyo", "FAILED TO CONNECT")
        } catch (e: IOException) {
            // handle general IO error
        } catch (e: Exception) {
            // just in case you missed anything else
        }
        return null
    }
}