package com.example.calculatorcurrencies

import java.math.BigDecimal
import java.text.ParseException

abstract class Symbol

class Number(val value: BigDecimal) : Symbol()

sealed class Operator : Symbol() {

    companion object {
        fun of(input: Char) = when (input) {
            '+' -> Plus
            '-' -> Minus
            '/' -> Div
            'x' -> Mult
            '%' -> Mod
            else -> throw ParseException(input.toString(), 0)
        }
    }

    abstract fun action(left: Number, right: Number): Number
}

object Plus : Operator() {
    override fun action(left: Number, right: Number) =
        Number(left.value + right.value)
}

object Minus : Operator() {
    override fun action(left: Number, right: Number) =
        Number(left.value - right.value)
}

object Mult : Operator() {
    override fun action(left: Number, right: Number) =
        Number(left.value * right.value)
}

object Div : Operator() {
    override fun action(left: Number, right: Number) =
        Number(left.value / right.value)
}

object Mod : Operator() {
    override fun action(left: Number, right: Number) =
        Number(left.value % right.value)
}
