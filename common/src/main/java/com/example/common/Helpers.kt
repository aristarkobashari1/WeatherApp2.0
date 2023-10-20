package com.example.common

import android.content.Context
import android.widget.Toast
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun Int.getDayFromTimestamp(): String {
    val zoneId = ZoneId.systemDefault()
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(this.toLong()), zoneId)

    val dateFormatter = DateTimeFormatter.ofPattern("d EEEE HH:mm", Locale.ENGLISH)
    return dateTime.format(dateFormatter)
}

fun Int.getClockFromTimestamp(): String {
    val zoneId = ZoneId.systemDefault()
    val dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(this.toLong()), zoneId)

    val dateFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
    return dateTime.format(dateFormatter)
}

fun Context.makeToastShort(text:String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}
fun Context.makeToastLong(text:String){
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun Pair<Double,Double>.isZero()= first==0.0 && second==0.0
