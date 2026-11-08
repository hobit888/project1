package com.example.questions.data

data class Category(val name: String)

object Categories {
    val all = Category("Все")
    val philosophy = Category("Философия")
    val humor = Category("Юмор")
    val relations = Category("Отношения")
    val fantasy = Category("Фантазия")
    val future = Category("Будущее")

    val list = listOf(all, philosophy, humor, relations, fantasy, future)
}
