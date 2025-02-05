# Getting Started

## **Prerequisites**
Before setting up the project, ensure you have the following installed on your system:

### **General Requirements**
- **Docker & Docker-Compose** (For easy deployment)
- **Git** (For cloning the repository)


### **Android App Requirements**
- **Android Studio** (Latest version)
- **Java 8+**
- **Gradle** (Automatically managed by Android Studio)

---

## **Installation & Setup**
### **Step 1: Clone the Repository**
```bash
 git clone https://github.com/yuvalweber/Racoonflix.git
 cd Racoonflix
```

### **Step 2: Setup the Backend**
1. Navigate to the backend directory:
    ```bash
    cd backend
    mkdir config

    ```

2. Create a `.env.local` file inside the `config` folder with the following variables:
    ```bash
    NODE_ENV=local
    CONNECTION_STRING=mongodb://mongo:27017/netflix # the connection string to connect to the mongo (mongo it's the service name in docker)
    SERVER_NAME='server' # the name of the recommendSystem service in docker-compose
    SERVER_PORT=8080 # the port of the recommendSystem inside the docker network
    PORT=12345 # the port that node will open it's rest api on 
    JWT_SECRET= # enter your jwt secret key error
    ```
---

### **Step 3: Setup the Android App**
1. Open Android Studio and select **Open an Existing Project**.
2. Navigate to the `android` folder inside the cloned repository.
3. Sync Gradle and ensure all dependencies are installed.
4. Connect a physical device or use an emulator.
5. Click **Run** to launch the app.

---

## **Running the Project with Docker-Compose**
To run the project using Docker-Compose:
```bash
docker-compose up --build -d
```

## **Running the Project with Docker**
To run the project using Docker:

first create a docker network using
```bash
docker network create netflix
```

and then we wiil attach each of them to the network
1) <ins>The Netflix recommend system:</ins>  

in order to build the recommend system itself run the following command:
```bash
docker build -f ./backend/RecommendSystem/Dockerfile -t "netflix:1.0.1" ./backend/RecommendSystem
```

now you can run the recommendationSystem and pass also ip and port to listen on (if not provided we will give default one)
```bash
docker run --net="netflix" --rm -v "$(pwd)/backend/RecommendSystem/data/:/usr/src/app/data/" --name "server" -dit netflix:1.0.1 "<ip>" "<port>"
```

<br>

2) <ins> The mongo server </ins>
first create volume that will store the mongo data:
```bash
docker volume create mongo_data
```
in order to create a mongo server just run the following command : 
```bash
docker run --net="netflix" --rm -v "mongo_data:/data/db" -dit --name "mongo" mongo:latest
```

3) <ins>The Netflix web service:</ins>  

in order to build the netflix web service run the following command:
```bash
docker build -f ./backend/Dockerfile -t "netflix-web-service:1.0.1" ./backend
```

now you can run container like this

**if don't know ip use the servie name which in our case is** : <b>server</b>


```bash
docker run --net="netflix" -p "8080:12345" --name "web-service" -e "NODE_ENV=local" -dit netflix-web-service:1.0.1
```

4) The netflix react app
in order to build the netflix react app run the following command:
```bash
docker build -f ./frontend/Dockerfile -t "netflix-react-app:1.0.1" ./frontend
```

now run the container like this
```bash
docker run --net="netflix" -p "3000:3000" --name "frontend" -e "API_SERVER=web-service" -e "API_PORT=12345" -dit netflix-react-app:1.0.1
```

Once complete, the API should be available at `http://localhost:8080`, and the frontend should be accessible at `http://localhost:3000`.

---

If you want to enter dummy data you can do so using our python script in the backend like this
```bash
python ./backend/populateMongo.py
```

---

## **Next Steps**
- [Backend Documentation](./Backend) - API endpoints & database structure
- [Frontend Documentation](./Frontend) - Web app details
- [Android App Documentation](./Android-App) - Mobile app details

🚀 **You're all set! Enjoy building your Netflix-like streaming platform!**