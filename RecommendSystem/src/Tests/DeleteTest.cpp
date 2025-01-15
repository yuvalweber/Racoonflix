#include <gtest/gtest.h>
#include "../Commands/PostCommand.h"
#include "../Commands/DeleteCommand.h"
#include "../Commands/CommandFeatures.h"
#include "../Helpers/FileIO.h"

using namespace std;

//Creating an object of the class
FileIO *deleteFileIo = new FileIO();
PostCommand *postCommandDelete = new PostCommand(deleteFileIo);
DeleteCommand *del = new DeleteCommand(deleteFileIo);

// Test case: DeleteMovie
// Test case to check if the movie is deleted successfully
TEST(DeleteMovie, DeleteMovieTest) {
    string input = "post 1 2 3 4";
    postCommandDelete->execute(input);
    del->execute("delete 1 2");
    EXPECT_FALSE(postCommandDelete->isMovieExist(1, 2));
}

TEST(DeleteMovie, DeleteMovieTestSecond) {
    del->execute("delete 1	3");
    EXPECT_TRUE(deleteFileIo->isMovieExist(1, 3) == IOResult::SUCCESS);
}

//test case to check if the massage of the delete movie is correct
TEST(DeleteMovie, SucssefulDeleteMovieMsg) {
    EXPECT_STREQ(del->execute("delete 1 3").c_str(),
                del->getCommandResultMessage(CommandResult::NO_CONTENT).c_str());
}

//test case to check if when the user or the movie does not exist the function will return the correct message
//movie does not exist
TEST(DeleteMovie, DeleteMovieNotExist) {
    EXPECT_STREQ(del->execute("delete 1 2").c_str(),
              del->getCommandResultMessage(CommandResult::NOT_FOUND).c_str());
}

//user does not exist
TEST(DeleteMovie, DeleteUserNotExist) {
    EXPECT_STREQ(del->execute("delete 2 2").c_str(),
              del->getCommandResultMessage(CommandResult::NOT_FOUND).c_str());
}

//Test case: InvalidDeleteCommand
//Test case to check if the delete command is invalid because of the number of arguments (less than 3)

TEST(InvalidDeleteCommand, InvalidDeleteCommandTest) {
    EXPECT_STREQ(del->execute("delete 1").c_str(),
              del->getCommandResultMessage(CommandResult::BAD_REQUEST).c_str());
}

//Test case to check if the delete command is invalid because the arguments are not valid
TEST(InvalidDeleteCommand, InvalidDeleteCommandTest2) {
    postCommandDelete->execute("post 2 3 4 5");
    //some arguments that are not valid
    vector<string> invalidArgs = {"delete, 2, 3, 4", "delete 2	3	4", "delete a2 3 4 5"
    , "delete 2 3 4 5.5", "delete 2 3 4 5@", "delete 2 -3 4"};
    for (int i = 0; i < invalidArgs.size(); i++) {
        EXPECT_STREQ(del->execute(invalidArgs[i]).c_str(),
                     del->getCommandResultMessage(CommandResult::BAD_REQUEST).c_str());
    }
    deleteFileIo->clearData();
}






