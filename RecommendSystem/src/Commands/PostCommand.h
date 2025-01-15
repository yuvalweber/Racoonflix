#ifndef POST_COMMAND_H
#define POST_COMMAND_H

#include "AddCommand.h"

#include <string>
#include <vector>
#include <algorithm>

using namespace std;

class PostCommand : public AddCommand {
    public :
        // This function returns the command symbol
        virtual string getSymbol() const override {return "post";}
        // This function checks if the input is valid
        virtual string isValidInput(vector<string> input) const override;
        // This function executes the command
        virtual string execute(string input) override;
        explicit PostCommand(IIO *io) : AddCommand(io) {}
};


#endif // POST_COMMAND_H
