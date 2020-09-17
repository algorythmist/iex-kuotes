package com.tecacet.kuotes

import com.google.gson.GsonBuilder
import com.tecacet.kuotes.gson.LocalDateDeserializer
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.time.LocalDate

interface QuoteSupplier {

    fun getQuotes(symbol: String, range: Range = Range.ONE_YEAR): List<Quote>

    fun getDividends(symbol: String, range: Range = Range.ONE_YEAR): List<Dividend>

    fun getSplits(symbol: String, range: Range = Range.ONE_YEAR): List<Split>
}

class IEXQuoteSupplier(val filename: String? = null,
                       val tokenSupplier: TokenSupplier = EnvironmentTokenSupplier()) : QuoteSupplier {

    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val BASE_URL = "https://cloud.iexapis.com/v1"
        private const val CHART_URL = "$BASE_URL/stock/%s/chart/%s"
        private const val DIVIDEND_URL = "$BASE_URL/stock/%s/dividends/%s"
        private const val SPLIT_URL = "$BASE_URL/stock/%s/splits/%s"
    }

    private val gsonBuilder = GsonBuilder()
    private val httpClient = OkHttpClient()

    init {
        gsonBuilder.registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
    }

    private fun readFile(file: File): String {
        val bufferedReader: BufferedReader = file.bufferedReader()
        return bufferedReader.use { it.readText() }
    }

    private fun readResource(name: String) = ClassLoader.getSystemResource(name).readText()

    private fun httpRequest(url: String): String? {
        val httpBuilder = HttpUrl.parse(url)?.newBuilder()!!
        httpBuilder.addQueryParameter("token", tokenSupplier.getToken())
        val request = Request.Builder().url(httpBuilder.build()).build()
        val response = httpClient.newCall(request).execute()
        if (response.code() != 200) {
            val message = response.body()?.string() ?: ""
            throw IOException("Call to $url failed with code ${response.code()}: $message")
        }
        return response.body()?.string()
    }

    private fun getContent(url: String): String? {
        if (filename == null)
            return httpRequest(url)
        val file = File(filename)
        if (file.exists())
            return readFile(file)
        return readResource(filename)
    }

    override fun getQuotes(symbol: String, range: Range): List<Quote> {
        val url = CHART_URL.format(symbol, range.code)
        logger.info(url)
        val json: String? = getContent(url)
        val gson = gsonBuilder.create()
        return gson.fromJson(json, Array<Quote>::class.java).asList()
    }

    override fun getDividends(symbol: String, range: Range): List<Dividend> {
        val url = DIVIDEND_URL.format(symbol, range.code)
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
