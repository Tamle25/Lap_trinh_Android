package com.example.android_tuan_2 // <-- THAY BẰNG TÊN PACKAGE CỦA BẠN

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.ArithmeticException

class CalculatorActivity : AppCompatActivity() {

    // Biến lưu trữ trạng thái
    private var currentNumber: String = "0"
    private var firstOperand: Long? = null
    private var pendingOperation: String? = null
    private var isNewNumber: Boolean = true

    // Tham chiếu đến các View
    private lateinit var textDisplay: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Liên kết Activity này với file layout của nó
        setContentView(R.layout.activity_calculator)

        textDisplay = findViewById(R.id.text_display)
        updateDisplay()

        // Thiết lập listener cho tất cả các nút
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        // Lấy ID của tất cả các nút
        val buttonIds = listOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
            R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide,
            R.id.btn_equals, R.id.btn_c, R.id.btn_ce, R.id.btn_bs
        )

        // Gán listener chung
        buttonIds.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onButtonClick(it) }
        }

        // Nút +/- (plusminus) không có trong yêu cầu logic nên tôi bỏ qua
        // Bạn có thể tự thêm nếu muốn
    }

    private fun onButtonClick(view: View) {
        when (view.id) {
            // Các nút số
            R.id.btn_0 -> onDigitClick("0")
            R.id.btn_1 -> onDigitClick("1")
            R.id.btn_2 -> onDigitClick("2")
            R.id.btn_3 -> onDigitClick("3")
            R.id.btn_4 -> onDigitClick("4")
            R.id.btn_5 -> onDigitClick("5")
            R.id.btn_6 -> onDigitClick("6")
            R.id.btn_7 -> onDigitClick("7")
            R.id.btn_8 -> onDigitClick("8")
            R.id.btn_9 -> onDigitClick("9")

            // Các nút phép toán
            R.id.btn_add -> onOperationClick("+")
            R.id.btn_subtract -> onOperationClick("-")
            R.id.btn_multiply -> onOperationClick("x")
            R.id.btn_divide -> onOperationClick("/")

            // Nút bằng
            R.id.btn_equals -> onEqualsClick()

            // Các nút xóa
            R.id.btn_bs -> onBackspaceClick()
            R.id.btn_ce -> onClearEntryClick()
            R.id.btn_c -> onClearClick()
        }
    }

    private fun onDigitClick(digit: String) {
        if (isNewNumber) {
            currentNumber = if (digit == "0") "0" else digit
            isNewNumber = false
        } else {
            // Nếu đang là "0" thì thay thế, không thì nối chuỗi
            currentNumber = if (currentNumber == "0") digit else currentNumber + digit
        }
        updateDisplay()
    }

    private fun onOperationClick(operation: String) {
        // Nếu đã có phép tính đang chờ, thực hiện nó trước
        if (!isNewNumber && firstOperand != null && pendingOperation != null) {
            performOperation()
        }

        // Lưu lại phép tính và toán hạng
        firstOperand = currentNumber.toLongOrNull()
        pendingOperation = operation
        isNewNumber = true
    }

    private fun onEqualsClick() {
        if (firstOperand != null && pendingOperation != null) {
            performOperation()
            // Đặt lại trạng thái sau khi nhấn bằng
            firstOperand = null
            pendingOperation = null
        }
        isNewNumber = true
    }

    private fun performOperation() {
        val secondOperand = currentNumber.toLongOrNull()
        if (firstOperand == null || secondOperand == null) return

        try {
            val result = when (pendingOperation) {
                "+" -> firstOperand!! + secondOperand
                "-" -> firstOperand!! - secondOperand
                "x" -> firstOperand!! * secondOperand
                "/" -> {
                    if (secondOperand == 0L) {
                        throw ArithmeticException("Chia cho 0")
                    }
                    firstOperand!! / secondOperand
                }
                else -> return
            }
            currentNumber = result.toString()
        } catch (e: ArithmeticException) {
            currentNumber = "Error"
        }

        updateDisplay()
    }

    // Yêu cầu: Nút BS: Xóa chữ số hàng đơn vị
    private fun onBackspaceClick() {
        if (currentNumber.length > 1) {
            currentNumber = currentNumber.dropLast(1)
        } else {
            currentNumber = "0"
        }
        updateDisplay()
    }

    // Yêu cầu: Nút CE: Xóa giá trị toán hạng hiện tại về 0
    private fun onClearEntryClick() {
        currentNumber = "0"
        isNewNumber = true // Sẵn sàng nhận số mới
        updateDisplay()
    }

    // Yêu cầu: Nút C: Xóa phép toán, nhập lại từ đầu
    private fun onClearClick() {
        currentNumber = "0"
        firstOperand = null
        pendingOperation = null
        isNewNumber = true
        updateDisplay()
    }

    private fun updateDisplay() {
        textDisplay.text = currentNumber
    }
}