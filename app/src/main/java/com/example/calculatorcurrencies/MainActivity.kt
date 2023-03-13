package com.example.calculatorcurrencies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.calculatorcurrencies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = mutableListOf<Boolean>()      //to keep history for backspace button
    private val canAddDecimal = mutableListOf<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        resetCalculator()
    }

    fun numberAction(view: View) {
        if (view is Button) {
            val length = binding.workingsTV.length()
            if (view.text == ".") {
                if (length == 0){                  //check '.' for first digit
                    binding.workingsTV.append("0.")     // if so append "0."
                    canAddDecimal.addAll(listOf(true, false))
                    canAddOperation.addAll(listOf(true, false))
                }
                else if (canAddDecimal[length] && canAddOperation[length]) {    //check if there is '.' before
                    binding.workingsTV.append(view.text)                        //if not, store it
                    canAddDecimal.add(false)
                    canAddOperation.add(false)
                } else if (canAddDecimal[length - 1] && !canAddOperation[length]) { // else if there is an operator
                    replaceOperatorOrDecimal(length, view.text)                     //check if there is '.' to the number before
                    canAddDecimal[length] = false                                   //if so replace operator with '.'
                    canAddOperation[length] = false
                }
            } else {
                binding.workingsTV.append(view.text)
                canAddOperation.add(true)
                canAddDecimal.add(canAddDecimal[length])
                if (length == 0) {
                    canAddDecimal[1] = true
                }
            }
        }
    }

    fun operationAction(view: View) {
        if (view is Button) {
            val length = binding.workingsTV.length()
            if (length > 0 && canAddOperation[length]) {
                binding.workingsTV.append(view.text)
                canAddOperation.add(false)
                canAddDecimal.add(true)
            } else if (length > 0 && !canAddOperation[length]) {    //if there is operator or '.'
                replaceOperatorOrDecimal(length, view.text)         //replace operator
                canAddOperation[length] = false
                canAddDecimal[length] = true
            }
        }
    }

    fun allClearAction(view: View) {
        if (view is Button) {
            resetCalculator()
        }
    }

    fun backSpaceAction(view: View) {
        if (view is Button) {
            val length = binding.workingsTV.length()
            if (length > 0) {       //do not forget to update history decimal and operation lists
                binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
                canAddDecimal.removeLast()
                canAddOperation.removeLast()
            }
        }
    }

    fun equalsAction(view: View) {
        if (view is Button) binding.resultsTV.text = calculateResults()
    }

    fun exchangeCurrencies(view: View) {
        if (view is Button) {
            val intent = Intent(this, ExCurrencies::class.java)
            startActivity(intent)
        }
    }

    private fun calculateResults(): String {
        val userInput = binding.workingsTV.text
        var result = Calculator(userInput).calculate()
        if (result == "" && userInput.isNotEmpty()) {       //check for 0 in division or mod
            resetCalculator()
            Toast.makeText(
                applicationContext,
                "You can't divide with zero",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (result.endsWith(".0")) result = result.replace(".0", "")
        return result
    }

    private fun replaceOperatorOrDecimal(n: Int , operation: CharSequence) {
        binding.workingsTV.text = binding.workingsTV.text.subSequence(0, n - 1)
        binding.workingsTV.append(operation)
    }

    private fun resetCalculator() {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""
        canAddOperation.clear()
        canAddOperation.add(false)
        canAddDecimal.clear()
        canAddDecimal.add(false)
    }

}