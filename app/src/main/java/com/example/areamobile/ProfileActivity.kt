package com.example.areamobile

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject
import kotlin.system.exitProcess

class ProfileActivity : AppCompatActivity() {

    lateinit var _settingsButton : Button
    lateinit var _deleteButton : Button

    lateinit var settings: SharedPreferences

    var resultIntent = Intent()
    lateinit var _url: String
    lateinit var _id: String
    lateinit var accessToken: String
    lateinit var emailView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        _settingsButton = findViewById(R.id.buttonSettings)
        _deleteButton = findViewById(R.id.buttonDelete)
        emailView = findViewById(R.id.emailView)

        settings = applicationContext.getSharedPreferences("Login", 0)

        _url = intent.getStringExtra("url")
        _id = intent.getStringExtra("id")
        accessToken = intent.getStringExtra("access_token")

        _settingsButton.setOnClickListener{
            var intent = Intent(this, Settings::class.java)
            intent.putExtra("url", _url)
            intent.putExtra("id", _id)
            intent.putExtra("access_token", accessToken)
            startActivity(intent)
        }
        _deleteButton.setOnClickListener{
            val wrapper = APIwrapper(_url)
            var rep = wrapper.deleteUser(accessToken, _id)

            if (rep.code() == 200) {
                Toast.makeText(baseContext, "Your profile has been deleted", Toast.LENGTH_LONG).show()
                val editor = settings.edit()
                editor.putBoolean("stay_connected", false)
                editor.apply()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(baseContext, "unable to delete your account: ${rep.body()?.string()}", Toast.LENGTH_LONG).show()
            }
        }

        val wrapper = APIwrapper(_url)
        val rep = wrapper.getUser(accessToken, _id)

        if (rep.code() == 200) {
            val jsonString = rep.body()?.string()
            Log.d("yoyo", jsonString)
            var jsonobj = JSONObject(jsonString)
            emailView.text = jsonobj.getJSONObject("user").getString("email")
        }
        else {
            Log.d("yoyo", rep.body()?.string())
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            resultIntent.putExtra("url", _url)
            resultIntent.putExtra("id", _id)
            resultIntent.putExtra("access_token", accessToken)
            setResult(Activity.RESULT_OK, resultIntent)
            super.onBackPressed()
            return true
        }
        return false
    }
}
