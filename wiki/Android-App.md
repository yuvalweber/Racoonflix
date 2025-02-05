# Android App Documentation

## **Overview**

The Android app of this Racoonflix is built using **Java** with **Android Studio**. It provides a mobile-friendly interface for browsing movies and interacting with the recommendation system.

---

## **Tech Stack**

- **Java**: Primary programming language for the Android app.
- **Retrofit**: HTTP client for making API requests.

- **Room Database**: Local database for caching data.
- **LiveData & ViewModel**: Implements reactive programming for UI updates.
---

## **Project Structure**

```
android/
│── app/src/main/java/com/example/netflix/


│   │── adapters/          # RecyclerView adapters
│   │── api/               # Retrofit API client
│   │── dao/               # CRUD for database entities
│   │── database/          # Room database setup
│   │── entities/          # Data models for ROOM responses
│   │── fragments/         # fragments for movie and category management
│   │── models/            # Data models for API responses
│   │── network/           # client for retrofit
│   │── repository/        # Manages API calls & local database
│   │── viewmodels/        # Store all the view models

│── res/
│   │── drawable/          # Icons and images
│   │── layout/            # XML layout files
│   │── menu/              # represent the main menu
│   │── values/            # Strings, colors, and dimensions
│── AndroidManifest.xml    # Application configuration
│── build.gradle           # Gradle build scripts
```
---

## **Navigation Structure**

The app follows a structured navigation pattern:

```
MainActivity                 # Entry point with bottom navigation
│── LoginActivity            # User login page
│── RegisterActivity         # User registration page
│── MovieDetailsActivity     # View specific movie details
│── MoviePlayerActivity      # Stream movies
│── ConnectedHomeActivity    # Logged-in home page with recommendations
│── ManagementActivity       # Admin panel for content management


```
---

## **Examples & Screenshots**
To visually showcase the platform, we added images for the app:

- **Generic Home Page:**  
  ![Generic Home Page](/images/android_home.png)

- **Login Page:**  
  ![Login Page](/images/android_login.png)

- **Registration Page:**  
  ![Registration Page](/images/android_register.png)

- **Connected Home Page:**  
  ![Connected Home Page](/images/android_connected.png)

- **Seen movies in Home Page:**  
  ![Connected Home Page Seen](/images/androidSeenMovies.png)

- **All Categories Fragment:**  
  ![All Categories Page](/images/android_all_categories.png)

- **Movie Info Page with Recommendations:**  
  ![Movie Info Page](/images/android_movie_info.png)

- **Light/Dark mode:**  
  ![Light Mode](/images/android_light_mode.png)

- **Management Page:**  
  ![Management Page](/images/android_management.png)

- **Search Content:**  
  ![Search Page](/images/android_search.png)

- **Android Menu:**  
  ![User Profile](/images/android_menu.png)

- **Movie Player Page:**  
  ![Watch Page](/images/android_watch.png)

---

## **How to Run guide:**

first of all you can populate the data using our code and the backend will have dummy data
```bash
python ./backend/populateMongo.py
```

There are many ways to run the app we will show 2 ways:

1) using adb reverse tcp (from cmd windows):

you need to connect you android to your pc and activate usb debugging on the phone so he can be connected to android studio.
Then from cmd (windows) run:

```bash
%localappdata%\Android\Sdk\platform-tools\adb.exe reverse tcp:8080 tcp:8080
```

it will make your android to connect to localhost:8080 of the pc (which is where our api is)
no you can run the android app from android studio and interact with the api


2) using your device on the same wifi:

first find your pc's wifi.
using cmd write the following command:
```bash
ipconfig
```
and under wifi adpater look for the IPV4 address (save it in some notepad or something).

now go to our android app (open it in android studio) and go to the folder network
and open the RetrofitInstance.java file and change the BASE_URL to your ip.

now your app should work and everything should be wonderful 😄 
---

🚀 **Android app setup complete!**