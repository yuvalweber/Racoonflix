#ifndef ICOMMAND_H
#define ICOMMAND_H

#include <string>
#include <vector>
#include "IHelpInfo.h"
using namespace std;
// Interface for the command classes to force the implementation of the execute() method.
// this method is used to execute the command.
// isValidInput() method is used to validate the input of the command.
class ICommand : public IHelpInfo {
  public:
    virtual ~ICommand() = default;
    virtual string execute(string) = 0;
    virtual string isValidInput(vector<string>) const = 0;
    virtual string getSymbol() const = 0;
};

#endif
