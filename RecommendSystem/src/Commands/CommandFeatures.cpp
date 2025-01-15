#include "CommandFeatures.h"
#include <regex>

// Map for command results
map<CommandResult, string> CommandResultMap = {
    {CommandResult::NOT_FOUND, "404 Not Found"},
    {CommandResult::OK, "200 Ok"},
    {CommandResult::CREATED, "201 Created"},
    {CommandResult::BAD_REQUEST, "400 Bad Request"},
    {CommandResult::NO_CONTENT, "204 No Content"},
};
// Split a string into a vector of strings by a delimiter
vector<string> CommandFeatures::splitToVector(string str, char delimiter)
{
    vector<string> result;
    stringstream stringStreamObject(str);
    string item;
    // Loop while getting the next string
    while (getline(stringStreamObject, item, delimiter))
    {
        // Check if didn't got an empty string
        if (item != "") {
            // Add the string to the vector
            result.push_back(item);
        }
    }
    // Return the vector
    return result;
}

// Convert a vector of strings to a vector of integers
vector<int> CommandFeatures::vectorStoI(vector<string> str, bool removeFirst)
{
    vector<int> result;
    // Loop over the vector of strings, check if the first element should be removed
    for (int i = removeFirst ? 1 : 0; i < str.size(); i++)
    {
        // Try to convert the string to an integer
        try {
            result.push_back(stoi(str.at(i)));
        // Catch any exception
        } catch (const exception &e) {
            continue;
        }
    }
    // Return the vector
    return result;
}
// Check if a vector of strings contains only integers using regex
bool CommandFeatures::isIntegers(vector<string> input) const
{
    // Define the regex pattern for integers
    regex integerPattern(R"(^\d+$)");

    // Loop over the vector of strings
    for (int i = 0; i < input.size(); i++)
    {
        // Check if the string is not an integer
        if (!regex_match(input[i], integerPattern))
        {
            return false;
        }
    }
  return true;
}

// Get the message of a command result
string CommandFeatures::getCommandResultMessage(CommandResult result) const
{
    // Return the message of the command result
    return CommandResultMap[result];
}
