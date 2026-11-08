package com.example.questions

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.questions.data.Categories
import com.example.questions.data.Category
import com.example.questions.data.FavoritesStorage
import com.example.questions.data.Question
import com.example.questions.data.QuestionRepository

class MainActivity : AppCompatActivity() {

    private lateinit var storage: FavoritesStorage
    private lateinit var card: View
    private lateinit var tagText: TextView
    private lateinit var questionText: TextView
    private lateinit var favoriteButton: TextView
    private lateinit var categoryContainer: LinearLayout

    private val chips = mutableMapOf<Category, TextView>()
    private var selected = Categories.all
    private var current: Question = QuestionRepository.daily()
    private var isDaily = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storage = FavoritesStorage(this)
        card = findViewById(R.id.card)
        tagText = findViewById(R.id.tagText)
        questionText = findViewById(R.id.questionText)
        favoriteButton = findViewById(R.id.favoriteButton)
        categoryContainer = findViewById(R.id.categoryContainer)

        buildCategoryButtons()

        findViewById<TextView>(R.id.randomButton).setOnClickListener { showRandom() }
        findViewById<TextView>(R.id.dailyButton).setOnClickListener {
            current = QuestionRepository.daily()
            isDaily = true
            showQuestion(true)
        }
        favoriteButton.setOnClickListener {
            storage.toggle(current.text)
            val message = if (storage.contains(current.text)) "Добавлено в избранное" else "Удалено из избранного"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            updateFavoriteButton()
        }
        findViewById<TextView>(R.id.shareButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, current.text)
            startActivity(Intent.createChooser(intent, "Поделиться"))
        }
        findViewById<TextView>(R.id.openFavoritesButton).setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        showQuestion(false)
    }

    override fun onResume() {
        super.onResume()
        updateFavoriteButton()
    }

    private fun buildCategoryButtons() {
        for (category in Categories.list) {
            val chip = TextView(this)
            chip.text = category.name
            chip.textSize = 15f
            chip.setPadding(dp(16), dp(8), dp(16), dp(8))
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.marginEnd = dp(8)
            chip.layoutParams = params
            chip.setOnClickListener {
                selected = category
                showRandom()
            }
            categoryContainer.addView(chip)
            chips[category] = chip
        }
    }

    private fun showRandom() {
        current = QuestionRepository.random(selected, current)
        isDaily = false
        showQuestion(true)
    }

    private fun showQuestion(animate: Boolean) {
        tagText.text = if (isDaily) "ВОПРОС ДНЯ" else current.category.name.uppercase()
        questionText.text = current.text
        updateFavoriteButton()
        updateChips()
        if (animate) {
            card.alpha = 0f
            card.animate().alpha(1f).setDuration(250).start()
        }
    }

    private fun updateFavoriteButton() {
        favoriteButton.text = if (storage.contains(current.text)) "В избранном" else "Сохранить"
    }

    private fun updateChips() {
        for ((category, chip) in chips) {
            val isSelected = category == selected
            val background = GradientDrawable()
            background.cornerRadius = dp(10).toFloat()
            background.setColor(Color.parseColor(if (isSelected) "#F2A31B" else "#1C2532"))
            chip.background = background
            chip.setTextColor(Color.parseColor(if (isSelected) "#131A24" else "#B7C2CF"))
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
