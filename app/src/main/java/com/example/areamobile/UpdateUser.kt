package com.example.areamobile

import android.os.AsyncTask
import android.util.Log
import okhttp3.*
import org.json.JSONObject

class UpdateUser(
    private val url: String,
    private val authToken: String,
    private val userId: String,
    private val passwd: String,
    private val email: String
) : AsyncTask<Void, Void, Response>() {
    override fun doInBackground(vararg p0: Void?): Response {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        var user = JSONObject()
        var requestJson = JSONObject()

        user.put("email", email)
        user.put("password", passwd)
        user.put("id", userId)
        requestJson.put("user", user)

        val jsonString = requestJson.toString()
        Log.d("yoyo", "json string update_user:$jsonString")
        val body = RequestBody.create(JSON, jsonString)

        val client = OkHttpClient()
        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url("$url/users/$userId")
            .post(body)
            .build()

        return client.newCall(request).execute()
    }
}