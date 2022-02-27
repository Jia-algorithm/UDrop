package com.yudi.udrop.interfaces

interface ProgressInterface {
    fun checkDetail(title: String) {}
    fun addNewSchedule(title: String) {}
    fun checkProgress(buttonText: String) {}
    fun clickScheduleButton(buttonText: String) {}
}