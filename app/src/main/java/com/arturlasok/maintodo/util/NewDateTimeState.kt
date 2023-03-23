package com.arturlasok.maintodo.util

data class NewDateTimeState(
    val taskToken: String = "",
    val taskDateTimeError: Int = 0,
    val taskDate: Long = 0L,
    val taskTime: Long = 0L,
    val notDate: Long = 0L,
    val notTime:Long = 0L
)
