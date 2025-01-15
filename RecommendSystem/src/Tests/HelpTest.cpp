#include <gtest/gtest.h>
#include "../Commands/HelpCommand.h"
#include "../Interfaces/IHelpInfo.h"
#include "../Helpers/FileIO.h"

using namespace std;

class DummyObject : public IHelpInfo {
    public:
        explicit DummyObject(const string& helpMessage) : m_helpMessage(helpMessage) {};
        virtual string getHelp() const override { return m_helpMessage; };
    private:
        string m_helpMessage;
};

//Creating an object of the class
FileIO *fileIo = new FileIO();
HelpCommand *a = new HelpCommand(fileIo);
vector<string> objectNames = {"dummy", "yummy, arguments: 1 105","gummy, arguments: 1 104"};
DummyObject *b = new DummyObject(objectNames.at(0));
DummyObject *c = new DummyObject(objectNames.at(1));
DummyObject *d = new DummyObject(objectNames.at(2));
string command = "HELP";

//Test for the getter
TEST(HelpCommandTest, getter) {
    EXPECT_EQ(a->getHelp(), command);
}

//Test if the insertInfo insert to the vector correctly and print alphabetically
TEST(HelpCommandTest, regTest){
    a->InsertInfo(b);
    a->InsertInfo(c);
    a->InsertInfo(d);
    string rightOrderCommand = objectNames.at(0) + "\n" +
                        objectNames.at(2) + "\n" +
                        objectNames.at(1) + "\n" +
                        command;
    EXPECT_EQ(a->getMessage(), rightOrderCommand);
}

//Checks if the input is just the word help
TEST(HelpCommandTest, validInput){
    vector<vector<string>> invalidInputs = {{"help", "me"}, {"helpme"}, {"helpo"}, {"help "}};
    for (vector<string> input : invalidInputs) {
        EXPECT_STREQ(a->isValidInput(input).c_str(), a->getCommandResultMessage(CommandResult::BAD_REQUEST).c_str());
    }
    EXPECT_STREQ(a->isValidInput({"help"}).c_str(), a->getCommandResultMessage(CommandResult::OK).c_str());
}

// Checks for duplicates
TEST(HelpCommandTest, duplicates){
    a->InsertInfo(b);
    a->InsertInfo(c);
    a->InsertInfo(d);
    a->InsertInfo(b);
    a->InsertInfo(c);
    a->InsertInfo(d);
    string newCommand = objectNames.at(0) + "\n" +
                        objectNames.at(2) + "\n" +
                        objectNames.at(1) + "\n" +
                        command;
    EXPECT_EQ(a->getMessage(), newCommand);
}
