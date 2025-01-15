import requests
from pymongo import MongoClient

tokenObject = {"userName": "nick-1", "password": "password"}
userObjectHeader = {}
userObjectCreate = {'firstName': "user11", 'lastName': 'last', 'userName': "nick-12", 'email': "email-12",
                     'password': 'password', 'profilePicture': 'https://www.google.com', 'seenMovies': []}

categoryObjectCreate = {'name': 'category1'}
movieObjectCreate = {'title': 'Kupershtock movie', 'year': 1999, 'category': [], 'director': 'Director',
                     'duration': 120, 'image': 'https://www.google.com', 'trailer': 'https://www.google.com'}

client = MongoClient('localhost', 27017)
db = client['netflix']
users = db['users']
movies = db['movies']
categories = db['categories']


####### TOKENS TEST ############################
def token_login(userObject):
    url = 'http://localhost:8080/api/tokens'
    return requests.post(url, json=userObject)

# no username or password field test
def test_token_no_username_or_password_field():
    user = {"userName": "nick-11"}
    r = token_login(user)
    assert r.status_code == 400
    assert r.json()['errors'] == 'Missing required fields'

# username not exist test
def test_token_username_not_exist():
    user = {"userName": "nick-12", "password": "password"}
    r = token_login(user)
    assert r.status_code == 404
    assert r.json()['errors'] ==  'User not found'

# username wrong password test
def test_token_username_wrong_password():
    user = {"userName": "nick-11", "password": "password1"}
    r = token_login(user)
    assert r.status_code == 404
    assert r.json()['errors'] ==  'User not found'

# successful login test
def test_token_successful_login():
    r = token_login(tokenObject)
    assert r.status_code == 200
    assert r.json()['x-user'] != None
    userObjectHeader['x-user'] = r.json()['x-user']
    

###############################################


####### USERS TEST ###########################

def create_user(userObject, tokenHeader):
    url = 'http://localhost:8080/api/users'
    return requests.post(url, json=userObject, headers=tokenHeader)

def get_user(id):
    url = 'http://localhost:8080/api/users/' + str(id)
    return requests.get(url, headers=userObjectHeader)

# missing required key test
def test_user_missing_required_key():
    # create copy of userObjectCreate
    user = userObjectCreate.copy()
    user.pop('userName')
    r = create_user(user, userObjectHeader)
    assert r.status_code == 400
    assert r.json()['errors'] == 'Missing required keys in the request body'

# invalid key test
def test_user_invalid_key():
    # create copy of userObjectCreate
    user = userObjectCreate.copy()
    user['invalid'] = 'invalid'
    r = create_user(user, userObjectHeader)
    assert r.status_code == 400
    assert r.json()['errors'] == 'Data provided contains invalid keys'

# not unique username test
def test_user_not_unique_username():
    # create copy of userObjectCreate
    user = userObjectCreate.copy()
    user['userName'] = 'nick-11'
    r = create_user(user, userObjectHeader)
    assert r.status_code == 404
    assert r.json()['errors'] == 'one of the movies does not exist or userName is not unique'

# one of the movies in the seenMovies list does not exist test
def test_user_seen_movies_not_exist():
    # create copy of userObjectCreate
    user = userObjectCreate.copy()
    user['seenMovies'] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    r = create_user(user, userObjectHeader)
    assert r.status_code == 404
    assert r.json()['errors'] == 'one of the movies does not exist or userName is not unique'

# successful create user test
def test_user_successful_create():
    user = userObjectCreate.copy()
    seen_movies = str(movies.find_one()['_id'])
    user['seenMovies'].append({'movieId': seen_movies, 'watchedAt': '2020-12-12'})
    r = create_user(user, userObjectHeader)
    assert r.status_code == 201
    assert r.headers['Location'] != None

# failed get user test
def test_user_failed_get():
    r = get_user(100)
    assert r.status_code == 404
    assert r.json()['errors'] == 'User not found'

# successful get user test
def test_user_sucessful_get():
    userId = str(users.find_one()['_id'])
    r = get_user(userId)
    assert r.status_code == 200
    assert r.json()['_id'] == userId

###############################################

####### CATEGORIES TEST ###########################
def get_categories(id=None):
    if id:
        url = 'http://localhost:8080/api/categories/' + str(id)
    else:
        url = 'http://localhost:8080/api/categories'
    return requests.get(url, headers=userObjectHeader)

def create_category(category):
    url = 'http://localhost:8080/api/categories'
    return requests.post(url, json=category, headers=userObjectHeader)

def delete_category(id):
    url = 'http://localhost:8080/api/categories/' + str(id)
    return requests.delete(url, headers=userObjectHeader)

def patch_category(id, category):
    url = 'http://localhost:8080/api/categories/' + str(id)
    return requests.patch(url, json=category, headers=userObjectHeader)

# test get all categories
def test_categories_get_all():
    r = get_categories()
    assert r.status_code == 200

# test failed get category by id
def test_categories_get_by_id_failed():
    r = get_categories(100)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Category not found'

# test get category by id
def test_categories_get_by_id():
    categoryId = str(categories.find_one()['_id'])
    r = get_categories(categoryId)
    assert r.status_code == 200
    assert r.json()['_id'] == categoryId

# test create category
def test_categories_invalid_data():
    category = categoryObjectCreate.copy()
    category.pop('name')
    r = create_category(category)
    assert r.status_code == 400
    assert r.json()['errors'] == 'data provided is not valid'

# test create category
def test_categories_invalid_key():
    category = categoryObjectCreate.copy()
    category['invalid'] = 'invalid'
    r = create_category(category)
    assert r.status_code == 400
    assert r.json()['errors'] == 'Data provided contains invalid keys'

# test not unique category name
def test_categories_not_unique_name():
    category = categoryObjectCreate.copy()
    category['name'] = 'Science'
    r = create_category(category)
    assert r.status_code == 404
    assert r.json()['errors'] == 'one of the movies does not exist or title is not unique'

# test one of the movies not exist
def test_categories_one_of_the_movies_not_exist():
    category = categoryObjectCreate.copy()
    category['movies'] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    r = create_category(category)
    assert r.status_code == 404
    assert r.json()['errors'] == 'one of the movies does not exist or title is not unique'

# test successful create category
def test_categories_successful_create():
    r = create_category(categoryObjectCreate)
    assert r.status_code == 201
    assert r.headers['Location'] != None

# test update category invalid data
def test_categories_update_invalid_data():
    category = categoryObjectCreate.copy()
    category['invalid'] = 'invalid'
    r = patch_category(1, category)
    assert r.status_code == 400
    assert r.json()['errors'] == 'Data provided contains invalid keys'

# test update category id not exist
def test_categories_update_id_not_exist():
    category = categoryObjectCreate.copy()
    r = patch_category(100, category)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Category not found'

# test update category successful
def test_categories_update_successful():
    categoryId = str(categories.find_one({'name':'Technology'})['_id'])
    category = categoryObjectCreate.copy()
    category['name'] = 'Kupershtock'
    r = patch_category(categoryId, category)
    assert r.status_code == 204

# test delete category id not exist
def test_categories_delete_id_not_exist():
    r = delete_category(100)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Category not found'

# test delete category successful
def test_categories_delete_successful():
    categoryId = str(categories.find_one({'name':'Arts'})['_id'])
    r = delete_category(categoryId)
    assert r.status_code == 204

###############################################

####### MOVIES TEST ###########################
def get_movies(id=None):
    if id:
        url = 'http://localhost:8080/api/movies/' + str(id)
    else:
        url = 'http://localhost:8080/api/movies'
    return requests.get(url, headers=userObjectHeader)

def create_movie(movie):
    url = 'http://localhost:8080/api/movies'
    return requests.post(url, json=movie, headers=userObjectHeader)

def delete_movie(id):
    url = 'http://localhost:8080/api/movies/' + str(id)
    return requests.delete(url, headers=userObjectHeader)

def put_movie(id, movie):
    url = 'http://localhost:8080/api/movies/' + str(id)
    return requests.put(url, json=movie, headers=userObjectHeader)

def search_movies(query):
    url = 'http://localhost:8080/api/movies/search/' + query
    return requests.get(url, headers=userObjectHeader)

def get_movies_recommendation(id=None):
    if id == None:
        url = 'http://localhost:8080/api/movies/recommend'
    else:
        url = 'http://localhost:8080/api/movies/' + str(id) + '/recommend'
    return requests.get(url, headers=userObjectHeader)

def add_movie_to_recommendation(id=None):
    url = 'http://localhost:8080/api/movies/' + str(id) + '/recommend'
    return requests.post(url, headers=userObjectHeader)

# test get all movies
def test_movies_get_all():
    r = get_movies()
    assert r.status_code == 200

# test failed get movie by id
def test_movies_get_by_id_failed():
    r = get_movies(100)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Movie not found'

# test get movie by id
def test_movies_get_by_id():
    movieId = str(movies.find_one()['_id'])
    r = get_movies(movieId)
    assert r.status_code == 200
    assert r.json()['_id'] == movieId

# test create movie failed required key
def test_movies_create_failed_required_key():
    movie = movieObjectCreate.copy()
    movie.pop('title')
    r = create_movie(movie)
    assert r.status_code == 400
    assert r.json()['errors'] == 'Missing required keys in the request body'

# test create movie invalid key
def test_movies_create_invalid_key():
    movie = movieObjectCreate.copy()
    movie['invalid'] = 'invalid'
    r = create_movie(movie)
    assert r.status_code == 400
    assert r.json()['errors'] == 'Data provided contains invalid keys'

# test create movie not unique title
def test_movies_create_not_unique_title():
    movie = movieObjectCreate.copy()
    movie['title'] = 'The Matrix'
    r = create_movie(movie)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Category not found or movie name not unique'

# test create movie non exist category
def test_movies_create_non_exist_category():
    movie = movieObjectCreate.copy()
    movie['category'] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    r = create_movie(movie)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Category not found or movie name not unique'

# test create movie successful
def test_movies_create_successful():
    categoryId = str(categories.find_one()['_id'])
    movie = movieObjectCreate.copy()
    movie['category'] = [categoryId]
    r = create_movie(movie)
    assert r.status_code == 201
    assert r.headers['Location'] != None

# test update movie invalid key
def test_movies_update_invalid_key():
    movie = movieObjectCreate.copy()
    movie['invalid'] = 'invalid'
    r = put_movie(1, movie)
    assert r.status_code == 400
    assert r.json()['errors'] == 'Data provided contains invalid keys'

# test update movie non required key
def test_movies_update_non_required_key():
    movie = movieObjectCreate.copy()
    movie.pop('title')
    r = put_movie(1, movie)
    assert r.status_code == 400
    assert r.json()['errors'] == 'Missing required keys in the request body'

# test update movie id not exist
def test_movies_update_id_not_exist():
    movie = movieObjectCreate.copy()
    r = put_movie(100, movie)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Movie not found or category not found'

# test update movie with non exist category
def test_movies_update_non_exist_category():
    movie = movies.find_one()
    movie['category'] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    movieId = str(movie['_id'])
    movie.pop('_id')
    movie.pop('__v')
    r = put_movie(movieId, movie)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Movie not found or category not found'

# test update movie successful
def test_movies_update_successful():
    movie = movies.find_one()
    categoryId = str(categories.find_one()['_id'])
    movie['category'] = [categoryId]
    movieId = str(movie['_id'])
    movie.pop('_id')
    movie.pop('__v')
    r = put_movie(movieId, movie)
    assert r.status_code == 204

# test delete movie id not exist
def test_movies_delete_id_not_exist():
    r = delete_movie(100)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Movie not found'

# test delete movie successful
def test_movies_delete_successful():
    movieId = str(movies.find_one()['_id'])
    r = delete_movie(movieId)
    assert r.status_code == 204

# test search movies
def test_movies_search():
    query = 'The Matrix'
    r = search_movies(query)
    titles = [movie['title'] for movie in r.json()]
    # check if we get all the movies with the title 'The Matrix'
    titles.sort()
    result = ['The Matrix', 'The Matrix Reloaded', 'The Matrix Revolutions']
    result.sort()
    assert titles == result
    assert r.status_code == 200

# test search movies no duplicates
def test_movies_search_no_duplicates():
    query = 'google'
    r = search_movies(query)
    # check if we get all the movies once
    titles = [movie['title'] for movie in r.json()]
    duplicates = [x for x in titles if titles.count(x) > 1]
    assert r.status_code == 200
    assert duplicates == []
    

# test get movies recommendation id not exist
def test_movies_get_recommendation_id_not_exist():
    r = get_movies_recommendation(350)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Movie not found'

# test get movies recommendation successful
def test_movies_get_recommendation_successful():
    r = get_movies_recommendation(104)
    assert r.status_code == 200
    assert r.json() == '111 109 114 107 108 103 110 112 113'

# test add movie to recommendation id not exist
def test_movies_add_recommendation_id_not_exist():
    r = add_movie_to_recommendation(350)
    assert r.status_code == 404
    assert r.json()['errors'] == 'Movie not found'

# test add movie to recommendation successful
def test_movies_add_recommendation_successful():
    r = add_movie_to_recommendation(110)
    assert r.status_code == 201