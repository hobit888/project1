package com.example.questions

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.questions.data.FavoritesStorage
import com.example.questions.data.QuestionRepository

class FavoritesActivity : AppCompatActivity() {

    private lateinit var storage: FavoritesStorage
    private lateinit var listView: ListView
    private lateinit var emptyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        storage = FavoritesStorage(this)
        listView = findViewById(R.id.favoritesList)
        emptyText = findViewById(R.id.emptyText)

        listView.setOnItemLongClickListener { _, _, position, _ ->
            storage.toggle(listView.adapter.getItem(position) as String)
            loadList()
            true
        }
        loadList()
    }

    private fun loadList() {
        val items = storage.getAll()
        listView.adapter = FavoritesAdapter(items)
        emptyText.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    private inner class FavoritesAdapter(items: List<String>) :
        ArrayAdapter<String>(this@FavoritesActivity, 0, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.item_favorite, parent, false)
            val text = getItem(position) ?: ""
            val question = QuestionRepository.find(text)

            view.findViewById<TextView>(R.id.itemCategory).text = question?.category?.name?.uppercase() ?: ""
            view.findViewById<TextView>(R.id.itemText).text = text
            return view
        }
    }
}
