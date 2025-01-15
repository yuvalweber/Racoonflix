#ifndef IHELPINFO_H
#define IHELPINFO_H

#include <string>
using namespace std;
// This is an interface class for the Commands to force the implementation of the getHelp() method.
// this method show relevant help information to the user on the command.
class IHelpInfo {
  public:
    virtual ~IHelpInfo() = default;
    virtual string getHelp() const = 0;
};

#endif
