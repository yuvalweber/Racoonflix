#ifndef IIO_H
#define IIO_H

#include <string>
#include <vector>
using namespace std;

// This enum class is used to represent the result of an I/O operation.
enum class IOResult {
  SUCCESS,
  FAILURE,
  NOT_FOUND
};
// This is an interface class for the I/O operations to force the implementation of the methods.
class IIO {
  public:
    virtual ~IIO() = default;
    // This method is used to get the movies of a user.
    virtual vector<int> getUserMovies(int userId) = 0;
    // This method is used to get all the users.
    virtual vector<int> getAllUsers() = 0;
    // This method is used to get all the movies.
    virtual vector<int> getAllMovies() = 0;
    // This method is used to get the average rating of a movie.
    virtual void writeUserMovie(int userId, int movie) = 0;
    // This method is used to get the average rating of a movie.
    virtual IOResult isUserExist(int userId) const = 0;
    // This method is used to get the average rating of a movie.
    virtual IOResult isMovieExist(int userId, int movieId) = 0;
    // clear the data
    virtual void clearData() = 0;
    // This function is used to delete a user from the file.
    virtual void deleteUser(int userId) = 0;
    // This function is used to delete a movie from a user.
    virtual void deleteUserMovie(int userId, int movieId) = 0;
};

#endif
