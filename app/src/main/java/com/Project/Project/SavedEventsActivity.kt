package com.Project.Project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SavedEventsActivity : AppCompatActivity() {

    private lateinit var dbHelper: EventDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var emptyText: TextView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_events)

        dbHelper = EventDatabaseHelper(this)
        recyclerView = findViewById(R.id.savedEventsRecyclerView)
        emptyText = findViewById(R.id.emptyText)
        backButton = findViewById(R.id.backButton)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadSavedEvents()

        backButton.setOnClickListener {
            finish() // 🔙 Go back to HomeActivity
        }
    }

    private fun loadSavedEvents() {
        val savedEvents = dbHelper.getSavedEvents()

        if (savedEvents.isEmpty()) {
            emptyText.text = "No events added."
            emptyText.visibility = TextView.VISIBLE
            recyclerView.visibility = RecyclerView.GONE
        } else {
            emptyText.visibility = TextView.GONE
            recyclerView.visibility = RecyclerView.VISIBLE

            // 🟡 Clicking a saved event now opens its detail page
            adapter = EventAdapter(savedEvents) { selectedEvent ->
                val intent = Intent(this, EventDetailActivity::class.java)
                intent.putExtra("eventName", selectedEvent.name)
                intent.putExtra("eventLocation", selectedEvent.location)
                intent.putExtra("eventDate", selectedEvent.date)
                intent.putExtra("eventDescription", selectedEvent.description)
                intent.putExtra("eventTime", selectedEvent.time)
                startActivity(intent)
            }

            recyclerView.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh list when coming back from EventDetailActivity
        loadSavedEvents()
    }
}
