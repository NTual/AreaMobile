package com.example.areamobile

import android.app.Activity
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import okhttp3.Response
import java.util.*

class AReaActivity : AppCompatActivity() {

    private var resultIntent = Intent()

    private lateinit var submitBtn: Button
    private lateinit var _actionSpinner: Spinner
    private lateinit var _reactionSpinner: Spinner

    private lateinit var _url: String
    private lateinit var _id: String
    private lateinit var _access_token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_area)

        _url = intent.getStringExtra("url")
        _id = intent.getStringExtra("id")
        _access_token = intent.getStringExtra("access_token")

        _actionSpinner = findViewById(R.id.action)
        _reactionSpinner = findViewById(R.id.reaction)
        submitBtn = findViewById(R.id.btnSubmit)
        submitBtn.setOnClickListener { submit_listener() }

        _actionSpinner.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        _actionSpinner.setSelection(0, true)
        var v = _actionSpinner.selectedView
        (v as TextView).setTextColor(Color.WHITE)
        _reactionSpinner.background.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
        _reactionSpinner.setSelection(0, true)
        v = _reactionSpinner.selectedView
        (v as TextView).setTextColor(Color.WHITE)

        //Set the listener for when each option is clicked.
        _actionSpinner.onItemSelectedListener = colorizeSpinner
        _reactionSpinner.onItemSelectedListener = colorizeSpinner

    }

    object colorizeSpinner : OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            //Change the selected item's text color
            (view as TextView).setTextColor(Color.WHITE)
        }

        override fun onNothingSelected(parent: AdapterView<*>) {}
    }

    fun submit_listener() {
        Log.d("yoyo", "action:${_actionSpinner.selectedItem}\nreaction:${_reactionSpinner.selectedItem}")
        val wrapper = APIwrapper(_url)
        if (_actionSpinner.selectedItem == "Weather" || _actionSpinner.selectedItem == "Films") {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(this, OnTimeSetListener(function = { view, h, m ->

                view.background = ContextCompat.getDrawable(applicationContext, R.drawable.image_visibility)
                val rep = wrapper.setService(_id, h, m)
                if (rep.code() != 200) {
                    Log.d("yoyo", "error services: ${rep.body()?.string()}")
                } else {
                    Log.d("yoyo", "You are now subscribe to this service.")
                }

            }),hour,minute,true)

            tpd.show()
        }
        else {
            val rep = wrapper.setService(_id, 0, 0)
            if (rep.code() != 200) {
                Log.d("yoyo", "error services: ${rep.body()?.string()}")
            } else {
                Log.d("yoyo", "You are now subscribe to this service.")
            }
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
