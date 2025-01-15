#include "PatchCommand.h"


string PatchCommand::isValidInput(vector<string> input) const {
    // Check if the input is valid, using the AddCommand's isValidInput
    string isValid = AddCommand::isValidInput(input);
    // Check if the input is valid for the patch command
    if (isValid == getCommandResultMessage(CommandResult::OK)) {
        // Check if the user exists
        if (io->isUserExist(stoi(input[1])) == IOResult::NOT_FOUND) {
            // Then this command is not valid
            return getCommandResultMessage(CommandResult::NOT_FOUND);
        }
    }
    return isValid;
}

string PatchCommand::execute(string input) {
    // Execute the patch command and save the output
    string commandOutput = AddCommand::execute(input);
    // Check if the command output is OK
    if (commandOutput == getCommandResultMessage(CommandResult::OK)) {
        // If it is, return the no content message
        return getCommandResultMessage(CommandResult::NO_CONTENT);
    }
    // Otherwise, return the output
    return commandOutput;
}
