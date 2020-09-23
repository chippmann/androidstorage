plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

val androidStorageVersion = properties["ch.hippmann.androidstorage.version"] as String
val androidStorageVersionCode = properties["ch.hippmann.androidstorage.versionCode"] as String

version = "$androidStorageVersion-$androidStorageVersionCode"

android {
    compileSdkVersion(30)
    buildToolsVersion = "30.0.1"

    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = Integer.parseInt(androidStorageVersionCode)
        versionName = "$androidStorageVersion-$androidStorageVersionCode"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val release by getting {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    testOptions {
        unitTests {
            it.isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    implementation(project(":androidstorage-api"))

    implementation("net.danlew:android.joda:2.10.6")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
    testImplementation("org.robolectric:robolectric:4.4")
    testImplementation("androidx.test:core:1.3.0")
}