package com.example.areamobile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_timer.*
import org.json.JSONObject

class TimerActivity : AppCompatActivity() {

    lateinit var _url: String
    lateinit var _id: String

    var monthMap = mapOf("1" to "January", "2" to "February", "3" to "Mars", "4" to "April", "5" to "May", "6" to "June",
        "7" to "July", "8" to "August", "9" to "September", "10" to "October", "11" to "November", "12" to "December")
    var dayMap = mapOf("Lundi" to "Monday", "Mardi" to "Tuesday", "Mercredi" to "Wednesday", "Jeudi" to "Thursday",
        "Vendredi" to "Friday", "Samedi" to "Saturday", "Dimanche" to "Sunday")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        _url = intent.getStringExtra("url")
        _id = intent.getStringExtra("id")

        val date_field = findViewById<TextView>(R.id.date_field)
        val time_field = findViewById<TextView>(R.id.time_field)
        val nb_days = findViewById<TextView>(R.id.nb_days)
        val date_calc_field = findViewById<TextView>(R.id.date_calc_field)

        val get_date = findViewById<Button>(R.id.get_day)
        val get_time = findViewById<Button>(R.id.get_time)
        val nb_days_field = findViewById<EditText>(R.id.nb_days_field)
        val calc_date = findViewById<Button>(R.id.calc_date)

        get_date.setOnClickListener { getDateListener() }
        get_time.setOnClickListener { getTimeListener() }
        calc_date.setOnClickListener { calcDateListener() }
    }

    fun getDateListener() {
        val wrapper = APIwrapper(_url)
        val rep = wrapper.getDate()

        if (rep.code() == 200) {
            var jsonobj = JSONObject(rep.body()?.string())
            date_field.text = dayMap[jsonobj.getString("weekDay")] + " " + jsonobj.getString("day") + " " + monthMap[jsonobj.getString("month")]
        }
    }

    fun getTimeListener() {
        val wrapper = APIwrapper(_url)
        val rep = wrapper.getTime()

        if (rep.code() == 200) {
            var jsonobj = JSONObject(rep.body()?.string())
            time_field.text = jsonobj.getString("hours") + ":" + jsonobj.getString("minutes")
        }
    }

    fun calcDateListener() {
        var wrapper = APIwrapper(_url)

        if (nb_days_field.text.isEmpty())
            nb_days_field.error = "Enter a number of day"
        else {
            val rep = wrapper.calcDate(nb_days_field.text.toString())
            nb_days.text = nb_days_field.text.toString()
            var jsonobj = JSONObject(rep.body()?.string())
            date_calc_field.text = dayMap[jsonobj.getString("weekDay")] + " " + jsonobj.getString("day") + " " + monthMap[jsonobj.getString("month")]
        }
    }
}
