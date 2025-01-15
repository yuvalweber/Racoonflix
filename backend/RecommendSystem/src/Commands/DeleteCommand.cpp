# include "DeleteCommand.h"

// This method returns the help message for the add command
string DeleteCommand::getHelp() const {
    // Get the symbol of the command and convert it to uppercase
    string symbol = this->getSymbol();
    transform(symbol.begin(), symbol.end(), symbol.begin(), ::toupper);
    return symbol + ", arguments: " + "[userid] [movieid1] [movieid2] …";
}

// This method checks if the input is valid for the delete command
string DeleteCommand::isValidInput(vector<string> input) const {
    if (input.size() < 3) {
        return getCommandResultMessage(CommandResult::BAD_REQUEST);
    }
    // Remove the first element from the vector
    input = vector<string>(input.begin() + 1, input.end());
    if (!isIntegers(input)) {
        return getCommandResultMessage(CommandResult::BAD_REQUEST);
    }
    // go through the input vector and check if the movies exist
    for (int i = 1; i < input.size(); i++)
    {
        // Check if the movie exists for the user
        if (io->isMovieExist(stoi(input[0]),stoi(input[i])) == IOResult::NOT_FOUND){
            return getCommandResultMessage(CommandResult::NOT_FOUND);
        }
    }
    return getCommandResultMessage(CommandResult::OK);
}

// This method executes the delete command
string DeleteCommand::execute(string input) {
    // Split the input string into a vector of strings
    vector<string> inputVector = splitToVector(input, ' ');
    // Check if the input is valid
    string validInput = isValidInput(inputVector);
    if (validInput != getCommandResultMessage(CommandResult::OK)) {
        return validInput;
    }
    vector<int> intVector = vectorStoI(inputVector);
    // Get the user id
    int userId = intVector[0];
    for (int i = 1; i < intVector.size(); i++)
    {
        // Delete the movie from the user
        io->deleteUserMovie(userId,intVector[i]);
    }
    // Return the result message
    return getCommandResultMessage(CommandResult::NO_CONTENT);
}
