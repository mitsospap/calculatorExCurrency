package com.example.calculatorcurrencies

import java.math.BigDecimal
import java.text.DecimalFormat

class Calculator(
    // check if can convert this to string (CharSequence vs String)
    private val input: CharSequence
) {

    fun calculate(): String {
        val numbersOperators = convertTextToSymbols(input)
        if (numbersOperators.isEmpty()) return ""
        else if (numbersOperators.last() is Operator) numbersOperators.removeLast()     //check for unfinished operations

        val doMultiply = doMultiplyCalculate(numbersOperators)
        return calculateAddSub(doMultiply)
    }

    private fun convertTextToSymbols(input: CharSequence): MutableList<Symbol> {
        val list = mutableListOf<Symbol>()
        var currentNumber = ""
        for (character in input) {
            if (character.isDigit() || character == '.')
                currentNumber += character
            else {
                list.add(Number(currentNumber.toBigDecimal()))
                currentNumber = ""
                list.add(Operator.of(character))
            }
        }
        if (currentNumber != "")
            list.add(Number(currentNumber.toBigDecimal()))
        return list
    }

    private fun doMultiplyCalculate(passedList: List<Symbol>): List<Symbol> {
        var list = passedList
        while (list.any { it is Mult || it is Div || it is Mod }) {
            list = calculateMultiply(list)
        }
        return list
    }

    private fun calculateMultiply(passedList: List<Symbol>): MutableList<Symbol> {
        val newList = mutableListOf<Symbol>()
        var endIndex = passedList.size          //do one operation per time to succeed operation's priority

        for (i in passedList.indices) {
            val operation = passedList[i]
            if (operation is Operator && i != passedList.lastIndex && i < endIndex) {
                val firstNumber = passedList[i - 1] as Number
                val secondNumber = passedList[i + 1] as Number

                when (operation) {
                    is Mult, is Div, is Mod-> {
                        if ((operation is Div || operation is Mod) && secondNumber.value.compareTo(
                                BigDecimal.ZERO) == 0){
                            return emptyList<Symbol>().toMutableList()
                        }else
                        {
                            newList.add(operation.action(firstNumber, secondNumber))
                            endIndex = i + 1
                        }
                    }
                    else -> {
                        newList.add(firstNumber)
                        newList.add(operation)
                    }
                }
            }
            if (i > endIndex) newList.add(passedList[i])
        }
        return newList
    }

    private fun calculateAddSub(passedList: List<Symbol>): String {
        if (passedList.isEmpty()) return ""
        var sum = passedList[0] as Number

        for (i in passedList.indices) {         // commutative property doesn't care about priority
            val operator = passedList[i]
            if (operator is Operator && i != passedList.lastIndex) {
                val secondNumber = passedList[i + 1] as Number
                sum = when (passedList[i]) {
                    is Plus, is Minus -> operator.action(sum, secondNumber)
                    else -> throw RuntimeException()
                }
            }
        }
        return sum.value.toString()
    }
}