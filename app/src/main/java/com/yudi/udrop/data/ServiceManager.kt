package com.yudi.udrop.data

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class ServiceManager {
    fun register(name: String, password: String) {
        val params = "{[\"name\":\"$name\",\"password\":\"$password\"]}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/user/register").build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to register.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.message)
                println(response.body?.string())
            }
        })
    }

    fun login(name: String, password: String) {
        val params = "{\"name\":\"$name\",\"password\":\"$password\"}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/user/login").build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to login.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.message)
                println(response.body?.string())
            }
        })
    }

    fun getUserInfo(userId: Int) {
        val params = "{\"user_id\":$userId}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/user/basic_info")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to get user info.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.message)
                println(response.body?.string())
            }
        })
    }

    fun changeUserInfo(userId: Int, name: String, userMotto: String) {
        val params = "{\"user_id\":$userId,\"name\":\"$name\",\"user_motto\":\"$userMotto\"}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/user/basic_info")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to change user info.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.message)
                println(response.body?.string())
            }
        })
    }

    companion object {
        val JSON = String.format("application/json; charset=utf-8").toMediaType()
        const val baseURL = "http://121.199.77.139:5001"
        const val TAG = "OkHttp"
    }
}