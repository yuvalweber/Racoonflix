#include "AddCommand.h"

// This method returns the help message for the add command
string AddCommand::getHelp() const {
    // Get the symbol of the command and convert it to uppercase
    string symbol = this->getSymbol();
    transform(symbol.begin(), symbol.end(), symbol.begin(), ::toupper);
    // Return the help message according to the format
    return symbol + ", arguments: " + "[userid] [movieid1] [movieid2] …";
}

// This method checks if the input is valid for the add command
string AddCommand::isValidInput(vector<string> input) const {
    if (input.size() < 3) {
        return getCommandResultMessage(CommandResult::BAD_REQUEST);
    }
    // Remove the first element from the vector
    input = vector<string>(input.begin() + 1, input.end());
    if (!isIntegers(input)) {
        return getCommandResultMessage(CommandResult::BAD_REQUEST);
    }
    return getCommandResultMessage(CommandResult::OK);
}

// This method executes the add command
string AddCommand::execute(string input) {
    // Split the input string into a vector of strings
    vector<string> inputVector = splitToVector(input, ' ');
    // Check if the input is valid
    string validInput = isValidInput(inputVector);
    // Return the error message if the input is not valid
    if (validInput != getCommandResultMessage(CommandResult::OK)) {
        return validInput;
    }
    // Convert the vector of strings to a vector of integers
    vector<int> intVector = vectorStoI(inputVector);
    // Get the user id
    int userId = intVector[0];
    for (int i = 1; i < intVector.size(); i++)
    {
        // Add the movie to the user
        io->writeUserMovie(userId,intVector[i]);
    }
    // Return the success message
    return getCommandResultMessage(CommandResult::OK);
}

// This method checks if a specific user exists in the file.
bool AddCommand::isUserExist(int userId) {
    // Check if the user exists in the file using the IIO object
    return io->isUserExist(userId) == IOResult::SUCCESS;
}

// This method checks if a specific movie exists for a specific user in the file.
bool AddCommand::isMovieExist(int userId, int movieId) {
    // Check if the movie exists for the user using the IIO object
    return io->isMovieExist(userId, movieId) == IOResult::SUCCESS;
}
