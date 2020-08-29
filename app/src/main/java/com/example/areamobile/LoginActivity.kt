package com.example.areamobile

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import android.content.Intent

import butterknife.BindView
import okhttp3.Response
import org.json.JSONObject
import android.content.SharedPreferences
import android.widget.*


class LoginActivity : AppCompatActivity() {

    @BindView(com.example.areamobile.R.id.input_email)
    lateinit var _emailText: EditText
    @BindView(com.example.areamobile.R.id.input_password)
    lateinit var _passwordText: EditText
    @BindView(com.example.areamobile.R.id.btn_login)
    lateinit var _loginButton: Button
    @BindView(com.example.areamobile.R.id.link_signup)
    lateinit var _signupLink: TextView
    @BindView(com.example.areamobile.R.id.input_url)
    lateinit var _urlText: EditText
    @BindView(com.example.areamobile.R.id.stay_connected)
    lateinit var _stayConnected: CheckBox

    lateinit var settings: SharedPreferences

    var resultIntent = Intent()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.areamobile.R.layout.activity_login)
        _emailText = findViewById(com.example.areamobile.R.id.input_email)
        _passwordText = findViewById(com.example.areamobile.R.id.input_password)
        _loginButton = findViewById(com.example.areamobile.R.id.btn_login)
        _signupLink = findViewById(com.example.areamobile.R.id.link_signup)
        _urlText = findViewById(com.example.areamobile.R.id.input_url)
        _stayConnected = findViewById(com.example.areamobile.R.id.stay_connected)

        settings = applicationContext.getSharedPreferences("Login", 0)

        var stay_connected = settings.getBoolean("stay_connected", false)
        if (stay_connected) {
            var url = settings.getString("url", "NaN")
            var email = settings.getString("email", "NaN")
            var password = settings.getString("password", "NaN")

            _urlText.setText(url)
            _emailText.setText(email)
            _passwordText.setText(password)
            _stayConnected.setChecked(true)

            login()
        }

        _loginButton.setOnClickListener { login() }

        _signupLink.setOnClickListener {
            // Start the Signup activity
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
        }
    }

    fun login() {
        Log.d(TAG, "Login")

        if (!validate()) {
            onLoginFailed(null)
            return
        }

        _loginButton.isEnabled = false

        val progressDialog = ProgressDialog(
            this@LoginActivity,
            com.example.areamobile.R.style.AppTheme_Dark_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Authenticating...")
        progressDialog.show()

        val email = _emailText.text.toString()
        val password = _passwordText.text.toString()
        val url = _urlText.text.toString()

        val editor = settings.edit()
        if (_stayConnected.isChecked) {
            editor.putBoolean("stay_connected", true)
            editor.putString("email", email)
            editor.putString("password", password)
            editor.putString("url", url)
        } else
            editor.putBoolean("stay_connected", false)
        editor.apply()

        android.os.Handler().postDelayed(
            {
                // On complete call either onLoginSuccess or onLoginFailed
                //connection via our api
                var wrapper = APIwrapper(url)
                var rep = wrapper.login(email, password)
                if (rep != null) {
                    var jsonobj = JSONObject(rep?.body()?.string())

                    if (rep?.code() != 200)
                        onLoginFailed(jsonobj.toString())
                    else {
                        resultIntent.putExtra("url", url)
                        resultIntent.putExtra("access_token", jsonobj.getString("access_token"))
                        resultIntent.putExtra("id", jsonobj.getString("id"))
                        onLoginSuccess()
                    }
                }
                else {
                    _stayConnected.setChecked(false)
                    _urlText.error = "Can't connect to this url"
                    val editor = settings.edit()
                    editor.putBoolean("stay_connected", false)
                    editor.apply()
                    _loginButton.isEnabled = true
                }
                progressDialog.dismiss()
            }, 3000
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val email = data.getStringExtra("email")
                    val url = data.getStringExtra("url")
                    val password = data.getStringExtra("password")
                    var wrapper = APIwrapper(url)
                    val rep = wrapper.login(email, password)

                    if (rep?.code() == 200) {
                        var jsonobj = JSONObject(rep.body()?.string())
                        resultIntent.putExtra("url", url)
                        resultIntent.putExtra("access_token", jsonobj.getString("access_token"))
                        resultIntent.putExtra("id", jsonobj.getString("id"))
                        setResult(Activity.RESULT_OK, resultIntent)
                        this.finish()
                    } else
                        onLoginFailed(rep?.body()?.string())
                }
            }
        }
    }

    override fun onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true)
    }

    fun onLoginSuccess() {
        _loginButton.isEnabled = true
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    fun onLoginFailed(rep: String?) {
        Toast.makeText(baseContext, "Login failed: $rep", Toast.LENGTH_LONG).show()

        var editor = settings.edit()
        editor.putBoolean("stay_connected", false)
        _loginButton.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val email = _emailText.text.toString()
        val password = _passwordText.text.toString()
        val url = _urlText.text.toString()

        if (email.isEmpty()) {
            _emailText.error = "Email cannot be empty"
            valid = false
        } else
            _emailText.error = null
        if (password.isEmpty() || password.length < 7 || password.length > 20) {
            _passwordText.error = "between 7 and 20 characters"
            valid = false
        } else {
            _passwordText.error = null
        }

        if (url.isEmpty() || !android.util.Patterns.WEB_URL.matcher(url).matches()) {
            _urlText.error = "enter a valid api url"
            valid = false
        } else
            _urlText.error = null

        return valid
    }

    companion object {
        private val TAG = "LoginActivity"
        private val REQUEST_SIGNUP = 0
    }
}