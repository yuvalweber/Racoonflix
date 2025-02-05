# Frontend Documentation

## **Overview**

The frontend of this Racoonflix is built using **React.js**. It provides an intuitive and responsive user interface for streaming movies, browsing categories and interacting with the recommendation system.

---

## **Tech Stack**

- **React.js**: Component-based JavaScript library for building the UI.
- **React Router**: Client-side navigation between pages.
- **Axios**: Handles API requests to communicate with the backend.
- **Context API & useAuth**: Authentication management.

- **ThemeContext**: Dark and light mode UI styling.

---

## **Project Structure**
```
frontend/
│── public/         # Static files (index.html, images, icons)
│── src/
│   │── Authentication/    # Auth context & protected routes
│   │── components/        # Reusable React components
│   │── pages/             # Page-level components

│   │── App.js             # Main application entry point
│   │── index.js           # React DOM rendering
│── package.json         # Dependencies & scripts

```

---

## **Routing Structure**
The application follows a client-side routing pattern using React Router:
```
/	              # Home Page
/login		      # User Login Page
/signup	              # User Registration Page
/movieInfo/:id        # View specific movie details
/play                 # get a state id of specific movie to watch it's content
/connected	      # Logged-in Home Page with recommendations
/management	      # Admin panel for content management
/search               # let you search a movie (based on id)
/allMovies	      # Browse all movies and categproies
```

---

## **State Management & Authentication**
The authentication and global state is handled using **Context API**.

### **Authentication (useAuth)**
The authentication context provides user data and session handling.

```
Authentication/
│── AuthContext.js      # Manages authentication state
│── ProtectedRoute.js   # Redirects unauthorized users
```

---
## **Login And Registration**

first we will show how to register to our app and that the info is shown in the mongo

![Register React](/images/registerExampleReact.png)

now we will see it exist in mongo

![Register Works](/images/mongoRegAnd.png)

now we can login happily :)

---
## **Movie Create edit and delete**

all this is only relevant to admins but let's see how it works.

First create a new movie

![](/images/createMovieReact.png)

and now see it works

![](/images/movieExistAfterCreateReact.png)

and we can see it's playing

![](/images/moviePlaying.png)

now let's edit the movie and change it's title

![](/images/editMovie.png)

and see it actually works

![](/images/editMovieWorks.png)


and lastly let's delete the movie

![](/images/deleteMovie.png)

and validate it worked :)

![](/images/deleteMovieWorks.png)

---

## **Examples**
To visually showcase the platform, we added an images for showing the app 

- **Generic Home Page:** 
![Generic Home Page](/images/homePage.png)

- **Login Page:** 
![Login Page](/images/login.png)

- **Registration Page:** 
![Registration Page](/images/register.png)

- **All Categories Page:** 
![All Categories Page](/images/allCategories.png)

- **Connected Home Page:** 
![Connected Home Page](/images/connected.png)

- **React seen movies:** 
![Connected Home Page seen](/images/seenMoviesReact.png)

- **info regarding movies + recommendations:** 
![Movie Info Page](/images/infoMovie.png)

- **Light/Dark mode:** 
![Light Mode](/images/lightMode.png)

- **Management Page:** 
![Management Page](/images/managementPage.png)

- **Search Page:** 
![Search Page](/images/searchPage.png)

- **user Profile and info:** 
![User Profile](/images/userProfile.png)

- **watch movies page:** 
![Watch Page](/images/watchMoviePage.png)

---


## **Next Steps**
- [Android App Documentation](./Android-App) - Mobile app interaction with APIs

🚀 **Frontend setup complete!**