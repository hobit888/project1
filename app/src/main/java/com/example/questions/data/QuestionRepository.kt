package com.example.questions.data

import com.example.questions.data.lists.fantasyQuestions
import com.example.questions.data.lists.futureQuestions
import com.example.questions.data.lists.humorQuestions
import com.example.questions.data.lists.philosophyQuestions
import com.example.questions.data.lists.relationsQuestions

object QuestionRepository {

    private val questions: List<Question> =
        philosophyQuestions.map { Question(it, Categories.philosophy) } +
        humorQuestions.map { Question(it, Categories.humor) } +
        relationsQuestions.map { Question(it, Categories.relations) } +
        fantasyQuestions.map { Question(it, Categories.fantasy) } +
        futureQuestions.map { Question(it, Categories.future) }

    fun random(category: Category, exclude: Question?): Question {
        val list = if (category == Categories.all) questions else questions.filter { it.category == category }
        return list.filter { it != exclude }.random()
    }

    fun daily(): Question {
        val day = (System.currentTimeMillis() / 86_400_000L).toInt()
        return questions[(day * 37) % questions.size]
    }

    fun find(text: String): Question? = questions.firstOrNull { it.text == text }
}
