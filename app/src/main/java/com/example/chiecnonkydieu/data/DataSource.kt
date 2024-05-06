package com.example.chiecnonkydieu.data

import com.example.chiecnonkydieu.model.QuestionAnswer
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


val database: DatabaseReference = Firebase.database.reference
const val REFERENCE_CAUHOI = "CAU HOI"
val questionAnswerList: MutableList<QuestionAnswer> = mutableListOf(
    QuestionAnswer(
        "Hà Nội",
        "HA NOI",
        "Thủ đô của Việt Nam nằm ở đâu?",
        "Hint1",
        "Hint2",
        "Hint3",
        "Thong tin"
    ),
    QuestionAnswer(
        "Hà Nội",
        "HA NOI",
        "Thủ đô của Việt Nam nằm ở đâu?",
        "Hint1",
        "Hint2",
        "Hint3",
        "Thong tin"
    ),

    )