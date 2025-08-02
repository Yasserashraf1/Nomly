# 🍲 Nomly – Recipe Application (Kotlin Android)
### **Nomly** is a full-featured Android recipe app built with Kotlin. It allows users to explore a variety of meals, view detailed cooking instructions, and manage favorite recipes—all powered by TheMealDB API. The app also supports user authentication, seamless navigation, offline favorites storage, and modern UI with video overlays.

# ✨ Features
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

## 🚀 Getting Started 
  1.Clone the repo: 
   `git clone https://github.com/your-username/nomly-recipe-app.git` 
  2.Open in Android Studio. 
  3.Run the app on an emulator or real device with internet access. 
  4.*Enjoy cooking and coding!*
  
# 👥 Developed By
This project was developed by a student team from the Faculty of Computing and Data Science FCDS, Alexandria University as part of a mobile development course @ ITI:

### 👩‍💻 Eng. Yousef Osama @joee231

### 👨‍💻 Eng. Yasser Ashraf @Yasserashraf1

### 👩‍💻 Eng. Asmaa Hamdy @AsmaaEltalawy

### 👨‍💻 Eng. Mariam Leon @Mariamleon

##### 📧 For collaboration or questions, contact Yasser at: `yasserashraf3142@gmail.com`
##### And Supervised by Eng. Hager Samir @hagersamir123 
