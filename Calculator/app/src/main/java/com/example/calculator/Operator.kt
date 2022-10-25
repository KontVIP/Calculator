package com.example.calculator

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

sealed class Operator {

    fun handle(value: Double): String = "$value ${operator() }"

    @Composable
    fun Text(): Unit = Text(text = operator())

    protected abstract fun operator(): String

    class Plus() : Operator() {
        override fun operator() = "+"
    }

    class Minus() : Operator() {
        override fun operator() = "-"
    }

    class Multiply() : Operator() {
        override fun operator() = "*"
    }

    class Divide() : Operator() {
        override fun operator() = "/"
    }


}