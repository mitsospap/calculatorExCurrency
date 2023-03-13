package com.example.calculatorcurrencies

import android.widget.ArrayAdapter

class Spinners {

    fun spinnerEnable(currencies: Int, cont: ExCurrencies): ArrayAdapter<CharSequence> {
        ArrayAdapter.createFromResource(
            cont,
            currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            return adapter
        }
    }
}