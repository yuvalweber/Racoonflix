import requests
import random

categoriesIds = []
user11IHeader = {}
movieIds = []

def create_user_and_login():
    url = 'http://localhost:8080/api/users'
    json_data = {'firstName': 'user11', 'lastName': 'last', 'userName': "nick-11", 'email': "email-11",
                     'password': 'password', 'profilePicture': 'https://www.google.com'}
    r = requests.post(url, json=json_data)
    if r.status_code == 201:
        url = 'http://localhost:8080/api/tokens'
        json_data = {'userName': 'nick-11', 'password': 'password'}
        r = requests.post(url, json=json_data)
        global user11IHeader
        user11IHeader = {'Authorization': f"bearer {r.json()['token']}"}
        
def create_categories():
    url = 'http://localhost:8080/api/categories'
    categories = ['Science', 'Technology', 'Arts', 'Sports', 'Health', 'Business', 'Entertainment']
    not_promoted_category = "Kupershtock"
    for category in categories:
        json_data = {'name': category, 'promoted': True}
        r = requests.post(url, json=json_data, headers=user11IHeader)
    json_data = {'name': not_promoted_category, 'promoted': False}
    r = requests.post(url, json=json_data, headers=user11IHeader)
    res = requests.get(url, headers=user11IHeader)
    global categoriesIds
    for category in res.json():
        categoriesIds.append(category['_id'])

def create_movies():
    url = 'http://localhost:8080/api/movies'
    movies = set(['The Matrix', 'The Godfather', 'The Shawshank Redemption', 'The Dark Knight', 'Pulp Fiction', 'Fight Club', 'Forrest Gump', 'Inception', 'The Lord of the Rings', 'The Matrix Reloaded', 'The Matrix Revolutions', 'The Godfather Part II', 'The Godfather Part III', 'The Shawshank Redemption', 'The Dark Knight Rises', 'The Dark Knight Returns', 'Pulp Fiction', 'Fight Club', 'Forrest Gump', 'Inception', 'The Lord of the Rings: The Fellowship of the Ring', 'The Lord of the Rings: The Two Towers', 'The Lord of the Rings: The Return of the King',
    'The King', 'Iron Man', 'The Incredible Hulk', 'Iron Man 2', 'Thor', 'Captain America: The First Avenger', 'The Avengers', 'Iron Man 3', 'Thor: The Dark World', 'Captain America: The Winter Soldier', 'Guardians of the Galaxy', 'Avengers: Age of Ultron', 'Ant-Man', 'Captain America: Civil War', 'Doctor Strange', 'Guardians of the Galaxy Vol. 2', 'Spider-Man: Homecoming', 'Thor: Ragnarok', 'Black Panther', 'Avengers: Infinity War', 'Ant-Man and the Wasp', 'Captain Marvel', 'Avengers: Endgame', 'Spider-Man: Far From Home', 'Black Widow', 'Shang-Chi and the Legend of the Ten Rings', 'Eternals', 'Spider-Man: No Way Home', 'Doctor Strange in the Multiverse of Madness', 'Thor: Love and Thunder', 'Black Panther: Wakanda Forever'])
    i = 0
    j = 0
    for movie in movies:
        random_category = random.choice(categoriesIds)
        json_data = {'title': movie, 'year': 1999, 'category': [random_category], 'director': 'Director',
                     'duration': 120, 'image': f"https://picsum.photos/400/3{i}{j}", 'trailer': 'http://localhost:3000/movies/video_360.mp4'}
        if j == 9:
            j = 0
            i += 1
        else:
            j += 1
        r =  requests.post(url, json=json_data,headers=user11IHeader)
        global movieIds
        movieIds.append(r.headers['Location'].split('/')[-1])

def get_expected_movies_from_list(index):
    match index:
        case 0:
            seen_movies = [{'movieId': movieIds[0], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[1], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[2], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[3], 'watchedAt': '2020-12-12'}]
        case 1:
            seen_movies = [{'movieId': movieIds[1], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[2], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[4], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[5], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[6], 'watchedAt': '2020-12-12'}]
        case 2:
            seen_movies = [{'movieId': movieIds[0], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[4], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[5], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[7], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[8], 'watchedAt': '2020-12-12'}]
        case 3:
            seen_movies = [{'movieId': movieIds[1], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[5], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[6], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[7], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[9], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[10], 'watchedAt': '2020-12-12'}]
        case 4:
            seen_movies = [{'movieId': movieIds[0], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[2], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[3], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[5], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[8], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[11], 'watchedAt': '2020-12-12'}]
        case 5:
            seen_movies = [{'movieId': movieIds[0], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[3], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[4], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[10], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[11], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[12], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[13], 'watchedAt': '2020-12-12'}]
        case 6:
            seen_movies = [{'movieId': movieIds[2], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[5], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[6], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[7], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[8], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[9], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[10], 'watchedAt': '2020-12-12'}]
        case 7:
            seen_movies = [{'movieId': movieIds[1], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[4], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[5], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[6], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[9], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[11], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[14], 'watchedAt': '2020-12-12'}]
        case 8:
            seen_movies = [{'movieId': movieIds[0], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[3], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[5], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[7], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[12], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[13], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[15], 'watchedAt': '2020-12-12'}]
        case 9:
            seen_movies = [{'movieId': movieIds[0], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[2], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[5], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[6], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[7], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[9], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[10], 'watchedAt': '2020-12-12'}, {'movieId': movieIds[16], 'watchedAt': '2020-12-12'}]
        case _:
            return []
    return seen_movies
    

def create_users():
    url = 'http://localhost:8080/api/users'
    users = ['user1', 'user2', 'user3', 'user4', 'user5', 'user6', 'user7', 'user8', 'user9', 'user10']
    for i in range(len(users)):
        seen_movies = get_expected_movies_from_list(i)
        json_data = {'firstName': users[i], 'lastName': 'last', 'userName': f"nick-{i}", 'email': f"email-{i}",
                     'password': 'password', 'profilePicture': '../frontend/public/images/icon.png', 'seenMovies': seen_movies, 'isAdmin': True}
        requests.post(url, json=json_data, headers=user11IHeader)

def get_recommendations():
    url = 'http://localhost:8080/api/movies/104/recommendations'
    r = requests.get(url, headers=user11IHeader)

create_user_and_login()
create_categories()
create_movies()
create_users()