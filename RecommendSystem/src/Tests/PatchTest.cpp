#include <gtest/gtest.h>
#include "../Commands/PatchCommand.h"
#include "../Commands/PostCommand.h"
#include "../Commands/CommandFeatures.h"
#include "../Helpers/FileIO.h"

using namespace std;

//Creating an object of the class
FileIO *patchFileIo = new FileIO();
PatchCommand *patch = new PatchCommand(patchFileIo);
PostCommand *postCommand = new PostCommand(patchFileIo);

//Sanity test
//Checking the validity of the input
TEST(PatchTest, validInputTest) {

    //Checking valid input
    string createUser = "post 3 101";
    postCommand->execute(createUser);
    vector<string> validInput = {"patch","3"};
    for (int i = 2; i < 100; i++)
    {
        string str_i = to_string(i);
        validInput.push_back(str_i); ;
        EXPECT_TRUE(patch->isValidInput(validInput) == patch->getCommandResultMessage(CommandResult::OK));
    }
    
    //Checking invalid input
    vector<vector<string>> invalidInputs = {{"patch"}, {"patch", "1"}, {"patch--"},
                                            {"patch","  1","3","5"}, {"patch", "shay"},
                                            {"patch","yuval","1"},
                                            {"patch","yair","1","2","3","4","5","6","7","h"}};
    for (vector<string> input : invalidInputs)
    {
        EXPECT_TRUE(patch->isValidInput(input) == patch->getCommandResultMessage(CommandResult::BAD_REQUEST));
    }
}

//Checking if a user that does not exist entered, should return "404 Not Found"
TEST(PatchTest, patchNonExistingUserTest) {
    EXPECT_STREQ(patch->execute("patch 200 2 3").c_str(),
                 patch->getCommandResultMessage(CommandResult::NOT_FOUND).c_str());
}


//Checking if after entering a new movie to a user, the movie is added to the list of movies of the user
TEST(PatchTest, patchNewMovieTest) {
    postCommand->execute("post 1 4 5");
    string input = "patch 1 2 3";
    patch->execute(input);
    EXPECT_TRUE(patch->isMovieExist(1, 2));
    EXPECT_TRUE(patch->isMovieExist(1, 3));
    patchFileIo->clearData();
}

//Checking if the patch function returns the correct message
TEST(PatchTest, patchTest) {
    postCommand->execute("post 1 2 3");
    string input = "patch 1 4 5";
    EXPECT_STREQ(patch->execute(input).c_str(),
                 patch->getCommandResultMessage(CommandResult::NO_CONTENT).c_str());
    patchFileIo->clearData();
}


//Checking the masaage of the getHelp function
TEST(PatchTest, getHelpTest) {
    string expected = "PATCH, arguments: [userid] [movieid1] [movieid2] …";
    EXPECT_EQ(patch->getHelp(), expected) << "getHelp function does not return the correct message";
}


