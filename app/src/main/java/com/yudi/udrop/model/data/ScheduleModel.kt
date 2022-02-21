package com.yudi.udrop.model.data

data class ScheduleModel(val completedNumber: Int, val totalNumber: Int) {
    val process: String
        get() = "已学 $completedNumber/$totalNumber"
}
