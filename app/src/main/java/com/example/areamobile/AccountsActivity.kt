package com.example.areamobile

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import butterknife.BindView
import okhttp3.HttpUrl
import android.content.ActivityNotFoundException
import android.net.Uri
import android.view.MenuItem


class AccountsActivity : AppCompatActivity() {

    @BindView(com.example.areamobile.R.id.connect_facebook)
    private lateinit var _connectFacebook: CardView
    private lateinit var _connectInsta: CardView

    private var resultIntent = Intent()

    private lateinit var _url: String
    private lateinit var _access_token: String
    private lateinit var _id: String

    inner class MyWebViewClient() : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            val regexpr = Regex("#(.*)")
            if (url != null) {
                val args: List<String>? = regexpr.find(url)?.value?.split("&")
                args?.forEach {
                    if (it.contains("access_token")) {
                    }
                }
            }
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        _connectFacebook = findViewById(R.id.connect_facebook)
        _connectFacebook.setOnClickListener { connect_to_facebook() }
        _connectInsta = findViewById(R.id.connect_insta)
        _connectInsta.setOnClickListener { connect_insta() }

        _url = intent.getStringExtra("url")
        _access_token = intent.getStringExtra("access_token")
        _id = intent.getStringExtra("id")
    }

    fun connect_to_facebook() {
        /*Log.d("yoyo", "connect to facebook: url:'$_url' access_token:'$_access_token' id:'$_id'")
        val postData = "id=" + URLEncoder.encode(_id, "utf-8")
        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.webViewClient = MyWebViewClient()
        myWebView.postUrl("$_url/facebook", postData.toByteArray()) */

        val wrapper = APIwrapper(_url)
        var rep = wrapper.loginFacebook(_id)
        var repUrl = rep.body()?.string()
        Log.d("yoyo", "bite1")
        Log.d("yoyo", repUrl)
        Log.d("yoyo", "bite2")


        val myWebView: WebView = findViewById(R.id.webview)
         myWebView.webViewClient = MyWebViewClient()
         myWebView.settings.javaScriptEnabled = true
         myWebView.loadUrl(repUrl)

       /* val urlString = repUrl
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.android.chrome")
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null)
            startActivity(intent)
        } */

    }

    fun connect_insta() {
        val urlBuilder = HttpUrl.parse("$_url/insta/$_id")!!.newBuilder()
            .build()

        Log.d("yoyo", urlBuilder.toString())
        /* val myWebView: WebView = findViewById(R.id.webview)
         myWebView.webViewClient = MyWebViewClient()
         myWebView.settings.javaScriptEnabled = true
         myWebView.loadUrl(urlBuilder.toString())*/

        val urlString = urlBuilder.toString()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.android.chrome")
        try {
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent.setPackage(null)
            startActivity(intent)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home) {
            resultIntent.putExtra("url", _url)
            resultIntent.putExtra("id", _id)
            resultIntent.putExtra("access_token", _access_token)
            setResult(Activity.RESULT_OK, resultIntent)
            super.onBackPressed()
            return true
        }
        return false
    }
}