package com.yudi.udrop.model.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.yudi.udrop.BR
import com.yudi.udrop.R

data class TextModel(
    val Title: String,
    val writer: String,
    val Context: String,
    val WriterInfo: String
) : BaseObservable() {

    @get:Bindable
    var collected: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.collected)
            notifyPropertyChanged(BR.collectionIcon)
            notifyPropertyChanged(BR.collectionText)
        }

    @get:Bindable
    var collectionIcon: Int = R.drawable.ic_home_collection
        get() = if (collected) R.drawable.ic_collection else R.drawable.ic_home_collection
        set(value) {
            field = value
            notifyPropertyChanged(BR.collectionIcon)
        }

    @get:Bindable
    var collectionText: Int = R.string.collect
        get() = if (collected) R.string.collected else R.string.collect
        set(value) {
            field = value
            notifyPropertyChanged(BR.collectionText)
        }
}