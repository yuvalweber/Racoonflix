#ifndef APP_H
#define APP_H

#include "./Commands/GetCommand.h"
#include "./Commands/GetCommand.h"
#include "./Commands/HelpCommand.h"
#include "./Commands/PostCommand.h"
#include "./Commands/PatchCommand.h"
#include "./Commands/DeleteCommand.h"
#include "./Commands/CommandFeatures.h"
#include "./Helpers/FileIO.h"

#include <vector>
#include <string>
#include <map>

using namespace std;

class App 
{
private:
// This map contains all the commands
map<string, ICommand*> m_commands;
public:
    // This method is used to initialize the app
    void init();
    // This method is used to run the app
    string run(string command);
};

#endif // APP_H
