package com.tecacet.kuotes.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate


class LocalDateDeserializer : JsonDeserializer<LocalDate> {

    override fun deserialize(elem: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): LocalDate? {
        val json = elem ?: return null
        return LocalDate.parse(json.asString)
    }


}