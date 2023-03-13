package com.example.calculatorcurrencies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Toast
import com.example.calculatorcurrencies.databinding.ActivityExCurrenciesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExCurrencies : AppCompatActivity() {
    private lateinit var binding: ActivityExCurrenciesBinding
    var baseCurrency: String = "EUR"
    var convertedToCurrency: String = "USD"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExCurrenciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etSecondConversion.isEnabled = false
        spinnerSetup()
        textChanged()
    }

    private fun textChanged() {     //for live result
        binding.etFirstConversion.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "Before Text Changed")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Main", "On Text Changed")
            }

            override fun afterTextChanged(p0: Editable?) {
                try {
                    getApiResult(baseCurrency, convertedToCurrency)
                } catch (e: Exception) {
                    Log.e("Main", "$e")
                }
            }
        })
    }

    private fun getApiResult(baseCurrency: String, convertedCurrency: String) {
        if (binding.etFirstConversion != null && binding.etFirstConversion.text.isNotEmpty() && binding.etFirstConversion.text.isNotBlank()) {

            if (this.baseCurrency == convertedCurrency) {
                Toast.makeText(
                    applicationContext,
                    "Cannot convert the same currency",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val conversionRate =
                            CurrencyClient().getRate(baseCurrency, convertedCurrency)

                        withContext(Dispatchers.Main) {
                            val text = ((binding.etFirstConversion.text.toString()
                                .toFloat()) * conversionRate).toString()
                            binding.etSecondConversion.setText(text)
                        }
                    } catch (e: Exception) {
                        Log.e("Main", "$e")
                    }
                }
            }
        }
    }

    private fun spinnerSetup() {
        binding.spinnerFirstConversion.adapter = Spinners().spinnerEnable(R.array.currencies, this)
        binding.spinnerSecondConversion.adapter = Spinners().spinnerEnable(R.array.currencies2, this)

        binding.spinnerFirstConversion.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    baseCurrency = parent?.getItemAtPosition(position).toString()
                    getApiResult(baseCurrency, convertedToCurrency)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            })

        binding.spinnerSecondConversion.onItemSelectedListener =
            (object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    convertedToCurrency = parent?.getItemAtPosition(position).toString()
                    getApiResult(baseCurrency, convertedToCurrency)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            })
    }

    fun calculatorMode(view: View) {
        if (view is Button) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}