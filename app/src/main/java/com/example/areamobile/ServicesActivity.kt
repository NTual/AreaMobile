package com.example.areamobile

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.MenuItem


class ServicesActivity : AppCompatActivity() {

    private var resultIntent = Intent()

    private lateinit var _url: String
    private lateinit var _id: String
    private lateinit var _access_token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)

        _url = intent.getStringExtra("url")
        _id = intent.getStringExtra("id")
        _access_token = intent.getStringExtra("access_token")

        val timer = findViewById<CardView>(R.id.service_timer)
        timer.setOnClickListener { timerListener() }
    }

    fun timerListener() {
        val intent = Intent(this, TimerActivity::class.java)
        intent.putExtra("url", _url)
        intent.putExtra("id", _id)
        startActivity(intent)
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
