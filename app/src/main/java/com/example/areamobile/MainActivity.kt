package com.example.areamobile

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : AppCompatActivity() {

    private val SECOND_ACTIVITY_REQUEST_CODE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    var url = data.getStringExtra("url")
                    var access_token = data.getStringExtra("access_token")
                    var id = data.getStringExtra("id")
                    val homeIntent = Intent(this, HomeActivity::class.java)
                    homeIntent.putExtra("url", url)
                    homeIntent.putExtra("access_token", access_token)
                    homeIntent.putExtra("id", id)
                    startActivity(homeIntent)
                }
            }
        }
    }
}
