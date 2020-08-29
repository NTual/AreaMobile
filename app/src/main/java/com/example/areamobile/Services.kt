package com.example.areamobile

import android.os.AsyncTask
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

class Services(private val url: String, private val id: String, private val hours: Int, private val minutes: Int) : AsyncTask<Void, Void, Response>() {
    override fun doInBackground(vararg p0: Void?): Response? {
        try {
            val JSON = MediaType.parse("application/json; charset=utf-8")
            var json = JSONObject()

            json.put("hours", hours)
            json.put("minutes", minutes)

            val jsonString = json.toString()
            val body = RequestBody.create(JSON, jsonString)

            val client = OkHttpClient()
            val request = Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url("$url/modules/$id/mail_meteo")
                .post(body)
                .build()

            return client.newCall(request).execute()
        } catch (se: SocketTimeoutException) {
            Log.d("yoyo", "FAILED TO CONNECT")
        } catch (e: IOException) {
        } catch (e: Exception) {
        }
        return null
    }
}