package com.example.numberfilter

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.numberfilter.databinding.ActivityMainBinding
import kotlin.math.floor
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var numberAdapter: NumberAdapter

    // list contain 6 RadioButton
    private lateinit var allRadioButtons: List<RadioButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        allRadioButtons = listOf(
            binding.rbOdd, binding.rbPrime, binding.rbPerfect,
            binding.rbEven, binding.rbSquare, binding.rbFibonacci
        )

        // setup Adapter và RecyclerView
        setupRecyclerView()

        binding.editTextNumber.addTextChangedListener {
            updateList()
        }

        setupRadioListeners()

        // update first
        updateList()
    }

    private fun setupRecyclerView() {
        numberAdapter = NumberAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = numberAdapter
        }
    }

    // handle event click
    private fun setupRadioListeners() {
        allRadioButtons.forEach { button ->
            button.setOnClickListener {
                // 1 click 5 off
                allRadioButtons.filter { it != button }.forEach { it.isChecked = false }

                button.isChecked = true

                updateList()
            }
        }
    }

    private fun updateList() {
        val limit = binding.editTextNumber.text.toString().toIntOrNull() ?: 0

        val selectedId = when {
            binding.rbOdd.isChecked -> R.id.rbOdd
            binding.rbPrime.isChecked -> R.id.rbPrime
            binding.rbPerfect.isChecked -> R.id.rbPerfect
            binding.rbEven.isChecked -> R.id.rbEven
            binding.rbSquare.isChecked -> R.id.rbSquare
            binding.rbFibonacci.isChecked -> R.id.rbFibonacci
            else -> R.id.rbOdd // default old button
        }

        val generatedNumbers = mutableListOf<Int>()

        for (i in 1 until limit) {
            val shouldAdd = when (selectedId) {
                R.id.rbOdd -> isOdd(i)
                R.id.rbEven -> isEven(i)
                R.id.rbPrime -> isPrime(i)
                R.id.rbSquare -> isPerfectSquare(i.toLong())
                R.id.rbPerfect -> isPerfectNumber(i)
                R.id.rbFibonacci -> isFibonacci(i)
                else -> false
            }
            if (shouldAdd) {
                generatedNumbers.add(i)
            }
        }

        if (generatedNumbers.isEmpty()) {
            binding.recyclerView.visibility = View.GONE
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.GONE
            numberAdapter.submitList(generatedNumbers)
        }
    }

    private fun isOdd(n: Int): Boolean = n % 2 != 0
    private fun isEven(n: Int): Boolean = n % 2 == 0
    private fun isPrime(n: Int): Boolean {
        if (n <= 1) return false
        if (n <= 3) return true
        if (n % 2 == 0 || n % 3 == 0) return false
        var i = 5
        while (i * i <= n) {
            if (n % i == 0 || n % (i + 2) == 0) return false
            i += 6
        }
        return true
    }
    private fun isPerfectSquare(n: Long): Boolean {
        if (n < 0) return false
        val root = sqrt(n.toDouble())
        return root == floor(root)
    }
    private fun isPerfectNumber(n: Int): Boolean {
        if (n <= 1) return false
        var sum = 1
        for (i in 2 until sqrt(n.toDouble()).toInt() + 1) {
            if (n % i == 0) {
                sum += i
                if (i * i != n) {
                    sum += n / i
                }
            }
        }
        return sum == n
    }
    private fun isFibonacci(n: Int): Boolean {
        val nLong = n.toLong()
        return isPerfectSquare(5L * nLong * nLong + 4) || isPerfectSquare(5L * nLong * nLong - 4)
    }
}