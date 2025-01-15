#ifndef ADDCOMMAND_H
#define ADDCOMMAND_H

#include "../Interfaces/ICommand.h"
#include "CommandFeatures.h"
#include "../Interfaces/IIO.h"
#include <string>
#include <vector>
#include <algorithm>

using namespace std;

// This class define the add command
class AddCommand : public ICommand, public CommandFeatures {
  public:
    // This method executes the add command
    virtual string execute(string) override;
    // This method returns the help message for the add command
    virtual string getHelp() const override;
    // This method checks if the input is valid for the add command
    virtual string isValidInput(vector<string>) const override;
    // This method checks if a specific user exists in the file.
    bool isUserExist(int userId);
    // This method checks if a specific movie exists for a specific user in the file.
    bool isMovieExist(int userId, int movieId);
    explicit AddCommand(IIO *io) : io(io) {}
  protected:
    IIO *io;
};

#endif
