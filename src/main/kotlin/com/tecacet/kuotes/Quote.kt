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

enum class QualifiedStatus(val code : String) {
    QUALIFIED("Q"), PARTIALLY_QUALIFIED("P"), NON_QUALIFIED("N");

    companion object {
        fun fromCode(code : String) : QualifiedStatus? {
            return when (code) {
                "Q" -> QUALIFIED
                "P" -> PARTIALLY_QUALIFIED
                "N" -> NON_QUALIFIED
                else -> null
            }
        }
    }
}

data class Dividend(
        val exDate: LocalDate,
        val paymentDate: LocalDate,
        val recordDate: LocalDate,
        val declaredDate: LocalDate,
        val amount: Double,
        val type: String,
        val qualified : QualifiedStatus?
)


data class Split(
        val exDate: LocalDate,
        val paymentDate: LocalDate,
        val recordDate: LocalDate,
        val declaredDate: LocalDate,
        val toFactor: Int,
        val forFactor: Int
)