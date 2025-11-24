package com.example.android_tuan_2 // <-- THAY BẰNG TÊN PACKAGE CỦA BẠN

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar // Cần import Calendar

class RegisterActivity : AppCompatActivity() {

    // Tham chiếu đến các View
    private lateinit var editFirstName: EditText
    private lateinit var editLastName: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var labelGender: TextView
    private lateinit var editBirthday: EditText
    private lateinit var editAddress: EditText
    private lateinit var editEmail: EditText
    private lateinit var checkTerms: CheckBox
    private lateinit var btnSelectBirthday: Button
    private lateinit var btnRegister: Button

    // Màu nền
    private val defaultBackgroundColor = Color.parseColor("#EEEEEE")
    private val errorBackgroundColor = Color.parseColor("#FFCDD2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Liên kết các biến với ID trong XML
        editFirstName = findViewById(R.id.edit_first_name)
        editLastName = findViewById(R.id.edit_last_name)
        radioGroupGender = findViewById(R.id.radio_group_gender)
        labelGender = findViewById(R.id.label_gender)
        editBirthday = findViewById(R.id.edit_birthday)
        editAddress = findViewById(R.id.edit_address)
        editEmail = findViewById(R.id.edit_email)
        checkTerms = findViewById(R.id.check_terms)
        btnSelectBirthday = findViewById(R.id.btn_select_birthday)
        btnRegister = findViewById(R.id.btn_register)

        // ===== LOGIC MỚI CHO NÚT "SELECT" =====
        btnSelectBirthday.setOnClickListener {
            showDatePickerDialog()
        }

        // Xử lý khi nhấn nút Register
        btnRegister.setOnClickListener {
            if (validateForm()) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Hàm hiển thị hộp thoại chọn ngày (DatePickerDialog)
     */
    private fun showDatePickerDialog() {
        // Lấy ngày tháng hiện tại để làm giá trị mặc định
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Tạo DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                // Hàm này được gọi khi người dùng nhấn "OK"
                // (tháng bắt đầu từ 0, nên cần +1)
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                editBirthday.setText(selectedDate)
            },
            year,
            month,
            day
        )

        // Hiển thị hộp thoại
        datePickerDialog.show()
    }

    /**
     * Hàm kiểm tra toàn bộ form (Giữ nguyên)
     */
    private fun validateForm(): Boolean {
        var isValid = true
        isValid = validateEditText(editFirstName) && isValid
        isValid = validateEditText(editLastName) && isValid
        isValid = validateEditText(editBirthday) && isValid
        isValid = validateEditText(editAddress) && isValid
        isValid = validateEditText(editEmail) && isValid

        if (radioGroupGender.checkedRadioButtonId == -1) {
            isValid = false
            labelGender.setTextColor(Color.RED)
        } else {
            labelGender.setTextColor(Color.BLACK)
        }

        if (!checkTerms.isChecked) {
            isValid = false
            checkTerms.setTextColor(Color.RED)
        } else {
            checkTerms.setTextColor(Color.BLACK)
        }

        return isValid
    }

    /**
     * Hàm phụ trợ để kiểm tra 1 EditText (Giữ nguyên)
     */
    private fun validateEditText(editText: EditText): Boolean {
        if (editText.text.toString().trim().isEmpty()) {
            editText.setBackgroundColor(errorBackgroundColor)
            return false
        } else {
            // Chú ý: EditText của bạn có màu nền gốc là #EEEEEE
            editText.setBackgroundColor(defaultBackgroundColor)
            return true
        }
    }
}