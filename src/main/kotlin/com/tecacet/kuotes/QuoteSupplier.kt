package com.tecacet.kuotes

import com.google.gson.GsonBuilder
import com.tecacet.kuotes.gson.LocalDateDeserializer
import com.tecacet.kuotes.gson.QualifiedStatusDeserializer
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.time.LocalDate

interface QuoteSupplier {

    fun getQuotes(symbol: String, range : Range = Range.ONE_YEAR) : List<Quote>

    fun getDividends(symbol :String, range : Range = Range.ONE_YEAR) : List<Dividend>

    fun getSplits(symbol :String, range : Range = Range.ONE_YEAR) : List<Split>
}

class IEXQuoteSupplier(val filename : String? = null) : QuoteSupplier {

    companion object {
        val logger = LoggerFactory.getLogger(this::class.java)
    }

    private val CHART_URL = "https://api.iextrading.com/1.0/stock/%s/chart/%s"
    private val DIVIDENT_URL = "https://api.iextrading.com/1.0/stock/%s/dividends/%s"
    private val SPLIT_URL = "https://api.iextrading.com/1.0/stock/%s/splits/%s"

    private val gsonBuilder = GsonBuilder()
    private val httpClient =  OkHttpClient()

    init {
        gsonBuilder.registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
        gsonBuilder.registerTypeAdapter(QualifiedStatus::class.javaObjectType, QualifiedStatusDeserializer())
    }

    private fun readFile(file : File): String {
        val bufferedReader: BufferedReader = file.bufferedReader()
        return bufferedReader.use { it.readText() }
    }

    private fun readResource(name: String) = ClassLoader.getSystemResource(name).readText()

    private fun httpRequest(url: String): String? {
        val request = Request.Builder().url(url).build();
        val response = httpClient.newCall(request).execute();
        return response.body()?.string();
    }

    private fun getContent(url : String) :String? {
       if (filename ==  null)
            return httpRequest(url)
        val file = File(filename)
        if (file.exists())
            return readFile(file)
        return readResource(filename)
    }

    override fun getQuotes(symbol :String, range : Range): List<Quote> {
        val url = CHART_URL.format(symbol, range.code)
        logger.info(url)
        val json: String? = getContent(url)
        val gson = gsonBuilder.create()
        return gson.fromJson(json, Array<Quote>::class.java).asList()
    }

    override fun getDividends(symbol: String, range: Range): List<Dividend> {
        val url = DIVIDENT_URL.format(symbol, range.code)
        logger.info(url)
        val json: String? = getContent(url)
        val gson = gsonBuilder.create()
        return gson.fromJson(json, Array<Dividend>::class.java).asList()
    }

    override fun getSplits(symbol: String, range: Range): List<Split> {
        val url = SPLIT_URL.format(symbol, range.code)
        logger.info(url)
        val json: String? = getContent(url)
        val gson = gsonBuilder.create()
        return gson.fromJson(json, Array<Split>::class.java).asList()
    }
}