package com.example.questions.data

import android.content.Context

class FavoritesStorage(context: Context) {

    private val prefs = context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    fun getAll(): List<String> {
        return (prefs.getStringSet("items", emptySet()) ?: emptySet()).toList()
    }

    fun contains(text: String): Boolean = getAll().contains(text)

    fun toggle(text: String) {
        val items = getAll().toMutableSet()
        if (!items.remove(text)) items.add(text)
        prefs.edit().putStringSet("items", items).apply()
    }
}
