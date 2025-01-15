#include <gtest/gtest.h>
#include "../Helpers/FileIO.h"


FileIO *fileObject = new FileIO();

// Test for writing new user and movie
TEST(FileIOTest, writeUserMovie) {
    fileObject->writeUserMovie(1, 1);
    fileObject->writeUserMovie(1, 2);
    // check for duplication
    fileObject->writeUserMovie(1, 2);
    fileObject->writeUserMovie(2, 1);
    fileObject->writeUserMovie(2, 1);
    fileObject->writeUserMovie(2, 2);
    EXPECT_TRUE(fileObject->isMovieExist(1, 1) == IOResult::SUCCESS);
    EXPECT_TRUE(fileObject->isMovieExist(1, 3) == IOResult::NOT_FOUND);
    EXPECT_TRUE(fileObject->isUserExist(1) == IOResult::SUCCESS);
    EXPECT_TRUE(fileObject->isUserExist(3) == IOResult::NOT_FOUND);
}
//Test for the getter
TEST(FileIOTest, getter) {
    vector<int> movies = fileObject->getUserMovies(1);
    EXPECT_EQ(movies.size(), 2);
    EXPECT_EQ(movies[0], 1);
    EXPECT_EQ(movies[1], 2);
    vector<int> users = fileObject->getAllUsers();
    EXPECT_EQ(users.size(), 2);
    EXPECT_EQ(users[0], 1);
    vector<int> allMovies = fileObject->getAllMovies();
    EXPECT_EQ(allMovies.size(), 2);
    EXPECT_EQ(allMovies[0], 1);
    EXPECT_EQ(allMovies[1], 2);
}
//Test for the clear data
TEST (FileIOTest, clearData) {
    fileObject->clearData();
    vector<int> movies = fileObject->getUserMovies(1);
    EXPECT_EQ(movies.size(), 0);
    vector<int> users = fileObject->getAllUsers();
    EXPECT_EQ(users.size(), 0);
    vector<int> allMovies = fileObject->getAllMovies();
    EXPECT_EQ(allMovies.size(), 0);
    fileObject->clearData();
}
