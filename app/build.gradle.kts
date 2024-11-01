plugins {
    id("com.android.application")
    kotlin("android")
}

val versionMajor = 1
val versionMinor = 1
val versionPatch = 0

android {
    compileSdk = 33
    defaultConfig {
        applicationId = "fr.smarquis.applinks"
        namespace = "fr.smarquis.applinks"
        minSdk = 19
        targetSdk = 33
        versionCode = versionMajor * 1000000 + versionMinor * 10000 + versionPatch
        versionName = "$versionMajor.$versionMinor.$versionPatch"
    }
    buildFeatures.viewBinding = true
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFile(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.transition:transition-ktx:1.5.1")

    implementation(platform("com.google.firebase:firebase-bom:29.0.4"))
    implementation("com.google.firebase:firebase-dynamic-links-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.android.installreferrer:installreferrer:2.2")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")

    testImplementation("junit:junit:4.13.2")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

apply(plugin = "com.google.gms.google-services")
