package com.example.hutbe.services

import com.example.hutbe.model.Hutbe
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*

object HutbeRepository {
    private const val BASE_URL = "https://dinhizmetleri.diyanet.gov.tr"
    private const val HUTBE_URL = "$BASE_URL/kategoriler/yayinlarimiz/hutbeler/türkçe"

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchHutbeler(): List<Hutbe> = withContext(Dispatchers.IO) {
        val html = client.get(HUTBE_URL) {
            headers {
                append("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/114.0.0.0 Safari/537.36")
                append("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                append("Accept-Language", "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7")
                append("Referer", "https://dinhizmetleri.diyanet.gov.tr/")
                append("Connection", "keep-alive")
                append("Cache-Control", "max-age=0")
            }
        }.bodyAsText()

        // JSON verisini izole et
        val startIndex = html.indexOf("var WPQ1ListData = ")
        if (startIndex == -1) return@withContext emptyList()

        val jsonStart = html.substring(startIndex + "var WPQ1ListData = ".length)
        val endIndex = jsonStart.indexOf("};")
        if (endIndex == -1) return@withContext emptyList()

        val rawJson = jsonStart.substring(0, endIndex + 1)

        // JSON parse et
        val root = Json.parseToJsonElement(rawJson).jsonObject
        val rows = root["Row"]?.jsonArray ?: JsonArray(emptyList())

        rows.mapNotNull { item ->
            try {
                val obj = item.jsonObject
                val title = htmlDecode(obj["Title"]?.jsonPrimitive?.content ?: "")

                Hutbe(
                    ID = obj["ID"]?.jsonPrimitive?.content ?: "",
                    Title = title,
                    Tarih = obj["Tarih"]?.jsonPrimitive?.content,
                    PDF = obj["PDF"]?.jsonPrimitive?.content?.let { BASE_URL + it },
                    Ses = obj["Ses"]?.jsonPrimitive?.content?.let { BASE_URL + it }
                )
            } catch (e: Exception) {
                println("Parse hatası: ${e.message}")
                null
            }
        }
    }

    private fun htmlDecode(str: String): String =
        str.replace("&quot;", "\"")
            .replace("&amp;", "&")
            .replace("&#39;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
}
