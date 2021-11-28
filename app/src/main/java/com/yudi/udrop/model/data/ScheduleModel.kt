package com.yudi.udrop.model.data

data class ScheduleModel(val completedNumber: String, val totalNumber: String){
    val process:String
    get() = "已学 "+completedNumber+"/"+totalNumber
}
