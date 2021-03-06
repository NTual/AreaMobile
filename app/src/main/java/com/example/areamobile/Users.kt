package com.example.areamobile

import android.os.AsyncTask
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class Users(private val url: String, private val authToken: String) : AsyncTask<Void, Void, Response>() {
    override fun doInBackground(vararg p0: Void?): Response {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$url/users")
            .header("token", authToken)
            .get()
            .build()

        return client.newCall(request).execute()
    }
}