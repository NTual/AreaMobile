package com.example.areamobile

import android.os.AsyncTask
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class DeleteUser(private val url: String, private val authToken: String, private val userId: String) : AsyncTask<Void, Void, Response>() {
    override fun doInBackground(vararg p0: Void?): Response {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$url/users/$userId")
            .header("token", authToken)
            .delete()
            .build()

        return client.newCall(request).execute()
    }
}