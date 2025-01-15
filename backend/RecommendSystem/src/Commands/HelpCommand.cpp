#include "HelpCommand.h"

// This method returns the help message for the add command
string HelpCommand::getHelp() const {
    // Get the symbol and convert it to uppercase
    string symbol = this->getSymbol();
    transform(symbol.begin(), symbol.end(), symbol.begin(), ::toupper);
    return symbol;
}

// Insert a command into the help command
void HelpCommand::InsertInfo(IHelpInfo *command) {
    // Avoid duplicates
    if (!std::any_of(m_helpInfos.begin(), m_helpInfos.end(),
    [command](const IHelpInfo *info) { return command == info; }))
    {
        m_helpInfos.push_back(command);
    }
}

// Get the help message
string HelpCommand::getMessage() {
    string message = "";
    // Sort the help infos 
    m_helpInfos = sortHelpInfos(m_helpInfos);
    // Build the message
    for(IHelpInfo *info : m_helpInfos) {
        message += info->getHelp() + "\n";
    }
    message += getHelp();
    return message;
}

// Check if the input is valid
string HelpCommand::isValidInput(vector<string> inputVector) const {
    if (inputVector.size() == 1 && inputVector.at(0) == this->getSymbol()) {
        // If the input is valid return 'OK'.
        return getCommandResultMessage(CommandResult::OK);
    }
    // If the input is invalid return 'BAD_REQUEST'.
    return getCommandResultMessage(CommandResult::BAD_REQUEST);
}

// Execute the help command
string HelpCommand::execute(string input) {
    // Split the input string into a vector of strings
    vector<string> inputVector = splitToVector(input, ' ');
    // Check if the input is valid
    if (isValidInput(inputVector) == getCommandResultMessage(CommandResult::OK)) {
        // If the input is valid return 'OK' and the help message
        return getCommandResultMessage(CommandResult::OK) + "\n\n" + getMessage();
    }
    // If the input is invalid return 'BAD_REQUEST'
    return getCommandResultMessage(CommandResult::BAD_REQUEST);
}

// Sort the help infos
vector<IHelpInfo*> HelpCommand::sortHelpInfos(vector<IHelpInfo*> vec) {
    sort(vec.begin(), vec.end(), [](IHelpInfo *a, IHelpInfo *b) {
        return a->getHelp() < b->getHelp();
    });
    return vec;
}
