package com.tecacet.kuotes

import java.time.LocalDate

enum class Range(val code: String) {
    ONE_YEAR("1y"), TWO_YEARS("2y"), FIVE_YEARS("5y"), YTD("ytd"),
    ONE_MONTH("1m"), THREE_MONTHS("3m"), SIX_MONTHS("6m")
}

data class Quote(
        val date: LocalDate,
        val open: Double,
        val close: Double,
        val high: Double,
        val low: Double,
        val volume: Long
)

data class Dividend(
        val exDate: LocalDate,
        val paymentDate: LocalDate,
        val recordDate: LocalDate,
        val declaredDate: LocalDate,
        val amount: Double,
        val frequency: String,
        val description: String,
        val flag: String?
)


data class Split(
        val exDate: LocalDate,
        val declaredDate: LocalDate,
        val toFactor: Int,
        val fromFactor: Int
)