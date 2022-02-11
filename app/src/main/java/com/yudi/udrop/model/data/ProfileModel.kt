package com.yudi.udrop.model.data

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

data class ProfileModel(
    var Name: String,
    val _motto: String,
    var DaysNum: Int
) {
    val keepLearningDays: String
        get() = "已坚持学习 $DaysNum 天"
    var doEdit = ObservableBoolean(false)
    var motto = ObservableField(_motto)
    var editMotto = ObservableField(_motto)
}

