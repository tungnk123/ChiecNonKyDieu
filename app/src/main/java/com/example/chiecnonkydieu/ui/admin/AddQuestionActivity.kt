package com.example.chiecnonkydieu.ui.admin

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.chiecnonkydieu.R
import com.example.chiecnonkydieu.data.GameData
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddQuestionActivity : AppCompatActivity() {

    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }
    final val REFERENCE_CAUHOI: String = "CAU HOI"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_questions)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val edtDoiTuong : EditText = findViewById(R.id.edt_DapAnCuaTroChoi)
        val btnHintDapAn : ImageButton = findViewById(R.id.btnHintDapAn)

        val edtCauHoi : EditText = findViewById(R.id.edt_CauHoiVeDoiTuong)
        val btnCauHoi : ImageButton = findViewById(R.id.btnHintCauHoi)

        val edtDoiTuongInHoa : EditText = findViewById(R.id.edt_DapAnInHoa)
        val btnDapAnInHoa : ImageButton = findViewById(R.id.btnHintInHoa)

        val edtHint1 : EditText = findViewById(R.id.edt_Hint1)
        val btnHint1 : ImageButton = findViewById(R.id.btnHint1)

        val edtHint2 : EditText = findViewById(R.id.edt_Hint2)
        val btnHint2 : ImageButton = findViewById(R.id.btnHint2)

        val edtHint3 : EditText = findViewById(R.id.edt_Hint3)
        val btnHint3 : ImageButton = findViewById(R.id.btnHint3)

        val edtThongTinKienThuc : EditText = findViewById(R.id.edtThongTin)
        val btnThongTin : ImageButton = findViewById(R.id.btnThongTin)

        val btnTaoToanBo : Button = findViewById(R.id.btnTaoToanBo)
        val btnThem : Button = findViewById(R.id.btnThem)


        val generativeModel = GenerativeModel(
            // For text-only input, use the gemini-pro model
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyBnrK2OivzemEL84fWtE7OGLGtzMm4B2Dc"
        )

        val imageButton: ImageButton = findViewById(R.id.btn_back)
        imageButton.setOnClickListener {
            onBackPressed()
        }
        btnHintDapAn.setOnClickListener{
            runBlocking {
                val prompt = "Cho tôi một từ tiếng việt bất kì có tối đa 12 kí tự.Khuyến khích bạn sử dụng các từ gần gũi với văn học, lịch sử, địa danh, văn hóa, con người, âm nhạc Việt Nam. Không cần giải thích chỉ cần ghi từ đó ra. Các từ không viết liền nhau ví dụ Khỏe Khoắn"
                val response = generativeModel.generateContent(prompt)
                edtDoiTuong.setText(response.text)
            }
        }
        btnDapAnInHoa.setOnClickListener {
            edtDoiTuongInHoa.setText(chuyenChuoiThanhChuoiInHoaKhongDau(edtDoiTuong.text.toString()))
        }
        btnCauHoi.setOnClickListener {
            if(edtDoiTuong.text.length.equals(0)) {
                Toast.makeText(applicationContext, "Vui lòng điền đối tượng", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            runBlocking {
                val prompt = "Tôi đang làm trò chơi đoán từ, cho tôi một câu hỏi về từ để mọi người có thể đoán " + edtDoiTuong.text + ",chỉ cần ghi câu hỏi"
                val response = generativeModel.generateContent(prompt)
                edtCauHoi.setText(response.text)
            }
        }
        btnHint1.setOnClickListener {
            if(edtDoiTuong.text.length.equals(0)) {
                Toast.makeText(applicationContext, "Vui lòng điền đối tượng", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            runBlocking {
                val prompt = "Hãy cho tôi biết từ " + edtDoiTuong.text + " thuộc loại từ gì. Chỉ cần ghi loại từ"
                val response = generativeModel.generateContent(prompt)
                edtHint1.setText(response.text)
            }
        }
        btnHint2.setOnClickListener {
            if(edtDoiTuong.text.length.equals(0)) {
                Toast.makeText(applicationContext, "Vui lòng điền đối tượng", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            runBlocking {
                val prompt = "Tôi đang làm trò chơi đoán từ, cho tôi một gợi ý ngắn về từ để mọi người có thể đoán " + edtDoiTuong.text
                val response = generativeModel.generateContent(prompt)
                edtHint2.setText(response.text)
            }
        }
        btnHint3.setOnClickListener {
            if(edtDoiTuong.text.length.equals(0)) {
                Toast.makeText(applicationContext, "Vui lòng điền đối tượng", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            runBlocking {
                val prompt = "Tôi đang làm trò chơi đoán từ, cho tôi một gợi ý ngắn về từ để mọi người có thể đoán " + edtDoiTuong.text + " khác với gợi ý này: " + edtHint2.text
                val response = generativeModel.generateContent(prompt)
                edtHint3.setText(response.text)
            }
        }
        btnThongTin.setOnClickListener {
            if(edtDoiTuong.text.length.equals(0)) {
                Toast.makeText(applicationContext, "Vui lòng điền đối tượng", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            runBlocking {
                val prompt = "Cho tôi một số thông tin kiến thức về từ " + edtDoiTuong.text + ", chỉ cần ghi ngắn gọn không cần dài dòng, không cần format in đậm in nghiêng, chỉ cần font bình thường"
                val response = generativeModel.generateContent(prompt)
                edtThongTinKienThuc.setText(response.text)
            }
        }
        btnTaoToanBo.setOnClickListener{
            try{
                val progressDialog = ProgressDialog.show(this, "Vui lòng chờ", "Đang tạo...");
                lifecycleScope.launch {
                    val prompt = "Cho tôi một từ tiếng việt bất kì có tối đa 12 kí tự.Khuyến khích bạn sử dụng các từ gần gũi với văn học, lịch sử, địa danh Việt Nam. Không cần giải thích chỉ cần ghi từ đó ra"
                    val response = generativeModel.generateContent(prompt)
                    edtDoiTuong.setText(response.text)

                    val word = edtDoiTuong.text.toString()

                    // Convert word to uppercase without diacritics
                    edtDoiTuongInHoa.setText(chuyenChuoiThanhChuoiInHoaKhongDau(word))

                    // Generate question about the word
                    val questionPrompt = "Tôi đang làm trò chơi đoán từ, cho tôi một câu hỏi về từ để mọi người có thể đoán \"$word\"... Giới hạn dưới 30 chữ "
                    val questionResponse = generativeModel.generateContent(questionPrompt)
                    edtCauHoi.setText(questionResponse.text)

                    // Generate hint 1
                    val hint1Prompt = "Hãy cho tôi biết từ \"$word\" thuộc loại từ gì..."
                    val hint1Response = generativeModel.generateContent(hint1Prompt)
                    edtHint1.setText(hint1Response.text)

                    // Generate hint 2
                    val hint2Prompt = "Tôi đang làm trò chơi đoán từ, cho tôi một gợi ý ngắn về từ để mọi người có thể đoán \"$word\"..."
                    val hint2Response = generativeModel.generateContent(hint2Prompt)
                    edtHint2.setText(hint2Response.text)

                    // Generate hint 3
                    val hint3Prompt = "Tôi đang làm trò chơi đoán từ, cho tôi một gợi ý ngắn về từ để mọi người có thể đoán \"$word\" khác với gợi ý này: ${edtHint2.text}"
                    val hint3Response = generativeModel.generateContent(hint3Prompt)
                    edtHint3.setText(hint3Response.text)

                    // Generate knowledge about the word
                    val knowledgePrompt = "Cho tôi một số thông tin kiến thức về từ \"$word\", chỉ cần ghi ngắn gọn không cần dài dòng, không cần format in đậm in nghiêng, không cần dấu * để chỉ heading, chỉ cần font bình thường"
                    val knowledgeResponse = generativeModel.generateContent(knowledgePrompt)
                    edtThongTinKienThuc.setText(knowledgeResponse.text)
                }
                progressDialog.dismiss()
            }
            catch (ex : Exception){
                Toast.makeText(applicationContext, "Thao tác quá nhanh vui lòng thử lại ", Toast.LENGTH_LONG).show()
            }
        }


        btnThem.setOnClickListener{
            try {
                val BoCauHoi = Question()
                BoCauHoi.DoiTuong = edtDoiTuong.text.toString()
                BoCauHoi.CauHoi = edtCauHoi.text.toString()
                BoCauHoi.DoiTuongInHoa = edtDoiTuongInHoa.text.toString()
                BoCauHoi.Hint1 = edtHint1.text.toString()
                BoCauHoi.Hint2 = edtHint2.text.toString()
                BoCauHoi.Hint3 = edtHint3.text.toString()
                BoCauHoi.ThongTin = edtThongTinKienThuc.text.toString()

                database.child(REFERENCE_CAUHOI).push().setValue(BoCauHoi).addOnSuccessListener {
                    Toast.makeText(this, "Thêm câu hỏi thành công", Toast.LENGTH_LONG).show()
                    edtDoiTuong.text.clear()
                    edtCauHoi.text.clear()
                    edtDoiTuongInHoa.text.clear()
                    edtHint1.text.clear()
                    edtHint2.text.clear()
                    edtHint3.text.clear()
                    edtThongTinKienThuc.text.clear()
                }

            }
            catch(e : Exception) {
                Toast.makeText(applicationContext, "Vui lòng thử lại", Toast.LENGTH_LONG).show()
            }

        }

    }
    fun chuyenChuoiThanhChuoiInHoaKhongDau(chuoi: String): String {
        val vietnameseCharactersWithAccents = "àáảãạâầấẩẫậăằắẳẵặèéẻẽẹêềếểễệìíỉĩịòóỏõọôồốổỗộơờớởỡợùúủũụưừứửữựỳýỷỹỵđÀÁẢÃẠÂẦẤẨẪẬĂẰẮẲẴẶÈÉẺẼẸÊỀẾỂỄỆÌÍỈĨỊÒÓỎÕỌÔỒỐỔỖỘƠỜỚỞỠỢÙÚỦŨỤƯỪỨỬỮỰỲÝỶỸỴĐ"
        val vietnameseCharactersWithoutAccents = "aaaaaaaaaaaaaaaaaeeeeeeeeeeeiiiiiooooooooooooooooouuuuuuuuuuuyyyyydAAAAAAAAAAAAAAAAAEEEEEEEEEEEIIIIIOOOOOOOOOOOOOOOOOUUUUUUUUUUUYYYYYD"

        val stringBuilder = StringBuilder()
        for (char in chuoi) {
            val index = vietnameseCharactersWithAccents.indexOf(char)
            if (index != -1) {
                stringBuilder.append(vietnameseCharactersWithoutAccents[index])
            } else {
                stringBuilder.append(char)
            }
        }
        return stringBuilder.toString().toUpperCase()
    }
    data class Question(
        var DoiTuong : String = "",
        var DoiTuongInHoa : String = "",
        var CauHoi : String = "",
        var Hint1 : String = "",
        var Hint2 : String = "",
        var Hint3 : String = "",
        var ThongTin : String = ""
    )
}