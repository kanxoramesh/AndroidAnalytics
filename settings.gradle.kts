import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

val localProperties = Properties().apply {
    load(File("local.properties").inputStream())
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.bitbucket.org/2.0/repositories/kanxoramesh/android-analytics/src/master")
            credentials(HttpHeaderCredentials::class) {
                name = "Authorization"
                value = "Bearer ${localProperties.getProperty("bitbucket.token")}"
            }
        }
    }
}

rootProject.name = "Analytics"
include(":app")
//include(":analytics-android")
