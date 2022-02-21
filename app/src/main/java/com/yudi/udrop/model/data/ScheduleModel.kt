package com.yudi.udrop.model.data

data class ScheduleModel(val _completedNumber: Int, val _totalNumber: Int) {
    val completedNumber: String
        get() = _completedNumber.toString()
    val totalNumber: String
        get() = _totalNumber.toString()
    val process: String
        get() = "已学 $completedNumber/$totalNumber"
}
