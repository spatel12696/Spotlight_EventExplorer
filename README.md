# 🎯 Spotlight – EventExplorer

**Spotlight** (EventExplorer) is an Android mobile application built using **Kotlin** that allows users to explore, save, and interact with local events in their community.  
The app integrates multiple Android components such as **Firebase Authentication**, **SQLite Database**, **Google Maps**, **Multimedia (Audio/Video)**, and **Accelerometer Sensors** to deliver a modern and interactive event discovery experience.

---

## 🧭 Project Overview

Spotlight is designed to help users **discover events**, **view event details**, and **save their favorites**.  
It brings together multiple Android concepts into a single cohesive prototype, demonstrating user authentication, database integration, mapping, and multimedia capabilities.

---

## 🚀 Key Features

### 🔐 User Authentication (Firebase)
- Secure **login** and **signup** using Firebase Authentication.
- Redirects authenticated users directly to the Home screen.
- Prevents unauthorized access to event data.

### 🏠 Dynamic Home Page
- Displays events using **RecyclerView** in a clean, scrollable list.
- Toolbar includes:
  - **Map** button (left)
  - **Saved Events** button (right)
  - **Logout** button (right)
- Features a **Search Bar** (top right) that filters events by name in real-time.

### 🗺️ Google Maps Integration
- Shows all event locations with **custom markers**.
- Includes user’s **current GPS location**.
- Tapping “View on Map” in an event detail centers the map on that specific event.

### 💾 Database Integration (SQLite)
- Local database with two tables:
  - `events` – all available events.
  - `saved_events` – events user has saved.
- Prevents duplicate entries.
- Supports adding, viewing, and deleting saved events.

### 🎬 Multimedia Integration
- Each event has a **promotional video or audio**.
- Integrated **VideoView** and **MediaPlayer** for smooth playback.
- Examples:
  - 🎵 *Music Fest* – plays MP3 background music.
  - 🎥 *Food Carnival / Fun Fair / Art Exhibit* – plays MP4 event video.

### 📱 Sensor Integration (Accelerometer)
- Detects **shake gesture** using accelerometer.
- On shake, prompts:
  > “Undo last saved event?”
- If confirmed, the most recent saved event is removed automatically.

### ⭐ Saved Events Page
- Displays all user-saved events.
- Allows removing events.
- Shows “No events added” if list is empty.
- Includes a **Back** button for easy navigation.

---

## 🧠 Tech Stack

| Component | Technology |
|------------|-------------|
| **Language** | Kotlin |
| **Database** | SQLite (via `SQLiteOpenHelper`) |
| **Authentication** | Firebase Authentication |
| **Maps** | Google Maps SDK |
| **Media** | VideoView, MediaPlayer |
| **Sensors** | Accelerometer |
| **UI** | XML Layouts (Material Design) |
| **IDE** | Android Studio |

---

## 🗂 Project Structure

```
app/src/main/java/com/Project/Project/
│
├── HomeActivity.kt
├── LoginActivity.kt
├── SignupActivity.kt
├── EventDetailActivity.kt
├── MapActivity.kt
├── SavedEventsActivity.kt
├── SensorActivity.kt
├── Event.kt
├── EventAdapter.kt
└── EventDatabaseHelper.kt

app/src/main/res/
│
├── layout/          # XML Layouts
├── drawable/        # Icons (ic_map, ic_star, ic_logout, ic_search)
├── raw/             # Audio/Video media files
└── values/          # Colors, Styles, Strings
```

---

## ⚙️ Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/spatel12696/Spotlight_EventExplorer.git
   ```

2. **Open in Android Studio**
   - File → Open → Select this project folder.

3. **Sync Gradle**
   - Let Android Studio install dependencies automatically.

4. **Add Firebase Configuration**
   - Place your `google-services.json` file inside `/app`.

5. **Add Google Maps API Key**
   - Open `AndroidManifest.xml` and replace the placeholder API key:
     ```xml
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="YOUR_API_KEY_HERE" />
     ```

6. **Run the App**
   - On an emulator or physical Android device (Android 8.0+ recommended).

---

## 📸 Suggested Screenshots

You can include these later in your README using markdown:
```markdown
![Login Screen](screenshots/login.png)
![Home Page](screenshots/home.png)
![Event Detail](screenshots/event_detail.png)
![Google Maps](screenshots/maps.png)
![Saved Events](screenshots/saved_events.png)
![Undo Dialog](screenshots/undo_dialog.png)
![Search Feature](screenshots/search.png)
```

---

## 🧭 Roadmap & Future Enhancements

- 🔍 **Search Bar Integration** (Completed)
- 📱 **Accelerometer Undo Feature** (Completed)
- 👤 **User Profiles** — personalized recommendations.
- 🔔 **Push Notifications** for upcoming events.
- 🌐 **Firebase Realtime Database** for live event updates.
- 🎨 **UI Enhancements** with animations and transitions.

---

## 🧑‍💻 Author

**Shivam Patel**  
🎓 Software Engineering | Ontario Tech University  
📍 Oshawa, Ontario, Canada  
🔗 [GitHub Profile](https://github.com/spatel12696)

---

## 📜 License

This project is developed for **educational purposes** as part of the  
_Mobile Application Development_ course at Ontario Tech University.  
All included media assets are for demonstration only.

---

✨ _“Discover. Experience. Connect. — Spotlight brings local events to life.”_
