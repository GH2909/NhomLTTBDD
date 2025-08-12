plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.weborderingfood"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.weborderingfood"
        minSdk = 24
        targetSdk = 36
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Thư viện Retrofit để giao tiếp với API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Thư viện Gson để chuyển đổi JSON thành Java Object
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Thư viện OkHttp để quản lý mạng và cookie
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    // Thư viện logging-interceptor để debug
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Thư viện Glide để tải và hiển thị hình ảnh từ URL
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.android.material:material:1.12.0")
}