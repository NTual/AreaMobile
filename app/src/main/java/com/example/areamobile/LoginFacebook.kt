package com.example.areamobile

import android.os.AsyncTask
import android.util.Log
import okhttp3.*
import org.json.JSONObject

class LoginFacebook(private val url: String, private val id: String) : AsyncTask<Void, Void, Response>() {
    override fun doInBackground(vararg p0: Void?): Response {
        val JSON = MediaType.parse("application/json; charset=utf-8")
        var json = JSONObject()

        json.put("id", id)

        val jsonString = json.toString()
        val body = RequestBody.create(JSON, jsonString)

        val client = OkHttpClient()
        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url("$url/facebook")
            .post(body)
            .build()

        return client.newCall(request).execute()
    }
}


/*
Log.d("yoyo", "connect to facebook: url:'$_url' access_token:'$_access_token' id:'$_id'")
        val wrapper = APIwrapper(_url)
        var rep = wrapper.loginFacebook(_id)
        Log.d("yoyo", "bite1")
        Log.d("yoyo", rep.body()?.string())

        var webView = findViewById<WebView>(R.id.webview)
        webView.loadData(rep.body()?.string(), "text/html; charset=UTF-8", null)
 */