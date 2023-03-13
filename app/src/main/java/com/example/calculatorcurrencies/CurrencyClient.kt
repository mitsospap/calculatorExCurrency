package com.example.calculatorcurrencies

import org.json.JSONObject
import java.net.URL

class CurrencyClient {
    private var apiKey = "your API-KEY"

    fun getRate(baseCurrency: String, convertedCurrency: String): Float {
        val api = "https://api.apilayer.com/fixer/latest?base=$baseCurrency&symbols=$convertedCurrency&apikey=$apiKey"
        val apiResult = URL(api).readText()
        val jsonObject = JSONObject(apiResult)
        return jsonObject.getJSONObject("rates").getString(
            convertedCurrency
        ).toFloat()
    }
}
