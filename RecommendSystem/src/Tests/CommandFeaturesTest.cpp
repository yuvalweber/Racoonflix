#include <gtest/gtest.h>
#include "../Commands/CommandFeatures.h"

using namespace std;

CommandFeatures *cFeatues = new CommandFeatures();

// Test if the splitToVector function works correctly
TEST(CommandFeaturesTest, splitToVectorTest){
    string str = "102 103 104";
    vector<string> expected = {"102", "103", "104"};
    vector<string> result = cFeatues->splitToVector(str, ' ');
    for(int i = 0; i < expected.size(); i++){
        EXPECT_EQ(expected.at(i), result.at(i));
    }
}

// Test if the Stoi function works correctly   
TEST(CommandFeaturesTest, convertToIntegersTest){
    vector<string> str = {"add","1", "2", "3"};
    vector<int> expected = {1, 2, 3};
    vector<int> result = cFeatues->vectorStoI(str);
    for(int i = 0; i < expected.size(); i++){
        EXPECT_EQ(expected.at(i), result.at(i));
    }
}

// Test if the isIntegers function works correctly
TEST(CommandFeaturesTest, isIntegersTest){
    vector<string> validIntegers = {"1", "2", "3"};
    EXPECT_TRUE(cFeatues->isIntegers(validIntegers));
    vector<string> invalidIntegers = {"1", "2", "3", "a"};
    EXPECT_FALSE(cFeatues->isIntegers(invalidIntegers));
}
