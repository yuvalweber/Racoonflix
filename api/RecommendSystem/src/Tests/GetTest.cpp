#include <vector>
#include <string>
#include <gtest/gtest.h>
#include "../Commands/GetCommand.h"
#include "../Commands/PostCommand.h"
#include "../Commands/PatchCommand.h"
#include "../Helpers/FileIO.h"

using namespace std;

FileIO *getFileIo = new FileIO();
GetCommand *getCommand = new GetCommand(getFileIo);
// Test the getRecommendation method
TEST(GetTest, getHelpTest) {
    string expected = "GET, arguments: [userid] [movieid1]";
    EXPECT_EQ(getCommand->getHelp(), expected) << "getHelp function does not return the correct message";
}

TEST(GetTest, invalidInput) {
    vector<vector<string>> invalidInputs = {
        {"get"},
        {"get","something"},
        {"get","  101","20"}
    };
    for(vector<string> input : invalidInputs){
        EXPECT_STREQ(getCommand->isValidInput(input).c_str(), getCommand->getCommandResultMessage(CommandResult::BAD_REQUEST).c_str());
    }
}

TEST(getTest, outputCorrectFormat) {
    // create test for checking that we get the correct recommendations 
    int userId = 1;
    int movieId = 104;
    PostCommand *postCommandObject = new PostCommand(getFileIo);
    
    postCommandObject->execute("post 1 100 101 102 103");
    postCommandObject->execute("post 2 101 102 104 105 106");
    postCommandObject->execute("post 3 100 104 105 107 108");
    postCommandObject->execute("post 4 101 105 106 107 109 110");
    postCommandObject->execute("post 5 100 102 103 105 108 111");
    postCommandObject->execute("post 6 100 103 104 110 111 112 113");
    postCommandObject->execute("post 7 102 105 106 107 108 109 110");
    postCommandObject->execute("post 8 101 104 105 106 109 111 114");
    postCommandObject->execute("post 9 100 103 105 107 112 113 115");
    postCommandObject->execute("post 10 100 102 105 106 107 109 110 116");

    string expectedRecommendaions = "105 106 111 110 112 113 107 108 109 114";
    vector<pair<int,int>> recommendations = getCommand->getRecommendedMovies(userId,movieId);
    string recommendationsString = "";
    for (pair<int,int> recommendation : recommendations){
        recommendationsString += to_string(recommendation.first) + " ";
    }
    recommendationsString.resize(recommendationsString.size() - 1);

    // check that the output is correct
    EXPECT_EQ(recommendationsString, expectedRecommendaions) << "recommendations are not correct";

    // check if the output is in the correct format
    vector<string> recommendationsList = getCommand->splitToVector(recommendationsString,' ');
    EXPECT_LE(recommendationsList.size(), 10) << "should not give more than 10 recommendations";

    // check that output only contains numbers
    for(string recommendation : recommendationsList){
        int number = atoi(recommendation.c_str());
        EXPECT_NE(number, 0) << "recommendation should be a number";
    }

    // clean data
    getFileIo->clearData();
}

