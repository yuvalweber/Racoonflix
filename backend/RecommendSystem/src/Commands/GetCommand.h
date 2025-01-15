#ifndef GET_COMMAND_H
#define GET_COMMAND_H

#include "../Interfaces/ICommand.h"
#include "../Interfaces/IIO.h"
#include "CommandFeatures.h"
#include <string>
#include <vector>
#include <map>
#include <algorithm>

using namespace std;

class GetCommand : public ICommand, public CommandFeatures
{
private:
    IIO* m_io_object;
     //this function return the unseen movies for the user.
    vector<int> getNotSeenMovies(vector<int> userMovies, int movieId);
    //this function return the similarity map between the user and the other users.
    map<int, int> getSimilarityMap(vector<int> userMovies, vector<int> users, int movieId, int userId);
    //this function uses the returns of the other related function and return the final recommendation.
    vector<pair<int,int>> getFinalRecommendation(vector<int> notSeenMovies, map<int, int> similarityMap, int movieId);
    //this function get the recommand map and return the sorted recommended movies for the user.
    vector<pair<int,int>> getSortedRecommendedMovies(map<int, int> recommendMap);
    //this function check if the recommendation is empty.
    bool isRecommendEmpty(const vector<pair<int,int>>& recommendedMovies) const;
public:
    // GetCommand constructor.
    explicit GetCommand(IIO* io_object) : m_io_object(io_object) {};
    //execute function for GetCommand.
    virtual string execute(string input) override;
    //get help function for GetCommand, returns a string that contains the help message.
    virtual string getHelp() const override;
    //isValidInput function for GetCommand, returns a boolean value that indicates if the input is valid.
    virtual string isValidInput(vector<string> input) const override;
    //this function return the recommend movie for the user.
    vector<pair<int,int>> getRecommendedMovies(int userId, int movieId);
    //this function return the symbol
    virtual string getSymbol() const override { return "get"; }
};
#endif // GET_COMMAND_H
