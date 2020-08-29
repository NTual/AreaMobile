package com.example.areamobile

import android.os.AsyncTask.execute
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject


class APIwrapper(private val url: String) {


    fun login(email : String, passwd : String) : Response?
    {
        var log = Login(url, email, passwd)
        return log.execute().get()
    }

    fun register(email: String, passwd: String) : Response
    {
       var reg = Register(url, email, passwd)
        return reg.execute().get()
    }

    fun users(authToken: String) : Response
    {
        var listUsers = Users(url, authToken)
        return listUsers.execute().get()
    }

    fun getUser(authToken: String, userId: String) : Response
    {
        var user = GetUser(url, authToken, userId)
        return user.execute().get()
    }

    fun deleteUser(authToken: String, userId: String) : Response
    {
        var user = DeleteUser(url, authToken, userId)
        return user.execute().get()
    }

    fun updateUser(authToken: String, userId: String, roles: String, passwd: String) : Response
    {
        var user = UpdateUser(url, authToken, userId, passwd, roles)
        return user.execute().get()
    }

    fun loginFacebook(userId: String) : Response
    {
        var log = LoginFacebook(url, userId)
        return log.execute().get()
    }

    fun getDate(): Response
    {
        val date = GetDate(url)
        return date.execute().get()
    }

    fun getTime(): Response
    {
        val time = GetTime(url)
        return time.execute().get()
    }

    fun calcDate(nbDays: String): Response
    {
        val time = CalcDate(url, nbDays)
        return time.execute().get()
    }

    fun setService(id: String, hours: Int, minutes: Int): Response
    {
        val service = Services(url, id, hours, minutes)
        return service.execute().get()
    }
}