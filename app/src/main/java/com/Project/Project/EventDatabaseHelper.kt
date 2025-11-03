package com.Project.Project

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EventDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "spotlight.db"
        private const val DATABASE_VERSION = 4

        private const val TABLE_EVENTS = "events"
        private const val TABLE_SAVED = "saved_events"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createEvents = """
            CREATE TABLE $TABLE_EVENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_LOCATION TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_TIME TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_LATITUDE REAL,
                $COLUMN_LONGITUDE REAL
            )
        """.trimIndent()

        val createSaved = """
            CREATE TABLE $TABLE_SAVED (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT UNIQUE,
                $COLUMN_LOCATION TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_TIME TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_LATITUDE REAL,
                $COLUMN_LONGITUDE REAL
            )
        """.trimIndent()

        db.execSQL(createEvents)
        db.execSQL(createSaved)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SAVED")
        onCreate(db)
    }

    fun addEvent(event: Event) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, event.name)
            put(COLUMN_LOCATION, event.location)
            put(COLUMN_DATE, event.date)
            put(COLUMN_TIME, event.time)
            put(COLUMN_DESCRIPTION, event.description)
            put(COLUMN_LATITUDE, event.latitude)
            put(COLUMN_LONGITUDE, event.longitude)
        }
        db.insert(TABLE_EVENTS, null, values)
        db.close()
    }

    fun getAllEvents(): List<Event> {
        val events = mutableListOf<Event>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_EVENTS", null)

        if (cursor.moveToFirst()) {
            do {
                events.add(
                    Event(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return events
    }

    fun saveEvent(event: Event) {
        if (isEventSaved(event.name)) return
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, event.name)
            put(COLUMN_LOCATION, event.location)
            put(COLUMN_DATE, event.date)
            put(COLUMN_TIME, event.time)
            put(COLUMN_DESCRIPTION, event.description)
            put(COLUMN_LATITUDE, event.latitude)
            put(COLUMN_LONGITUDE, event.longitude)
        }
        db.insert(TABLE_SAVED, null, values)
        db.close()
    }

    fun isEventSaved(name: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_SAVED WHERE $COLUMN_NAME=?", arrayOf(name))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun getSavedEvents(): List<Event> {
        val saved = mutableListOf<Event>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_SAVED", null)

        if (cursor.moveToFirst()) {
            do {
                saved.add(
                    Event(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        location = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME)),
                        description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return saved
    }

    fun deleteSavedEventByName(name: String) {
        val db = writableDatabase
        db.delete(TABLE_SAVED, "$COLUMN_NAME=?", arrayOf(name))
        db.close()
    }
}
