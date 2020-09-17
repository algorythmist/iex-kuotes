package com.tecacet.kuotes

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class IEXQuoteSupplierTest {

    @Test
    fun testReadQuotes() {
        val quoteReader = IEXQuoteSupplier()
        val quotes1m = quoteReader.getQuotes("NFLX", Range.ONE_MONTH)
        assertTrue(quotes1m.size > 19)

        val quotes1y = quoteReader.getQuotes("NFLX")
        assertTrue(quotes1y.size > 250)
    }

    @Test
    fun testReadQuotesFromResource() {
        val quoteReader = IEXQuoteSupplier(filename = "nflx_1y.json")
        val quotes1m = quoteReader.getQuotes("NFLX")
        assertEquals(253, quotes1m.size)
        val quote1 = quotes1m[0]
        assertEquals(155.03, quote1.close)
        assertEquals(5803446, quote1.volume)
        assertEquals(LocalDate.of(2017, 6, 21), quote1.date)
    }

    @Test
    fun testReadDividends() {
        val quoteReader = IEXQuoteSupplier()
        val dividends = quoteReader.getDividends("AAPL", Range.FIVE_YEARS)
        assertTrue(dividends.size > 0) //This API appears to be faulty

        val dividend = dividends[0]
        assertEquals("Ordinary Shares", dividend.description)
        assertEquals("quarterly", dividend.frequency)
    }

    @Test
    fun testReadSplits() {
        val quoteReader = IEXQuoteSupplier()
        val splits = quoteReader.getSplits("AAPL", Range.FIVE_YEARS)
        assertEquals(1, splits.size)
        val split = splits[0]
        assertEquals(4, split.toFactor)
        assertEquals(1, split.fromFactor)
        assertEquals(LocalDate.of(2020, 8, 31), split.exDate)
    }
}