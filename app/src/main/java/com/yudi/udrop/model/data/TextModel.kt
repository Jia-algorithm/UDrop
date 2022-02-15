package com.yudi.udrop.model.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.yudi.udrop.BR
import com.yudi.udrop.R

data class TextModel(
    val Title: String,
    val Writer: String,
    val Context: String,
    val _collected: Boolean
) : BaseObservable() {
    val aboutWriter: String
        get() = "        李白（701年—762年12月），字太白，号青莲居士，又号“谪仙人”，唐代伟大的浪漫主义诗人，被后人誉为“诗仙”，与杜甫并称为“李杜”，为了与另两位诗人李商隐与杜牧即“小李杜”区别，杜甫与李白又合称“大李杜”。"// TODO: get info

    @get:Bindable
    var collected: Boolean = _collected
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