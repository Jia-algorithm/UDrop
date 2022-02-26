package com.yudi.udrop.model.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.yudi.udrop.BR
import com.yudi.udrop.R

data class ProgressModel(val title: String, val writer: String, val content: String, val _finished: Int) :
    BaseObservable() {
    @get:Bindable
    var finished: Int = _finished
        set(value) {
            field = value
            notifyPropertyChanged(BR.finished)
            notifyPropertyChanged(BR.statusResId)
            notifyPropertyChanged(BR.statusIcon)
            notifyPropertyChanged(BR.statusColor)
        }

    @get:Bindable
    var statusResId: Int = R.string.finished
        get() = if (finished == 1) R.string.finished else R.string.unfinished
        set(value) {
            field = value
            notifyPropertyChanged(BR.statusResId)
        }

    @get:Bindable
    var statusIcon: Int = R.drawable.ic_finished
        get() = if (finished == 1) R.drawable.ic_finished else R.drawable.ic_unfinished
        set(value) {
            field = value
            notifyPropertyChanged(BR.statusIcon)
        }

    @get:Bindable
    var statusColor: Int = R.color.udrop_text_2
        get() = if (finished == 1) R.color.udrop_text_2 else R.color.udrop_text_0
        set(value) {
            field = value
            notifyPropertyChanged(BR.statusColor)
        }
}
