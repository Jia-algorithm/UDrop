package com.yudi.udrop.data

import android.util.Log
import com.yudi.udrop.model.local.TextDetail
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
                val result = response.body?.string()
                if (result != "Failed" && JSONObject(result).getString("success") == "Yes")
                    completion(JSONObject(result).getInt("userId"))
                else
                    completion(-2)
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
                val result = response.body?.string()
                if (result != "Failed")
                    completion(JSONObject(result).getInt("userId"))
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
                val result = response.body?.string()
                if (result != "Failed")
                    with(JSONObject(result)) {
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
                val result = response.body?.string()
                if (result != "Failed")
                    with(JSONObject(result)) {
                        completion(
                            try {
                                getJSONArray("new_list")
                            } catch (e: Exception) {
                                JSONArray()
                            },
                            try {
                                getJSONArray("review_list")
                            } catch (e: Exception) {
                                JSONArray()
                            }
                        )
                    }
            }
        })
    }

    fun getTextDetail(title: String, completion: (TextDetail?) -> Unit) {
        val request = Request.Builder().url("$baseURL/passage/detail?title=$title").build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to get user schedule.")
                e.printStackTrace()
                completion(null)
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                if (result != "Failed")
                    with(JSONObject(result)) {
                        completion(
                            TextDetail(
                                title,
                                getString("author"),
                                "",
                                getString("content"),
                                getString("author_info")
                            )
                        )
                    }
            }
        })
    }

    fun setNewSchedule(userId: Int, newScheduleList: JSONArray, completion: (Boolean) -> Unit) {
        val params = "{\"user_id\":$userId,\"new_schedule\":$newScheduleList}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/study/new_schedule")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to update new schedule.")
                e.printStackTrace()
                completion(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body?.string() == "Success") completion(true) else completion(false)
            }
        })
    }

    fun setReviewSchedule(
        userId: Int,
        reviewScheduleList: JSONArray,
        completion: (Boolean) -> Unit
    ) {
        val params = "{\"user_id\":$userId,\"review_schedule\":$reviewScheduleList}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/study/review_schedule")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to update review schedule.")
                e.printStackTrace()
                completion(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body?.string() == "Success") completion(true) else completion(false)
            }
        })
    }

    fun getCollection(userId: Int, completion: (JSONArray) -> Unit) {
        val request =
            Request.Builder().url("$baseURL/user/collection?user_id=$userId").build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to get user collection.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                if (result != "Failed")
                    with(JSONObject(result)) {
                        completion(
                            try {
                                getJSONArray("collection_list")
                            } catch (e: Exception) {
                                JSONArray()
                            }
                        )
                    }
            }
        })
    }

    fun addCollection(userId: Int, title: String, completion: (Boolean) -> Unit) {
        val params = "{\"user_id\":$userId,\"title\":\"$title\"}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/user/collection")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to update review schedule.")
                e.printStackTrace()
                completion(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body?.string() == "Added") completion(true) else completion(false)
            }
        })
    }

    fun removeCollection(userId: Int, title: String, completion: (Boolean) -> Unit) {
        val params = "{\"user_id\":$userId,\"title\":\"$title\"}"
        val request =
            Request.Builder().delete(params.toRequestBody(JSON)).url("$baseURL/user/collection")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to update review schedule.")
                e.printStackTrace()
                completion(false)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body?.string() == "Removed") completion(true) else completion(false)
            }
        })
    }

    fun checkCollection(userId: Int, title: String, completion: (Boolean) -> Unit) {
        val request =
            Request.Builder().url("$baseURL/user/check_collection?user_id=$userId&title=$title")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to get user collection.")
                e.printStackTrace()
                completion(false)
            }

            override fun onResponse(call: Call, response: Response) {
                completion(response.body?.string() == "Yes")
            }
        })
    }

    fun getRecommendation(number: Int, completion: (JSONArray) -> Unit) {
        val request =
            Request.Builder().url("$baseURL/poems/random?number=$number")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to get user collection.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                if (result != "Failed")
                    with(JSONObject(result)) {
                        completion(
                            try {
                                getJSONArray("result_list")
                            } catch (e: Exception) {
                                JSONArray()
                            }
                        )
                    }
            }
        })
    }

    fun searchText(key: String, completion: (JSONArray) -> Unit) {
        val request =
            Request.Builder().url("$baseURL/poems/search?key=$key")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to get user collection.")
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                if (result != "Failed")
                    with(JSONObject(result)) {
                        completion(
                            try {
                                getJSONArray("result_list")
                            } catch (e: Exception) {
                                JSONArray()
                            }
                        )
                    }
            }
        })
    }

    fun communicate(userId: Int, text: String, completion: (Boolean, String) -> Unit) {
        val params = "{\"user_id\":$userId,\"text\":\"$text\"}"
        val request =
            Request.Builder().post(params.toRequestBody(JSON)).url("$baseURL/reply")
                .build()
        OkHttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "failed to update review schedule.")
                e.printStackTrace()
                completion(true, "啊哦，出了点小问题，先去别的地方看看吧")
            }

            override fun onResponse(call: Call, response: Response) {
                val result = response.body?.string()
                if (result != "Failed")
                    with(JSONObject(result)) {
                        completion(getBoolean("is_finished"), getString("response"))
                    }
                else
                    completion(true, "啊哦，出了点小问题，先去别的地方看看吧")
            }
        })
    }

    companion object {
        val JSON = String.format("application/json; charset=utf-8").toMediaType()
        const val baseURL = "http://121.199.77.139:5001"
        const val TAG = "OkHttp"
    }
}