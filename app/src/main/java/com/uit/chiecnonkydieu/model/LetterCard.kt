package com.uit.chiecnonkydieu.model

data class LetterCard(
    val letter: String = "A",
    var isHidden: Boolean = letter != " "
)
