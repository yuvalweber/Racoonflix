#include "App.h"

using namespace std;

void App::init()
{
    // Create a new instance of the FileIO class
    FileIO* fileIO = new FileIO();

    // Create the commands and enter them into the map
    ICommand* postCommand = new PostCommand(fileIO);
    m_commands["post"] = postCommand;
    ICommand *patchCommand = new PatchCommand(fileIO);
    m_commands["patch"] = patchCommand;
    ICommand *deleteCommand = new DeleteCommand(fileIO);
    m_commands["delete"] = deleteCommand;
    ICommand* getCommand = new GetCommand(fileIO);
    m_commands["get"] = getCommand;

    // Create a new instance of the HelpCommand class
    HelpCommand* help = new HelpCommand(fileIO);
    // Insert the commands into the helpInfo vector
    help->InsertInfo(postCommand);
    help->InsertInfo(patchCommand);
    help->InsertInfo(deleteCommand);
    help->InsertInfo(getCommand);

    // Cast the help command to ICommand and insert it into the map (upcasting)
    ICommand* HelpCommand = static_cast<ICommand*>(help);
    m_commands["help"] = HelpCommand;
}

string App::run(string command)
{
    
    //Find the first word of the command
    string firstWordCommand = command.substr(0, command.find_first_of(" "));
    //Create a commandFeatures object
    CommandFeatures* commandFeatures = new CommandFeatures();

    string response = commandFeatures->getCommandResultMessage(CommandResult::BAD_REQUEST);
    // If command exist then run it
    if (m_commands.find(firstWordCommand) != m_commands.end())
    {
        response = m_commands[firstWordCommand]->execute(command);
    }

    // Return the response
    return response;
}



