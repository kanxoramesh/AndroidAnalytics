## About The Project
Analytics SDK for Android. 
* Persistence cache
* Sync with server
### Built With
* Room
* OkHttp
* Jetpack Compose
<!-- GETTING STARTED -->
## Getting Started
  ### Prerequisites
* Internet permission on App manifest 
  ```sh
      <uses-permission android:name="android.permission.INTERNET" />
  ```
  
#### SDK Configure with Remote URL
Analytics SDK is hosted on Bitbucket. You can find more about Android SDK Gradle Artifact deployments in my previous post https://kanxoramesh.medium.com/bitbucket-as-maven-repository-888c7f72cfb7 
* Provide Repo accesstoken in local.properties file
  ```
  bitbucket.token=*******token here*****

  ```
* On Project level setting.gradle.kts
  ```
   repositories {
        
        maven {
            url = uri("url")

            authentication {
                create<HttpHeaderAuthentication>("header")
            }
            credentials(HttpHeaderCredentials::class) {
                name = "Authorization"
                value = "Bearer ${localProperties.getProperty("bitbucket.token")}"
            }
        }
    }
    
  ```
* On App level
  ```
    implementation 'com.analytics.analytics_android:analytics:1.0.1'//

  ```
  
-------------OR----------------------
#### SDK Configure with Local Path
  ```
implementation(project(":analytics-android"))
    
  ```
  ### Installation
1. Initialize the SDK on Application Class
    ```sh
      AndroidAnalytics.initialize(
          AndroidAnalytics.Builder(this)
              .setStorage(RoomAnalyticsStorage(this)) // Local Cache Storage
              .setLogLevel(LogLevel.INFO) // Log level
              .maxSessionPoolCount(2) // Max number of sessions to store in cache; after this, network sync will push data to server
              .setNetworkSynchronizer(
                  AndroidAnalytics.OkHttpNetworkConnectionBuilder("http://10.0.2.2:8080/network") // Network synchronizer to push cached data to network
                      .method(HttpMethod.POST) // HTTP method
                      .timeout(5) // Request timeout duration in seconds
                      .build()
              )
      )

   ```
2. Start Session before log event
    ```sh
    AndroidAnalytics.getInstance().startSession()
    ```
3. Add some Events. Ensure wrap with Try/Catch. Session Must start to log Events
   
   ```sh
     try {
       AndroidAnalytics.getInstance()
         .addEvent("Event $event", mapOf("Tap $event" to "main"))
       } catch (e: Exception) {
         Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
       }
    ```
4. End Session
   
   ```sh
    AndroidAnalytics.getInstance().endSession()
    ```
<!-- CONTACT -->
## Contact

Ramesh Pokhrel - [@medium](https://kanxoramesh.medium.com) - pokhrelramesh1996@gmail.com

