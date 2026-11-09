package com.example.questions.data

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object AiQuestionClient {
    private const val SERVER_URL = "http://10.0.2.2:3000/generate"

    fun generate(category: String, count: Int = 1): List<String> {
        val connection = (URL(SERVER_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 15_000
            readTimeout = 90_000
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
        }

        try {
            val body = JSONObject()
                .put("category", category)
                .put("count", count)
                .toString()

            connection.outputStream.use { output ->
                output.write(body.toByteArray(Charsets.UTF_8))
            }

            val status = connection.responseCode
            val stream = if (status in 200..299) connection.inputStream else connection.errorStream
            val responseText = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()

            if (status !in 200..299) {
                throw IllegalStateException("Сервер ответил HTTP $status: $responseText")
            }

            val questionsText = JSONObject(responseText).optString("questions")
            val questions = questionsText
                .lineSequence()
                .map { it.trim().replace(Regex("^\\d+[.)]\\s*"), "") }
                .filter { it.isNotBlank() }
                .toList()

            if (questions.isEmpty()) {
                throw IllegalStateException("Сервер не вернул вопросы: $responseText")
            }
            return questions
        } finally {
            connection.disconnect()
        }
    }
}
