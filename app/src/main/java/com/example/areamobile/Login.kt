package com.example.areamobile

import android.os.AsyncTask
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException

class Login(private val url: String, private val email: String, private val passwd: String) : AsyncTask<Void, Void, Response>() {
    override fun doInBackground(vararg p0: Void?): Response? {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        var json = JSONObject()

        try {
            json.put("email", email)
            json.put("password", passwd)

            val jsonString = json.toString()
            val body = RequestBody.create(JSON, jsonString)

            val client = OkHttpClient()
            val request = Request.Builder()
                .addHeader("Content-Type", "application/json")
                .url("$url/login")
                .post(body)
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