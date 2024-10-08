import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sello.wherethereis4cast"
    compileSdk = 34

    val projectFile = rootProject.file("local.properties")
    val properties = Properties()
    properties.load(FileInputStream(projectFile))

    defaultConfig {
        applicationId = "com.sello.wherethereis4cast"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        resourceConfigurations.add("en")
        buildConfigField(type = "String", name = "apiKeySafe", properties.getProperty("apiKeySafe"))
        testInstrumentationRunner = "com.sello.wherethereis4cast.testrunner.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true

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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.coil.compose)

    //androidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material)

    //Hilt
    implementation(libs.google.dagger.hilt.impl.android)
    implementation(libs.androidx.navigation.compose)
    ksp(libs.google.dagger.hilt.impl.compiler.android)
    implementation(libs.androidx.hilt.navigation.compose)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.converter.gson)

    //Room
    implementation(libs.androidx.room.runtime)

    // To use Kotlin annotation processing tool (kapt) MUST HAVE!
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //location
    implementation(libs.play.services.location)

    //Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.google.dagger.hilt.impl.compiler.android)

    // MockWebServer for API mocking
    testImplementation(libs.mockwebserver)
    androidTestImplementation(libs.mockwebserver)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockserver.netty)
}