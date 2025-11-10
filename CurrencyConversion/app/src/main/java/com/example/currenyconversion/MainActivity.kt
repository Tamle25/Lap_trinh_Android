package com.example.currencyconversion
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconversion.databinding.ActivityMainBinding // Import file binding
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    // Sử dụng View Binding
    private lateinit var binding: ActivityMainBinding

    // Biến lưu trữ tỷ giá, lấy USD làm đồng tiền cơ sở (1 USD = X)
    private val rates = mapOf(
        "USD" to 1.0,
        "VND" to 25447.50,
        "EUR" to 0.92,
        "JPY" to 154.62,
        "GBP" to 0.79,
        "AUD" to 1.50,
        "CAD" to 1.37,
        "CHF" to 0.90,
        "CNY" to 7.24,
        "KRW" to 1338.40,
        "SGD" to 1.35
    )

    // Danh sách tên các đồng tiền
    private val currencies = rates.keys.sorted()

    // Cờ để chống lặp vô hạn khi cập nhật EditText
    private var isUpdating = false

    // Định dạng số cho đẹp
    private val formatter = DecimalFormat("#,##0.####")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Khởi tạo và gán binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinners()
        setupListeners()
    }

    /**
     * Cài đặt Adapter (dữ liệu) cho cả 2 Spinner
     */
    private fun setupSpinners() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerFrom.adapter = adapter
        binding.spinnerTo.adapter = adapter

        // Thiết lập giá trị mặc định
        binding.spinnerFrom.setSelection(currencies.indexOf("USD"))
        binding.spinnerTo.setSelection(currencies.indexOf("VND"))
    }

    /**
     * Hàm tính toán chuyển đổi
     * @param amount Số tiền cần chuyển
     * @param fromRate Tỷ giá của đồng tiền GỐC (so với USD)
     * @param toRate Tỷ giá của đồng tiền ĐÍCH (so với USD)
     * @return Số tiền sau khi quy đổi
     */
    private fun convert(amount: Double, fromRate: Double, toRate: Double): Double {
        // Công thức: Chuyển tiền gốc về USD, sau đó từ USD chuyển sang tiền đích
        val amountInUsd = amount / fromRate
        return amountInUsd * toRate
    }

    /**
     * Gắn các trình lắng nghe sự kiện
     */
    private fun setupListeners() {
        // Lắng nghe thay đổi của EditText "From"
        binding.editTextFrom.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // Chỉ thực hiện khi cờ isUpdating = false và EditText này đang được focus
                if (!isUpdating && binding.editTextFrom.isFocused) {
                    performConversion(isFromSource = true)
                }
            }
        })

        // Lắng nghe thay đổi của EditText "To"
        binding.editTextTo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // Chỉ thực hiện khi cờ isUpdating = false và EditText này đang được focus
                if (!isUpdating && binding.editTextTo.isFocused) {
                    performConversion(isFromSource = false)
                }
            }
        })

        // Lắng nghe sự kiện chọn item của Spinner
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Khi thay đổi Spinner, luôn tính toán lại dựa trên giá trị của "From"
                performConversion(isFromSource = true)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerFrom.onItemSelectedListener = spinnerListener
        binding.spinnerTo.onItemSelectedListener = spinnerListener
    }

    /**
     * Hàm chính thực hiện việc chuyển đổi và cập nhật UI
     * @param isFromSource = true nếu người dùng nhập ở ô "From", false nếu nhập ở ô "To"
     */
    private fun performConversion(isFromSource: Boolean) {
        if (isUpdating) return // Nếu đang cập nhật thì bỏ qua
        isUpdating = true // Đặt cờ, báo là đang cập nhật

        try {
            // Lấy tỷ giá
            val fromCurrency = binding.spinnerFrom.selectedItem.toString()
            val toCurrency = binding.spinnerTo.selectedItem.toString()
            val fromRate = rates[fromCurrency]!!
            val toRate = rates[toCurrency]!!

            if (isFromSource) {
                // Nguồn là "From" -> Cập nhật "To"
                val amountStr = binding.editTextFrom.text.toString()
                val amount = amountStr.toDoubleOrNull() ?: 0.0
                val result = convert(amount, fromRate, toRate)
                binding.editTextTo.setText(formatter.format(result))
            } else {
                // Nguồn là "To" -> Cập nhật "From" (tính ngược)
                val amountStr = binding.editTextTo.text.toString()
                val amount = amountStr.toDoubleOrNull() ?: 0.0
                // Chuyển ngược: (amount, toRate, fromRate)
                val result = convert(amount, toRate, fromRate)
                binding.editTextFrom.setText(formatter.format(result))
            }
        } catch (e: Exception) {
            // Xử lý lỗi nếu có
            e.printStackTrace()
        } finally {
            isUpdating = false // Hoàn tất cập nhật, hạ cờ
        }
    }
}