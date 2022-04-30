package io.ymsoft.androidstudy.main

import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class DateImpl : Dateretrun {
    val format = SimpleDateFormat("yyyyMMdd")
    val d = format.parse("20220430")!!
    override suspend fun get(): Date {
        delay(1000)
        return d
    }
}