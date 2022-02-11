package com.yudi.udrop.model.data

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import com.yudi.udrop.BR
import com.yudi.udrop.R

data class RegisterModel(var _password: String, var _confirm: String) : BaseObservable() {
    @get:Bindable
    var password: String = _password
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
            notifyPropertyChanged(BR.tipIcon)
            notifyPropertyChanged(BR.tipText)
            notifyPropertyChanged(BR.showConfirmWarning)
        }

    @get: Bindable
    var confirm: String = _confirm
        set(value) {
            field = value
            notifyPropertyChanged(BR.confirm)
            notifyPropertyChanged(BR.showConfirmWarning)
        }

    var showInputWarning = ObservableBoolean(false)

    @get: Bindable
    var tipIcon: Int = R.drawable.ic_success
        get() = if (password.length > 7 && password.contains("[a-zA-Z]".toRegex()) && password.contains(
                "[0-9]".toRegex()
            )
        ) R.drawable.ic_success else R.drawable.ic_warning
        set(value) {
            field = value
            notifyPropertyChanged(BR.tipIcon)
        }

    @get: Bindable
    var tipText: Int = R.string.none
        get() = if (password.length > 7 && password.contains("[a-zA-Z]".toRegex()) && password.contains(
                "[0-9]".toRegex()
            )
        ) R.string.none else R.string.password_tip
        set(value) {
            field = value
            notifyPropertyChanged(BR.tipText)
        }

    @get: Bindable
    var showConfirmWarning: Boolean = false
        get() = password != confirm
        set(value) {
            field = value
            notifyPropertyChanged(BR.showConfirmWarning)
        }
}
