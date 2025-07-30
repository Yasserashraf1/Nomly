plugins {
    id("com.android.application") version "8.6.1" apply false
    id("com.android.library") version "8.6.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.9.2" apply false
    id("com.google.dagger.hilt.android") version "2.57" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}