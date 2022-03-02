package com.yudi.udrop.model.local

data class TextDetail(
    val title: String,
    val _writer: String,
    val dynasty: String,
    val content: String,
    val writerInfo: String
) {
    val writer: String
        get() = "$dynasty · $_writer"
}