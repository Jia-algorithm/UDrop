package com.yudi.udrop.model.local

enum class FunctionType(val typeId: String) {
    RECITE_BY_SENTENCE("recite_by_sentence"), RECITE_WHOLE("recite_whole"), LEARN_NEW("learn_new"), REVIEW(
        "review"
    ),
    GAME("game"), DEFAULT("default");

    companion object {
        fun typeFromId(id: String): FunctionType = when (id) {
            "recite_by_sentence" -> RECITE_BY_SENTENCE
            "recite_whole" -> RECITE_WHOLE
            "learn_new" -> LEARN_NEW
            "review" -> REVIEW
            "game" -> GAME
            else -> DEFAULT
        }
    }
}