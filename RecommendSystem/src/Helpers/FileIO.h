#ifndef FILEIO_H
#define FILEIO_H

#include "../Interfaces/IIO.h"
#include "../Commands/CommandFeatures.h"
#include <fstream>
#include <string>
#include <vector>
#include <algorithm>
#include <mutex>

// Define what file to read from and write to according to what we want (tests\project).
#ifdef TESTS
const string FILE_NAME = "../../../data/moviesTest.txt";
#else
const string FILE_NAME = "../../data/movies.txt";
#endif

// This class is used to read and write data to a file.
class FileIO : public IIO, public CommandFeatures {
  public:
    // This method is used to get the movies of a user.
    virtual vector<int> getUserMovies(int userId) override;
    // This method is used to get all the users.
    virtual vector<int> getAllUsers() override;
    // This method is used to get all the movies.
    virtual vector<int> getAllMovies() override;
    // This method is used to add a movie to a user.
    virtual void writeUserMovie(int userId, int movie) override;
    // This method is used to check if a user exists.
    virtual IOResult isUserExist(int userId) const override;
    // This method is used to check if a movie exists.
    virtual IOResult isMovieExist(int userId, int movieId) override;
    // This method is used to clear the data in the file.
    virtual void clearData() override;
    // This function is used to delete a user from the file.
    virtual void deleteUser(int userId) override;
    // This function is used to delete a movie from a user.
    virtual void deleteUserMovie(int userId, int movieId) override;

private:
    // This method is used to sort and remove duplicates from a vector.
    vector<int> sortAndRemoveDuplicates(vector<int> vec);
};

#endif // FILEIO_H
