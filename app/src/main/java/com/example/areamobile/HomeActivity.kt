package com.example.areamobile

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.CardView
import android.util.Log
import android.widget.TextView

class HomeActivity : AppCompatActivity() {

    lateinit var _url: String
    lateinit var _access_token: String
    lateinit var _id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var test = findViewById<TextView>(R.id.yoyotest)
        _url = intent.getStringExtra("url")
        _access_token = intent.getStringExtra("access_token")
        _id = intent.getStringExtra("id")

        val accounts_button = findViewById<CardView>(R.id.manage_accounts)
        val logout_button = findViewById<CardView>(R.id.logout)
        val services_button = findViewById<CardView>(R.id.services)
        val profile_button = findViewById<CardView>(R.id.profile)
        val area_button = findViewById<CardView>(R.id.area)
        accounts_button.setOnClickListener { accounts_listener() }
        logout_button.setOnClickListener { logout_listener() }
        services_button.setOnClickListener { services_listener() }
        profile_button.setOnClickListener { profile_listener() }
        area_button.setOnClickListener { area_listener() }

        test.text = _url
    }

    override fun onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true)
    }

    fun accounts_listener() {
        val intent = Intent(this, AccountsActivity::class.java)
        intent.putExtra("url", _url)
        intent.putExtra("access_token", _access_token)
        intent.putExtra("id", _id)
        startActivity(intent)
    }

    fun logout_listener() {
        val settings = applicationContext.getSharedPreferences("Login", 0)
        val editor = settings.edit()
        editor.putBoolean("stay_connected", false)
        editor.apply()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun services_listener() {
        val intent = Intent(this, ServicesActivity::class.java)
        intent.putExtra("url", _url)
        intent.putExtra("id", _id)
        intent.putExtra("access_token", _access_token)
        startActivity(intent)
    }

    fun profile_listener() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("url", _url)
        intent.putExtra("access_token", _access_token)
        intent.putExtra("id", _id)
        startActivity(intent)
    }

    fun area_listener() {
        val intent = Intent(this, AReaActivity::class.java)
        intent.putExtra("url", _url)
        intent.putExtra("access_token", _access_token)
        intent.putExtra("id", _id)
        startActivity(intent)
    }

    companion object {
        private val TAG = "HomeActivity"
        private val REQUEST_ACCOUNTS = 0
    }
}
