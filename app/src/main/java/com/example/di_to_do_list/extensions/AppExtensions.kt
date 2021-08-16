package com.example.di_to_do_list.extensions

import android.annotation.SuppressLint
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

private val locale = Locale("pt", "BR")

// Irá converter a Date recebida para String
fun Date.format():String {
    return SimpleDateFormat("dd/MM/yy", locale).format(this)
}

var TextInputLayout.text : String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }