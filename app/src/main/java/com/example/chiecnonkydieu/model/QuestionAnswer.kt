package com.example.chiecnonkydieu.model

data class QuestionAnswer(
    val question: String = "Thủ đô của Việt Nam nằm ở đâu?",
    val answer: String = "HA NOI",
    val answerCoDau: String = "Hà Nội",
    val hintList: MutableList<Hint> = mutableListOf()
)
