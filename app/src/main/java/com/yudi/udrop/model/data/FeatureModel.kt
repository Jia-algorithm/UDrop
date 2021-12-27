package com.yudi.udrop.model.data

import com.yudi.udrop.R

data class FeatureModel(val title: Int) {
    val iconResId: Int
        get() = when (title) {
            R.string.my_collection -> R.drawable.ic_home_collection
            R.string.my_errorbook -> R.drawable.ic_home_errorbook
            R.string.game_center -> R.drawable.ic_home_game
            R.string.about_us -> R.drawable.ic_about_us
            R.string.feedback -> R.drawable.ic_feedback
            else -> R.drawable.ic_help
        }
}