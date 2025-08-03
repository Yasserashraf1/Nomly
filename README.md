# 🍲 Nomly – Recipe Application (Kotlin Android)
### **Nomly** is a full-featured Android recipe app built with Kotlin. It allows users to explore a variety of meals, view detailed cooking instructions, and manage favorite recipes—all powered by TheMealDB API. The app also supports user authentication, seamless navigation, offline favorites storage, and modern UI with video overlays.

# ✨ Features
* 🔍 Search recipes by name
* 📝 View detailed recipe information
* ❤️ Save favorite recipes locally
* 👥 User authentication (Login/Register )
* 🔄 Auto recipe suggestions via API
* 🧠 Clean MVVM Architecture
* 💾 Offline support for saved favorites

## 🔐 Authentication
  * **Splash screen** with Lottie animation.
  * **Register** and login functionality.
  * Secure user session management using **SharedPreferences**.
## 📱 Fragments
  * **SplashFragment** – Animated splash screen.
  * **LoginFragment & RegisterFragment** – User login and sign-up.
  * **HomeFragmen**t – Displays list of meals from [TheMealDB API](https://www.themealdb.com/api.php).
  * **RecipeDetailFragment** – Shows full recipe details with expandable sections.
  * **FavoriteFragment** – Manage and view your saved recipes.
  * **SearchFragment** – Find meals by keyword or ingredient.
  * **AboutFragment** – Displays developer info and app summary.
## 🔁 Navigation
  * Navigation Component for structured and safe fragment navigation.
  * Bottom navigation bar for Home, Favorites, Search, and About.
  * Auth flow handled via AuthActivity and main app flow via RecipeActivity.
  * Prevents returning to login/register after authentication.
## 🛠 Local Database
  * **Room** used to store and retrieve favorite recipes offline.
## 🎥 Video Overlay
  * YouTube-style floating video player integrated into recipe detail view.
## 🧭 Intelligent Splash Logic
  * Checks login status on launch.
  * First-time users are routed to Register/Login.
  * Returning users skip directly to the Home screen.
## 📜 Options Menu
  * `Sign Out` to clear session and return to AuthActivity.
  * `About the Creator` to navigate to AboutFragment.

# 🧱 Tech Stack

| Layer           | Libraries/Tools                                   |
|------------------|---------------------------------------------------|
| Language         | Kotlin                                            |
| Architecture     | MVVM (Model-View-ViewModel)                       |
| Networking       | Retrofit, Gson                                    |
| Local Storage    | Room, SQLite                                      |
| Authentication   | Auth (SharedPreferences for login/session)        |
| UI               | XML, RecyclerView, Navigation Component           |

# 📂 Project Structure
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

## 🚀 Getting Started 
  * Clone the repo:
    `git clone https://github.com/your-username/nomly-recipe-app.git` 
  * Open in Android Studio. 
  * Run the app on an emulator or real device with internet access. 
  * *Enjoy cooking and coding!*
  
# 👥 Developed By
This project was developed by a student team from the Faculty of Computing and Data Science [FCDS](https://share.google/Zqfmvs1b6bnfHeTzL), Alexandria University as part of a mobile development course @ [ITI](https://iti.gov.eg/home):

### 👩‍💻 Eng. Yousef Osama @joee231

### 👨‍💻 Eng. Yasser Ashraf @Yasserashraf1

### 👩‍💻 Eng. Asmaa Hamdy @AsmaaEltalawy

### 👨‍💻 Eng. Mariam Leon @Mariamleon

### And Supervised by 
#### Eng. Hager Samir @hagersamir123 

##### 📧 For collaboration or questions, contact Yasser at: `yasserashraf3142@gmail.com`

