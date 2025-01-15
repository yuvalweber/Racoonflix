#ifndef COMMANDFEATURES_H
#define COMMANDFEATURES_H

#include <string>
#include <vector>
#include <map>
#include <sstream>
#include <iostream>

using namespace std;

// Base class for command advanced features

// Enum for command results
enum class CommandResult
{
    NOT_FOUND,
    OK,
    CREATED,
    BAD_REQUEST,
    NO_CONTENT,

};
// Map for command results
extern map<CommandResult, string> CommandResultMap;

class CommandFeatures
{
public:
    // Split a string into a vector of strings by a delimiter
    vector<string> splitToVector(string str, char delimiter);
    // Convert a vector of strings to a vector of integers, removeFirst is a flag to remove the first element from the vector
    vector<int> vectorStoI(vector<string> str, bool removeFirst = true);
    // Check if a vector of strings contains only integers using regex
    bool isIntegers(vector<string> input) const;
    // Get the message of a command result
    string getCommandResultMessage(CommandResult result) const;
};
#endif
