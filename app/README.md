
# 🍽️ Nomly - Recipe App

Nomly is a modern Android recipe app built with Kotlin using the MVVM architecture. It integrates with a remote meal API, allows user authentication (Firebase), and supports saving favorite recipes locally using Room Database.

---

## 🚀 Features

- 🔍 Search recipes by name
- 📝 View detailed recipe information
- ❤️ Save favorite recipes locally
- 👥 User authentication (Login/Register with Firebase)
- 🔄 Auto recipe suggestions via API
- 🧠 Clean MVVM Architecture
- 💾 Offline support for saved favorites

---

## 🧱 Tech Stack

| Layer         | Libraries/Tools                            |
|---------------|---------------------------------------------|
| Language      | Kotlin                                      |
| Architecture  | MVVM (Model-View-ViewModel)                |
| Networking    | Retrofit, Gson                              |
| Local Storage | Room, SQLite                                |
| DI (optional) | Hilt/Koin (can be integrated later)         |
| Authentication| Firebase Auth                               |
| UI            | XML, RecyclerView, Navigation Component     |

---

## 📂 Project Structure

```
com.example.nomly/
│
├── data/
│   ├── local/ (Room DB, DAOs, Entities)
│   ├── remote/ (Retrofit API, DTOs)
│   └── repository/
│
├── domain/
│   └── model/ (Recipe, User, etc.)
│
├── ui/
│   ├── adapter/
│   ├── auth/ (Login, Register, Splash)
│   └── main/ (Home, Favorite, Search, Detail)
│
├── presentation/
│   └── viewmodel/
│
├── utils/ (SharedPrefs, Converters)
└── RecipeApplication.kt
```



