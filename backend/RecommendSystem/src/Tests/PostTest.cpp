#include <gtest/gtest.h>
#include "../Commands/PostCommand.h"
#include "../Commands/CommandFeatures.h"
#include "../Helpers/FileIO.h"

using namespace std;

//Creating an object of the class
FileIO *postFileIo = new FileIO();
PostCommand *post = new PostCommand(postFileIo);
CommandFeatures *commandFeaturesPost = new CommandFeatures();

//Sanity test
//Checking the validity of the input
TEST(postTest, validInputTest) {

    //checking valid input
    vector<string> validInput = {"post","3"};
    for (int i = 2; i < 100; i++)
    {
        string str_i = to_string(i);
        validInput.push_back(str_i); ;
        EXPECT_TRUE(post->isValidInput(validInput) == commandFeaturesPost->getCommandResultMessage(CommandResult::OK));
    }
    
    //checking invalid input
    vector<vector<string>> invalidInputs = {{"post"}, {"post", "1"}, {"post--"},
                                            {"post","  1","3","5"}, {"post", "shay"},
                                            {"post","yuval","1"},
                                            {"post","yair","1","2","3","4","5","6","7","h"}};
    for (vector<string> input : invalidInputs)
    {
        EXPECT_TRUE(post->isValidInput(input) == commandFeaturesPost->getCommandResultMessage(CommandResult::BAD_REQUEST));
    }
}

//Checking the execute function
//Checking if after entering a new user, the user is posted to the list of users
TEST(postTest, postNewUserTest) {
    string input = "post 1 2 3";
    if (post->execute(input) == commandFeaturesPost->getCommandResultMessage(CommandResult::CREATED))
    {
        EXPECT_TRUE(post->isUserExist(1));
    }
    EXPECT_TRUE(post->isUserExist(1));
    postFileIo->clearData();
}
TEST(postTest, postExistingUserTest) {
    EXPECT_FALSE(post->isUserExist(555));
    EXPECT_FALSE(post->isUserExist(666));
    EXPECT_FALSE(post->isUserExist(777));
    //Checking if handling a user that already exists returns "404 Not Found"
    string invalidInput = "post 3 3";
    if (post->execute(invalidInput) == commandFeaturesPost->getCommandResultMessage(CommandResult::CREATED))
    {
        // if exist return 404
        EXPECT_STREQ(post->execute(invalidInput).c_str(),
                    commandFeaturesPost->getCommandResultMessage(CommandResult::NOT_FOUND).c_str());
    }
}

//Checking if after entering a new movie with a new user, the movie is posted to the list of movies of the user
TEST(postTest, postNewMovieTest) {
    string input = "post 1 2 3";
    if (post->execute(input) == commandFeaturesPost->getCommandResultMessage(CommandResult::CREATED))
    {
        EXPECT_TRUE(post->isMovieExist(1, 2));
        EXPECT_TRUE(post->isMovieExist(1, 3));
    }
    postFileIo->clearData();
}

//Checking if the post function returns the correct message
TEST(postTest, postTest) {
    string input = "post 1 2 3";
    EXPECT_STREQ(post->execute(input).c_str(),
                 commandFeaturesPost->getCommandResultMessage(CommandResult::CREATED).c_str());
    postFileIo->clearData();
}

//checking the masaage of the getHelp function
TEST(postTest, getHelpTest) {
    string expected = "POST, arguments: [userid] [movieid1] [movieid2] …";
    EXPECT_EQ(post->getHelp(), expected) << "getHelp function does not return the correct message";
}


