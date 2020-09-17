package com.tecacet.kuotes

interface TokenSupplier {

    fun getToken() : String?
}

class EnvironmentTokenSupplier : TokenSupplier {

    override fun getToken(): String? {
        val env = System.getenv()
        return env["IEX_TOKEN"]
    }

}