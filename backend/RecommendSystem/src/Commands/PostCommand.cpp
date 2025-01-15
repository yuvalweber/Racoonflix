#include "PostCommand.h"


string PostCommand::isValidInput(vector<string> input) const {
    // Check if the input is valid, using the AddCommand's isValidInput
    string isValid = AddCommand::isValidInput(input);
    // Check if the input is valid for the post command
    if (isValid == getCommandResultMessage(CommandResult::OK)) {
        // Check if the user exists
        if (io->isUserExist(stoi(input[1])) == IOResult::SUCCESS) {
            // Then this command is not valid
            return getCommandResultMessage(CommandResult::NOT_FOUND);
        }
    }
    return isValid;
}

string PostCommand::execute(string input) {
    // Execute the post command and save the output
    string commandOutput = AddCommand::execute(input);
    // Check if the command output is OK
    if (commandOutput == getCommandResultMessage(CommandResult::OK)) {
        // If it is, return the created message
        return getCommandResultMessage(CommandResult::CREATED);
    }
    // Otherwise, return the output
    return commandOutput;
}
