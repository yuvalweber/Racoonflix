#ifndef DELETECOMMAND_H
#define DELETECOMMAND_H

#include "../Interfaces/IIO.h"
#include "../Interfaces/ICommand.h"
#include "CommandFeatures.h"
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

// This class define the delete command
class DeleteCommand : public ICommand, public CommandFeatures {
  public:
    // This method is used to execute the delete command.
    virtual string execute(string) override;
    // This method is used to get the help information of the delete command.
    virtual string getHelp() const override;
    // This method is used to validate the input of the delete command.
    virtual string isValidInput(vector<string>) const override;
    // This is the constructor of the delete command.
    explicit DeleteCommand(IIO *io) : io(io) {}
  private:
    IIO *io;
    // This method is used to get the symbol of the delete command.
    string getSymbol() const {
        return "delete";
    }
};

#endif
