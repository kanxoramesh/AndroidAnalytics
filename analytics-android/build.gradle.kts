import com.android.build.gradle.internal.scope.publishBuildArtifacts

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id ("maven-publish")

}

android {
    namespace = "com.analytics.analytics_android"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.gson)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    kapt(libs.androidx.room.compiler)
    testImplementation (libs.assertj.core)
    testImplementation (libs.kotlin.stdlib)
    testImplementation (libs.kotlin.test.junit)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.inline)// Optional: For inline mocking
    implementation (libs.okhttp)
    testImplementation (libs.mockito.kotlin)

}

publishing {
    repositories {
        maven {
            name = "myrepo"
            url = uri("file://${project.projectDir.parent}/analytics-library/") // Adjusted to point to the desired directory
        }
    }

    publications {
        create<MavenPublication>("maven") {
            pom {
                groupId = "com.analytics.analytics_android" //your library package name e.g. com.remote.control
                version = "1.0.1" //library version 1.0.0
                artifactId = "analytics" //your library artifact id. eg. dispatcher
                artifact(file("$buildDir/outputs/aar/${project.getName()}-release.aar")) // Replace "aar location" with the actual location of your AAR file
                name = "Android Analytics"
                description = "Analytics for Android"
                url = "Bitbucket.org"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "kanxoramesh"
                        name = "Ramesh Pokhrel"
                        email = "pokhrelramesh1996@gmail.com"
                    }
                }
                withXml {
                    val dependenciesNode = asNode().appendNode("dependencies")
                        configurations["implementation"].allDependencies.forEach {
                            val dependencyNode = dependenciesNode.appendNode("dependency")
                            dependencyNode.appendNode("groupId", it.group)
                            dependencyNode.appendNode("artifactId", it.name)
                            dependencyNode.appendNode("version", it.version)
                        }
                }
            }
        }
    }
}