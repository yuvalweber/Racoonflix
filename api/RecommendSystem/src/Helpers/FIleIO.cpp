#include "FileIO.h"


// This method remove duplicates
vector<int> FileIO::sortAndRemoveDuplicates(vector<int> vec) {
  // Sort the vector
  sort(vec.begin(), vec.end());
  // Remove duplicates
  vec.erase(unique(vec.begin(), vec.end()), vec.end());
  // Return the new vector
  return vec;
}
// This method checks if a specific user exists in the file.
IOResult FileIO::isUserExist(int userId) const {
  string line;
  string userIdStr = to_string(userId);
  ifstream file(FILE_NAME);

  // While there are lines in the file, read them.
  while (getline(file, line)) {
      // If the first word in the line matches the user ID, then return SUCCESS.
      if (line.substr(0, line.find_first_of(" ")) == userIdStr) {
          file.close();
          return IOResult::SUCCESS;
      }
  }
  file.close();
  return IOResult::NOT_FOUND;
}
// This method reads the data for a specific user from the file.
vector<int> FileIO::getUserMovies(int userId) {
  string result;
  string line;
  string userIdStr = to_string(userId);
  ifstream file(FILE_NAME);

  // While there are lines in the file, read them.
  while (getline(file, line)) {
      // If the first word in the line matches the user ID, then return the line.
      if (line.substr(0, line.find_first_of(" ")) == userIdStr) {
          result = line;
          break;
      }
  }
  file.close();
  // Return the movies of the user (without the user ID).
  vector<int> movies = vectorStoI(splitToVector(result, ' '));
  // Remove duplicates from the vector and sort it.
  return sortAndRemoveDuplicates(movies);
}

// This method checks if a specific movie exists for a specific user in the file.
IOResult FileIO::isMovieExist(int userId, int movieId) {
  // Get the movies of the user.
  vector<int> movies = getUserMovies(userId);
  // If the movie is in the vector, return SUCCESS.
  for (int i = 0; i < movies.size(); i++) {
      if (movies[i] == movieId) {
          return IOResult::SUCCESS;
      }
  }
  // If the movie is not in the vector, return NOT_FOUND.
  return IOResult::NOT_FOUND;
}
// Get all users from the file.
vector<int> FileIO::getAllUsers() {
  vector<string> result;
  string line;
  ifstream file(FILE_NAME);

  // While there are lines in the file, read them.
  while (getline(file, line)) {
      // Get the user ID from the line and add it to the result vector.
      result.push_back(line.substr(0, line.find_first_of(" ")));
  }
  file.close();
  // Remove duplicates from the vector and sort it.
  vector<int> sorted = sortAndRemoveDuplicates(vectorStoI(result, false));
  // Return the sorted vector.
  return sorted;
}

// Get all movies from the file.
vector<int> FileIO::getAllMovies() {
  vector<vector<string>> resultStr;
  vector<int> result;
  string line;
  ifstream file(FILE_NAME);

  // While there are lines in the file, read them.
  while (getline(file, line)) {
      // Split the line by space and add it to the result vector.
      resultStr.push_back(splitToVector(line, ' '));
  }
  file.close();
  // Get the movies from the result vector.
  for (int i = 0; i < resultStr.size(); i++) {
    vector<int> resultTemp = vectorStoI(resultStr[i]);
    // Add the movies to the result vector.
    for (int j = 0; j < resultTemp.size(); j++) {
      result.push_back(resultTemp[j]);
    }
  }
  // Remove duplicates from the vector and sort it.
  result = sortAndRemoveDuplicates(result);
  return result;
}
// This method writes data for a specific user to the file.
void FileIO::writeUserMovie(int userId, int movie) {
  // Check if the user exists.
  IOResult result = isUserExist(userId);
  // If the user does not exist, then add the user and the movie to the file.
  if (result == IOResult::NOT_FOUND) {
      ofstream file(FILE_NAME, ios_base::app);
      file << userId << " " << movie << endl;
      file.close();
      return;
  }
  else {
      string line;
      string userIdStr = to_string(userId);
      string movieStr = to_string(movie);
      ifstream file(FILE_NAME);
      #ifdef TESTS
      string tempFileName = "../../../data/temp.txt";
      #else
      string tempFileName = "../../data/temp.txt";
      #endif
      ofstream tempFile(tempFileName);

      // While there are lines in the file, read them.
      while (getline(file, line)) {
          if (line.substr(0, line.find_first_of(" ")) == userIdStr) {
              vector<string> splittedLine = splitToVector(line, ' ');
              // If the movie is not in the line, then add it.
              if (!std::any_of(splittedLine.begin() + 1, splittedLine.end(), [movieStr](string s) { return s == movieStr; })) {
                  line += " " + movieStr;
              }
          }
          tempFile << line << endl;
      }
      file.close();
      tempFile.close();
      // Remove the old file and rename the new file.
      remove(FILE_NAME.c_str());
      rename(tempFileName.c_str(), FILE_NAME.c_str());
  }
}

// This method clears the data in the file.
void FileIO::clearData() {
  ofstream file(FILE_NAME);
  file.close();
}

// This method deletes a user from the file.
void FileIO::deleteUser(int userId) {
  string line;
  string userIdStr = to_string(userId);
  ifstream file(FILE_NAME);
  #ifdef TESTS
  string tempFileName = "../../../data/temp.txt";
  #else
  string tempFileName = "../../data/temp.txt";
  #endif
  ofstream tempFile(tempFileName);

  // While there are lines in the file, read them.
  while (getline(file, line)) {
      // If the user ID in the line does not match the user ID, then add the line to the new file.
      if (line.substr(0, line.find_first_of(" ")) != userIdStr) {
          tempFile << line << endl;
      }
  }
  file.close();
  tempFile.close();
  // Remove the old file and rename the new file.
  remove(FILE_NAME.c_str());
  rename(tempFileName.c_str(), FILE_NAME.c_str());
}

// This method deletes a movie from a user.
void FileIO::deleteUserMovie(int userId, int movieId) {
  string line;
  string userIdStr = to_string(userId);
  ifstream file(FILE_NAME);
  #ifdef TESTS
  string tempFileName = "../../../data/temp.txt";
  #else
  string tempFileName = "../../data/temp.txt";
  #endif
  ofstream tempFile(tempFileName);

  // While there are lines in the file, read them.
  while (getline(file, line)) {
      // If the user ID in the line matches the user ID, then remove the movie from the line.
      if (line.substr(0, line.find_first_of(" ")) == userIdStr) {
          vector<string> splittedLine = splitToVector(line, ' ');
          vector<string> newLine;
          // Push the user ID to the new line.
          newLine.push_back(splittedLine[0]);
          // Go through the movies of the user and add them to the new line, except the movie we want to delete.
          for (int i = 1; i < splittedLine.size(); i++) {
              if (stoi(splittedLine[i]) != movieId) {
                  newLine.push_back(splittedLine[i]);
              }
          }
          // If the new line isn't empty, then add it to the new file.
          if (newLine.size() > 0) {
              // Push the user ID to the new line.
              tempFile << newLine[0];
              // Go through the movies of the user and add them to the new line.
              for (int i = 1; i < newLine.size(); i++) {
                  tempFile << " " << newLine[i];
              }
              tempFile << endl;
          }
      }
      else {
          tempFile << line << endl;
      }
  }
  file.close();
  tempFile.close();
  // Remove the old file and rename the new file.
  remove(FILE_NAME.c_str());
  rename(tempFileName.c_str(), FILE_NAME.c_str());
}
