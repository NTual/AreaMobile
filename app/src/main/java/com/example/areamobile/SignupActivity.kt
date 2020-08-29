package com.example.areamobile

import android.R
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import butterknife.ButterKnife
import butterknife.BindView
import okhttp3.Response

class SignupActivity : AppCompatActivity() {

    @BindView(com.example.areamobile.R.id.input_email)
    lateinit var _emailText: EditText
    @BindView(com.example.areamobile.R.id.input_password)
    lateinit var _passwordText: EditText
    @BindView(com.example.areamobile.R.id.btn_signup)
    lateinit var _signupButton: Button
    @BindView(com.example.areamobile.R.id.link_login)
    lateinit var _loginLink: TextView
    @BindView(com.example.areamobile.R.id.signup_url)
    lateinit var _url: EditText

    var resultIntent = Intent()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.areamobile.R.layout.activity_signup)
        //ButterKnife.bind(this)
        _emailText = findViewById(com.example.areamobile.R.id.input_email)
        _passwordText = findViewById(com.example.areamobile.R.id.input_password)
        _signupButton = findViewById(com.example.areamobile.R.id.btn_signup)
        _loginLink = findViewById(com.example.areamobile.R.id.link_login)
        _url = findViewById(com.example.areamobile.R.id.signup_url)

        _signupButton.setOnClickListener { signup() }

        _loginLink.setOnClickListener {
            // Finish the registration screen and return to the Login activity
            finish()
        }
    }

    fun signup() {

        if (!validate()) {
            onSignupFailed(null)
            return
        }

        _signupButton.isEnabled = false

        val progressDialog = ProgressDialog(
            this@SignupActivity,
            com.example.areamobile.R.style.AppTheme_Dark_Dialog
        )
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        val email = _emailText.text.toString()
        val password = _passwordText.text.toString()
        val url = _url.text.toString()

        android.os.Handler().postDelayed(
            {
                Log.d("yoyo", "signingup!")
                var wrapper = APIwrapper(url)
                var rep = wrapper.register(email, password)
                if (rep == null) {
                    onSignupFailed("Cannot connect to server.")
                } else {
                    resultIntent.putExtra("url", url)
                    resultIntent.putExtra("email", email)
                    resultIntent.putExtra("password", password)

                    if (rep.code() != 200) {
                        onSignupFailed(rep.body()?.string())
                    } else
                        onSignupSuccess()
                }
                progressDialog.dismiss()
            }, 3000
        )
    }


    fun onSignupSuccess() {
        _signupButton.isEnabled = true
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    fun onSignupFailed(rep: String?) {
        Toast.makeText(baseContext, "Login failed: $rep", Toast.LENGTH_LONG).show()
        _signupButton.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val email = _emailText.text.toString()
        val password = _passwordText.text.toString()
        val url = _url.text.toString()

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.error = "enter a valid email address"
            valid = false
        } else {
            _emailText.error = null
        }

        if (password.isEmpty() || password.length < 7 || password.length > 20) {
            _passwordText.error = "between 7 and 20 alphanumeric characters"
            valid = false
        } else {
            _passwordText.error = null
        }

        if (url.isEmpty() || !android.util.Patterns.WEB_URL.matcher(url).matches()) {
            _url.error = "enter a valid api url"
            valid = false
        } else
            _url.error = null

        return valid
    }
}
