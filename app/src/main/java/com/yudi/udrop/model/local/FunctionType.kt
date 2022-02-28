package com.yudi.udrop.model.local

enum class FunctionType(val typeId: String) {
    RECITE_BY_SENTENCE("recite_by_sentence"), RECITE_WHOLE("recite_whole"), LEARN_NEW("learn_new"), REVIEW(
        "review"
    ),
    GAME("game"), DEFAULT("default")
}