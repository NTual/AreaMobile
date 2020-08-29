package com.example.areamobile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONObject

class Settings : AppCompatActivity() {

    private var resultIntent = Intent()

    private lateinit var wrapper: APIwrapper

    private lateinit var _url: String
    private lateinit var _id: String
    private lateinit var accessToken: String

    private lateinit var _currentPassword: EditText
    private lateinit var _newPassword: EditText
    private lateinit var _newPassword2: EditText
    private lateinit var _updateButton: Button
    private lateinit var _email: String

    lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        _url = intent.getStringExtra("url")
        _id = intent.getStringExtra("id")
        accessToken = intent.getStringExtra("access_token")
        _currentPassword = findViewById(R.id.current_password)
        _newPassword = findViewById(R.id.new_password)
        _newPassword2 = findViewById(R.id.new_password2)
        _updateButton = findViewById(R.id.btn_update)
        wrapper = APIwrapper(_url)

        settings = applicationContext.getSharedPreferences("Login", 0)

        _updateButton.setOnClickListener { changeSettings() }
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

    fun changeSettings() {
        val progressDialog = ProgressDialog(
            this@Settings,
            R.style.AppTheme_Dark_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Changing settings...")
        progressDialog.show()

        android.os.Handler().postDelayed(
            {
                if (validate()) {
                    val rep = wrapper.updateUser(accessToken, _id, _email, _newPassword.text.toString())
                    if (rep.code() != 200) {
                        //Toast.makeText(baseContext, "Update user fail, message: ${rep?.message()}\nbody:${rep.body()?.string()}\ncode:${rep.code()}", Toast.LENGTH_LONG).show()
                        Log.d(
                            "yoyo",
                            "Update user fail, message: ${rep?.message()}\nbody:${rep.body()?.string()}\ncode:${rep.code()}"
                        )
                    } else {
                        val editor = settings.edit()
                        editor.putString("password", _newPassword.text.toString())
                        editor.apply()
                        Log.d(
                            "yoyo", "profile updated:${rep?.message()}\n" +
                                    "body:${rep.body()?.string()}\n" +
                                    "code:${rep.code()}"
                        )
                        Toast.makeText(baseContext, "Your profile as been updated", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
                progressDialog.dismiss()
            }, 3000
        )
    }

    fun validate(): Boolean {
        val rep = wrapper.getUser(accessToken, _id)
        val currentPassword = _currentPassword.text.toString()
        val newPassword = _newPassword.text.toString()
        val newPassword2 = _newPassword2.text.toString()
        var jsonobj: JSONObject

        if (rep.code() == 200) {
            val jsonString = rep.body()?.string()
            jsonobj = JSONObject(jsonString)
            _email = jsonobj.getJSONObject("user").getString("email")
        } else {
            Log.d("yoyo", rep.body()?.string())
            Toast.makeText(baseContext, "Get user failed: ${rep?.message()}", Toast.LENGTH_LONG).show()
            return false
        }
        if (currentPassword.isEmpty() || currentPassword != jsonobj.getJSONObject("user").getString("password")) {
            _currentPassword.error = "null or wrong current password"
            return false
        }
        if (newPassword.isEmpty() || newPassword2.isEmpty() || newPassword != newPassword2 || newPassword.length < 7 || newPassword.length > 20) {
            _newPassword.error =
                "passwords must not be null or be different, passwords need to have between 7 and 20 charactere"
            _newPassword2.error =
                "passwords must not be null or be different, passwords need to have between 7 and 20 charactere"
            return false
        }
        return true
    }
}
