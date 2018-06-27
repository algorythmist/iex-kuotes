package com.tecacet.kuotes.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tecacet.kuotes.QualifiedStatus
import java.lang.reflect.Type

class QualifiedStatusDeserializer : JsonDeserializer<QualifiedStatus> {
    override fun deserialize(elem: JsonElement?, p1: Type?, p2: JsonDeserializationContext?): QualifiedStatus? {
        val json = elem ?: return null
        return QualifiedStatus.fromCode(json.asString)
    }
}