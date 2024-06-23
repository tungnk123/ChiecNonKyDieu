package com.uit.chiecnonkydieu.data

import com.uit.chiecnonkydieu.model.QuestionAnswer
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
        "Danh từ riêng",
        "Tọa lạc bên bờ sông Hồng",
        "Là trung tâm chính trị, văn hóa và giáo dục quan trọng của Việt Nam ",
        "Thong tin"
    ),
    QuestionAnswer(
        "Hà Nội",
        "HA NOI",
        "Thủ đô của Việt Nam nằm ở đâu?",
        "Danh từ riêng",
        "Tọa lạc bên bờ sông Hồng",
        "Là trung tâm chính trị, văn hóa và giáo dục quan trọng của Việt Nam ",
        "Thong tin"
    ),
    )