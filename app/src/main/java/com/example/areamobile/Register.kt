package com.example.areamobile

import android.os.AsyncTask
import android.util.Log
import okhttp3.*
import org.json.JSONObject

class Register(private val url: String, private val email: String, private val passwd: String) : AsyncTask<Void, Void, Response>() {
    override fun doInBackground(vararg p0: Void?): Response {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        var json = JSONObject()

        json.put("email", email)
        json.put("password", passwd)

        val jsonString = json.toString()
        val body = RequestBody.create(JSON, jsonString)

        val client = OkHttpClient()
        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url("$url/register")
            .post(body)
            .build()

        return client.newCall(request).execute()
    }
}