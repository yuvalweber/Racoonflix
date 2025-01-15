#include "../Interfaces/ICommand.h"
#include "../Interfaces/IHelpInfo.h"
#include "CommandFeatures.h"
#include <vector>
#include <string>
#include <iostream>
#include "../Helpers/FileIO.h"

using namespace std;

class HelpCommand : public ICommand, public CommandFeatures {
    public:
        // This method returns the symbol of the help command
        virtual string getSymbol() const override { return "help"; };
        // This method returns the help message for the help command
        virtual string getHelp() const override;
        // This method is used to insert a HelpInfo object into the HelpInfo vector
        void InsertInfo(IHelpInfo *command);
        // This method is used to get the help message
        string getMessage();
        // This method is used to check if the input is valid
        virtual string isValidInput(vector<string>) const override;
        // This method is used to execute the help command 
        virtual string execute(string) override;
        explicit HelpCommand(IIO *io) : io(io) {}
    private:
        // This method is used to sort the help infos
        vector<IHelpInfo*> sortHelpInfos(vector<IHelpInfo*> vec);
        // This vector contains all the help infos
        vector<IHelpInfo*> m_helpInfos;
        IIO *io;
};
