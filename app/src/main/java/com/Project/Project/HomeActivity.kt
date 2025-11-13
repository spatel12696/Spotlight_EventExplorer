package com.Project.Project

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.abs

class HomeActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbHelper: EventDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var allEvents: List<Event>

    private lateinit var sensorManager: SensorManager
    private var lastUpdate: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()
        dbHelper = EventDatabaseHelper(this)

        recyclerView = findViewById(R.id.eventsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val mapBtn = findViewById<ImageButton>(R.id.mapButton)
        val savedBtn = findViewById<ImageButton>(R.id.savedEventsButton)
        val logoutBtn = findViewById<ImageButton>(R.id.logoutButton)
        val searchBtn = findViewById<ImageButton>(R.id.searchButton)

        // 🗺️ Open map showing ALL events
        mapBtn.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("showAllEvents", true)
            startActivity(intent)
        }

        savedBtn.setOnClickListener {
            startActivity(Intent(this, SavedEventsActivity::class.java))
        }

        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        searchBtn.setOnClickListener {
            toggleSearchView()
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_UI
        )

        loadEvents()

        Toast.makeText(this, "Shake your device to undo last saved event!", Toast.LENGTH_SHORT).show()
    }

    private fun loadEvents() {
        val events = dbHelper.getAllEvents()
        if (events.size < 8) {
            dbHelper.addEvent(
                Event(
                    name = "Downtown Music Fest",
                    location = "Oshawa Centre",
                    date = "Nov 15, 2025",
                    time = "6:00 PM",
                    description = "Live bands and food trucks in downtown Oshawa!",
                    latitude = 43.945,
                    longitude = -78.895
                )
            )
            dbHelper.addEvent(
                Event(
                    name = "Food Carnival",
                    location = "Lakeview Park",
                    date = "Nov 22, 2025",
                    time = "12:00 PM",
                    description = "Enjoy cuisines from all around the world!",
                    latitude = 43.952,
                    longitude = -78.901
                )
            )
            dbHelper.addEvent(
                Event(
                    name = "Art Exhibit Spotlight",
                    location = "Robert McLaughlin Gallery",
                    date = "Nov 25, 2025",
                    time = "3:00 PM",
                    description = "Explore modern and abstract art installations.",
                    latitude = 43.950,
                    longitude = -78.910
                )
            )
            dbHelper.addEvent(
                Event(
                    name = "Fun Fair",
                    location = "Memorial Park",
                    date = "Dec 1, 2025",
                    time = "10:00 AM",
                    description = "Exciting rides, games, and local food stalls!",
                    latitude = 43.94,
                    longitude = -78.88
                )
            )
            dbHelper.addEvent(
                Event(
                    name = "Fun Fair",
                    location = "North Oshawa Grounds",
                    date = "Dec 8, 2025",
                    time = "11:00 AM",
                    description = "Family fun fair with live performances and snacks!",
                    latitude = 43.96,
                    longitude = -78.86
                )
            )
            dbHelper.addEvent(
                Event(
                    name = "Music Fest",
                    location = "Tribute Communities Centre",
                    date = "Dec 15, 2025",
                    time = "5:00 PM",
                    description = "Rock night with local and international bands!",
                    latitude = 43.897,
                    longitude = -78.863
                )
            )
            dbHelper.addEvent(
                Event(
                    name = "Food Carnival",
                    location = "Harmony Creek Park",
                    date = "Dec 20, 2025",
                    time = "1:00 PM",
                    description = "Delicious street food and dessert trucks all day!",
                    latitude = 43.93,
                    longitude = -78.88
                )
            )
            dbHelper.addEvent(
                Event(
                    name = "Food Carnival",
                    location = "Simcoe Street Plaza",
                    date = "Dec 28, 2025",
                    time = "2:00 PM",
                    description = "Experience cultural foods and music shows!",
                    latitude = 43.915,
                    longitude = -78.87
                )
            )
        }

        allEvents = dbHelper.getAllEvents()
        adapter = EventAdapter(allEvents) { selectedEvent ->
            val intent = Intent(this, EventDetailActivity::class.java).apply {
                putExtra("eventName", selectedEvent.name)
                putExtra("eventLocation", selectedEvent.location)
                putExtra("eventDate", selectedEvent.date)
                putExtra("eventTime", selectedEvent.time)
                putExtra("eventDescription", selectedEvent.description)
                putExtra("eventLatitude", selectedEvent.latitude)
                putExtra("eventLongitude", selectedEvent.longitude)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }

    private fun toggleSearchView() {
        val searchView = SearchView(this)
        searchView.queryHint = "Search events..."
        searchView.isIconified = false
        searchView.isFocusable = true
        searchView.requestFocusFromTouch()

        val dialog = AlertDialog.Builder(this)
            .setTitle("Search Events")
            .setView(searchView)
            .setNegativeButton("Close") { _, _ ->
                filterEvents(null)
            }
            .create()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterEvents(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterEvents(newText)
                return true
            }
        })

        dialog.show()
    }

    private fun filterEvents(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            dbHelper.getAllEvents()
        } else {
            allEvents.filter { it.name.contains(query, ignoreCase = true) }
        }

        adapter = EventAdapter(filteredList) { selectedEvent ->
            val intent = Intent(this, EventDetailActivity::class.java).apply {
                putExtra("eventName", selectedEvent.name)
                putExtra("eventLocation", selectedEvent.location)
                putExtra("eventDate", selectedEvent.date)
                putExtra("eventTime", selectedEvent.time)
                putExtra("eventDescription", selectedEvent.description)
                putExtra("eventLatitude", selectedEvent.latitude)
                putExtra("eventLongitude", selectedEvent.longitude)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val currentTime = System.currentTimeMillis()

            if ((currentTime - lastUpdate) > 150) {
                val diffTime = currentTime - lastUpdate
                lastUpdate = currentTime
                val speed = abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000
                if (speed > 700) showUndoDialog()
                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }

    private fun showUndoDialog() {
        val savedEvents = dbHelper.getSavedEvents()
        if (savedEvents.isNotEmpty()) {
            val lastEvent = savedEvents.last()
            AlertDialog.Builder(this)
                .setTitle("Undo Last Saved Event?")
                .setMessage("Do you want to remove '${lastEvent.name}' from saved events?")
                .setPositiveButton("Yes") { _, _ ->
                    dbHelper.deleteSavedEventByName(lastEvent.name)
                    Toast.makeText(this, "Removed last saved event.", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
        } else {
            Toast.makeText(this, "No saved events to undo.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onPause() { super.onPause(); sensorManager.unregisterListener(this) }
    override fun onResume() { super.onResume(); sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI) }
}
