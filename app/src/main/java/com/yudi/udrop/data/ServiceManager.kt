package com.yudi.udrop.data

import android.util.Log
import com.yudi.udrop.model.local.UserModel
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class ServiceManager {
    fun register(name: String, password: String, completion: (Int) -> Unit) {
        val params = "{\"name\":\"$name\",\"password\":\"$password\"}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/user/register").build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to register.")
                e.printStackTrace()
                completion(-2)
            }

            override fun onResponse(call: Call, response: Response) {
                if (JSONObject(response.body?.string()).getInt("user_id") == 0)
                    completion(JSONObject(response.body?.string()).getInt("user_id"))
                else
                    completion(-1)
            }
        })
    }

    fun login(name: String, password: String, completion: (Int) -> Unit) {
        val params = "{\"name\":\"$name\",\"password\":\"$password\"}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/user/login").build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to login.")
                e.printStackTrace()
                completion(-2)
            }

            override fun onResponse(call: Call, response: Response) {
                completion(JSONObject(response.body?.string()).getInt("userId"))
            }
        })
    }

    fun getUserInfo(userId: Int, completion: (UserModel) -> Unit) {
        val request =
            Request.Builder().url("$baseURL/user/basic_info?user_id=$userId").build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to get user info.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                with(JSONObject(response.body?.string())) {
                    completion(
                        UserModel(
                            userId,
                            getString("user_name"),
                            if (getString("user_motto") == "null") "" else getString("user_motto"),
                            getInt("learned_days")
                        )
                    )
                }
            }
        })
    }

    fun changeUserInfo(
        userId: Int,
        name: String,
        userMotto: String,
        completion: (Boolean) -> Unit
    ) {
        val params = "{\"user_id\":$userId,\"name\":\"$name\",\"user_motto\":\"$userMotto\"}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/user/basic_info")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to change user info.")
                e.printStackTrace()
                completion(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body?.string() == "Success") completion(true) else completion(false)
            }
        })
    }

    fun getSchedule(userId: Int, completion: (JSONArray, JSONArray) -> Unit) {
        val request =
            Request.Builder().url("$baseURL/study/schedule?user_id=$userId").build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to get user schedule.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                with(JSONObject(response.body?.string())) {
                    completion(getJSONArray("new_list"), getJSONArray("review_list"))
                }
            }
        })
    }

    companion object {
        val JSON = String.format("application/json; charset=utf-8").toMediaType()
        const val baseURL = "http://121.199.77.139:5001"
        const val TAG = "OkHttp"
    }
}