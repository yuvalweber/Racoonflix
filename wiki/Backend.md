# Backend Documentation

## **Overview**
The backend of this Netflix-like platform is built using **Node.js** with **Express.js** and follows an **MVC (Model-View-Controller)** architecture. It handles user authentication, movie management, recommendation algorithms, and API interactions with the frontend and Android app.

---

## **Tech Stack**
- **Node.js & Express.js**: Web framework for API handling.
- **MongoDB**: NoSQL database for storing users, movies, and categories.
- **JWT Authentication**: Secure authentication using JSON Web Tokens.
- **Multer**: Handles file uploads for movies and user profile pictures.
- **Docker & Docker-Compose**: Deployment and containerization.

---

## **Project Structure**
```
backend/
│── config/            # Configuration files (environment variables)
│── controllers/       # Request handlers (business logic)
│── models/            # Database schemas
│── routes/            # API routes

│── services/          # API Services logic implementation
│── RecommendSystem/   # The all stack of the recommendation system
  │── data/            # contains info about movies and users who watch them
  │── src/             # all the code folder
    │── Commands/      # all the commands files (post, patch...)
    │── Helpers/       # IO base implementation
    │── Interfaces/    # Interfaces used in the recommend system
    │── Socket/        # All the socket logic implementation
    │── Tests/         # Tests implemented in gtest for testing the application
  │── Dockerfile       # Dockerfile for the recommend system to build
  │── DockerfileTests/ # Dockerfile for running the gtests
│── middleware/        # Multer middleWare for upload files
│── uploads/           # saved folder for upload of movies and pictures


│── server.js          # Main entry point
│── package.json       # Dependencies & scripts
```

---

## **Environment Variables**
Create a `.env.local` file inside the `config` folder with the following variables:
```text
NODE_ENV=local
CONNECTION_STRING=mongodb://mongo:27017/netflix # the connection string to connect to the mongo (mongo it's the service name in docker)
SERVER_NAME='server' # the name of the recommendSystem service in docker-compose
SERVER_PORT=8080 # the port of the recommendSystem inside the docker network
PORT=12345 # the port that node will open it's rest api on 
JWT_SECRET= # enter your jwt secret key error
API_SERVER=localhost
API_PORT=8080
```

---

# Command Line Usage (for recommendSystem if connect to it using socket)

## post
This command is for adding new movies to user based on id (for first time when creating).  

##### Arguments:
1) user-id \[**required**\]  
2) list of movies by id \[**required**\]  

```bash
post [userid] [movieid1] [movieid2] …
```

## patch
This command is for adding new movies to user based on id (when user exist).  

##### Arguments:
1) user-id \[**required**\]  
2) list of movies by id \[**required**\]  

```bash
patch [userid] [movieid1] [movieid2] …
```

## get
This command is for getting new movies to watch based on the user and movies similar to the movie he requested.

##### Arguments:
1) user-id \[**required**\]  
2) movie id (movie he liked) \[**required**\] 

```bash
get [userid] [movieid]
```

**on success this command will return a list of recommended movies based on likeability rating**

## help
This command is showing a help menu with list of available commands.

```bash
POST, arguments: [userid] [movieid1] [movieid2] …
PATCH, arguments: [userid] [movieid1] [movieid2] …
GET, arguments: [userid] [movieid]
DELETE, arguments: [userid] [moviedid1] [movieid2] ...
HELP
```

## delete
This command is for deleting movies from user in the recommend system.

##### Arguments:
1) user-id \[**required**\]  
2) movie ids  \[**required**\] 

```bash
delete [userid] [movieid1] [movieid2] ...
```

In order to test the recommendSystem you can run the dockerFileTests and see the output like the following

![](/backend/images/tests-output.png)

---

## **API Endpoints**

### **Authentication**
#### **User Login**
- **Endpoint:** `POST /api/tokens`
- **Description:** Authenticates a user and returns a JWT token.
- **Response:** `{ "token": "<jwt_token>" }`

#### **User Token info**
- **Endpoint:** `GET /api/tokens`
- **Description:** get info about the token user provided (userId, isAdmin)
- **Response:** json object with provided data

#### **User Registration**
- **Endpoint:** `POST /api/users`
- **Description:** Creates a new user account.
- **Response:** `201 Created`

---

### **User Management**
#### **Get User Details**
- **Endpoint:** `GET /api/users/:id`
- **Description:** Retrieves a user's profile information.
- **Response:** User object from MongoDB.

---

### **Movies**
#### **Get Movies preferences for user**
- **Endpoint:** `GET /api/movies`
- **Description:** Fetches movies based on user preferences.
- **Response:** get movie objects from mongo.

#### **Get all available movies**


- **Endpoint:** `GET /api/movies/all`
- **Description:** Fetches all movies.
- **Response:** get movie objects from mongo.

#### **Get specific movie**
- **Endpoint:** `GET /api/movies/:id`
- **Description:** Fetches movie based on movie id.
- **Response:** get movie object from mongo.

#### **Search for a Movie**
- **Endpoint:** `GET /api/movies/search/:query`
- **Description:** Searches for movies based on a query.

#### **Movie Recommendations**
- **Endpoint:** `GET /api/movies/:id/recommend/`
- **Description:** Retrieves recommended movies based on user activity.
- **Response:** get movie objects match recommendation based on movie provided.

#### **Movie Recommendations**
- **Endpoint:** `POST /api/movies/:id/recommend/`
- **Description:** add movie to recommendation system based on id.
- **Response:** indication that it works.

---

### **Categories**
#### **Get All Categories**
- **Endpoint:** `GET /api/categories`
- **Description:** Retrieves a list of all available categories.
- **Response:** get category objects from mongo.

#### **Create a Category**
- **Endpoint:** `POST /api/categories`
- **Description:** Adds a new category.
- **Response:** `201 Created`

#### **Get a specific Category**
- **Endpoint:** `GET /api/categories/:id`
- **Description:** get info about a specific category.
- **Response:** return a specific category from mongo

#### **Edit a Category**
- **Endpoint:** `PATCH /api/categories/:id`
- **Description:** edit info about a specific category.
- **Response:** `201 Created`

#### **Delete a Category**
- **Endpoint:** `DELETE /api/categories/:id`
- **Description:** delete a specific category.
- **Response:** `204 No Content`

---

## API Endpoints examples

### **get user info**
![](/backend/images/userGet.png)

### **create user**
![](/backend/images/userCreate.png)

### **search movies**
![](/backend/images/searchMovies.png)

### **get movies**
![](/backend/images/getMovies.png)

### **delete movie**
![](/backend/images/deleteMovie.png)

### **get Recommendations**
![](/backend/images/getRecommendations.png)

### **get categories**
![](/backend/images/categoriesGet.png)

### **create category**
![](/backend/images/categoriesCreate.png)

### **delete category**
![](/backend/images/deleteCategory.png)

---

## **Testing the Backend**
We provide Python script for testing:
- **`populateMongo.py`**: Adds sample data to MongoDB.


---

## **Next Steps**
- [Frontend Documentation](./Frontend.md) - Web app integration
- [Android App Documentation](./Android-App.md) - API usage in the mobile app

🚀 **Backend setup complete!**