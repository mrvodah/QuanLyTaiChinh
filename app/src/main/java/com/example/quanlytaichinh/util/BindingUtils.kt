package com.example.quanlytaichinh.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.quanlytaichinh.model.Section
import java.text.NumberFormat
import java.util.*

@BindingAdapter("sectionPercent")
fun TextView.setPercentString(section: Section) {
    text = "${section.percent}%"
}

@BindingAdapter("sectionRemainMoney")
fun TextView.setRemainString(section: Section) {
    text = NumberFormat.getNumberInstance(Locale.GERMANY).format(section.total - section.paid)
}

@BindingAdapter("formatMoney")
fun TextView.setFormatMoneyString(money: Long) {
    text = NumberFormat.getNumberInstance(Locale.GERMANY).format(money)
}