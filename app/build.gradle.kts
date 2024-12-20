import com.android.build.api.variant.BuildConfigField

println("API Key from gradle.properties: ${project.findProperty("apikey")}")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
}

androidComponents {
    val key = project.findProperty("apikey")?.toString() ?: error(
        "You should add apiKey into gradle.properties"
    )

    onVariants { variant ->
        variant.buildConfigFields.put(
            "WEATHER_API_KEY",
            BuildConfigField(
                "String",
                "\"$key\"",
                "API key for accessing the sevice"
            )
        )
    }
}

android {
    namespace = "com.faist.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.faist.weatherapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.compose.material)

    //MVI Kotlin
    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.coroutines)
    implementation(libs.mvikotlin.logging)

    //Decompose
    implementation(libs.decompose.core)
    implementation(libs.decompose.jetpack)

    //Room
    implementation(libs.room.core)
    ksp(libs.room.compiler)

    //Dagger
    implementation(libs.dagger.core)
    ksp(libs.dagger.compiler)

    //Glide-compose
    implementation(libs.glide.compose)

    //Retrofit
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}