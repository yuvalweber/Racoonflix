#ifndef PATCH_COMMAND_H
#define PATCH_COMMAND_H

#include "AddCommand.h"

#include <string>
#include <vector>
#include <algorithm>

using namespace std;

class PatchCommand : public AddCommand {
    public :
        // This function returns the command symbol
        virtual string getSymbol() const override {return "patch";}
        // This function checks if the input is valid
        virtual string isValidInput(vector<string> input) const override;
        // This function executes the command
        virtual string execute(string input) override;
        explicit PatchCommand(IIO *io) : AddCommand(io) {}
};


#endif // PATCH_COMMAND_H
