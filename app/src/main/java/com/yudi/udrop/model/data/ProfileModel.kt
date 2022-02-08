package com.yudi.udrop.model.data

import androidx.databinding.ObservableBoolean

data class ProfileModel(
    val Name: String,
    val Motto: String,
    val DaysNum: Int
) {
    val keepLearningDays: String
        get() = "已坚持学习 $DaysNum 天"
    var doEdit = ObservableBoolean(false)
}

