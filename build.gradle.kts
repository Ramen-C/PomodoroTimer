// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.android.library") version "8.1.4" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.16" apply false
}