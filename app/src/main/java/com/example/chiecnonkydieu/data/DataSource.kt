package com.example.chiecnonkydieu.data

import android.content.Context
import com.example.chiecnonkydieu.R
import com.example.chiecnonkydieu.model.Hint
import com.example.chiecnonkydieu.model.QuestionAnswer


val hintList:MutableList<Hint> = mutableListOf(
    Hint("Đây là hint 1"),
    Hint("Đây là hint 2"),
    Hint("Đây là hint 3")
)
val questionAnswerList: List<QuestionAnswer> = mutableListOf(
    QuestionAnswer("Thủ đô của Việt Nam nằm ở đâu?", "HA NOI", "Hà Nội", hintList),
    QuestionAnswer("Loại hình thể thao nào được yêu thích nhất ở Việt Nam?", "BONG DA", "Bóng Đá", hintList),
    QuestionAnswer("Bộ phim khoa học viễn tưởng nào là một trong những tác phẩm nổi tiếng nhất?", "STARWARS", "STARWARS", hintList),
    QuestionAnswer("Đâu là bộ phim có tựa đề \"Dặm xanh\"?", "THE GREEN MILES", "THE GREEN MILES", hintList),
)

