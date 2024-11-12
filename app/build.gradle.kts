plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.dicoding.asclepius"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dicoding.asclepius"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        mlModelBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0") // or 1.15.0 if compatible
    implementation("androidx.appcompat:appcompat:1.7.0") // or 1.7.0 if compatible
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0") // or 2.2.0 if compatible
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4")
    implementation("androidx.camera:camera-core:1.4.0") // or 1.4.0 if compatible
    implementation("androidx.camera:camera-lifecycle:1.4.0") // or 1.4.0 if compatible
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("com.github.yalantis:ucrop:2.2.9")
    implementation("de.hdodenhof:circleimageview:3.1.0")
}
