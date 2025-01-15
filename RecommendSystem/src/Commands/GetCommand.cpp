#include "GetCommand.h"


// This method returns the help message for the add command
string GetCommand::getHelp() const {
    string symbol = this->getSymbol();
    transform(symbol.begin(), symbol.end(), symbol.begin(), ::toupper);
    return symbol + ", arguments: [userid] [movieid1]";
}

string GetCommand :: execute(string input)
{
    //Send the string to the splitToVector function to split it into a vector of strings.
    vector<string> splitInput = splitToVector(input, ' ');
    //check if the input is valid.
	string valid = isValidInput(splitInput);
    if (valid != getCommandResultMessage(CommandResult::OK))
    {
        return valid;
    }
    /*Convert all the elements except the first one to integers,
    there is no worry about the input because we already checked if valid.*/
    vector<int> IdVector = vectorStoI(splitInput);
    //Get the user and movie id from the vector.
    int userId = IdVector[0];
    int movieId = IdVector[1];
    //Get the movie for the user.
    string recommendationsString = "";
    vector<pair<int,int>> recommendations = getRecommendedMovies(userId, movieId);
    for (pair<int,int> recommendation : recommendations) {
        recommendationsString += to_string(recommendation.first) + " ";
    }
    //Check if the recommendation is empty.
    if (isRecommendEmpty(recommendations))
    {
        return getCommandResultMessage(CommandResult::OK);
    }
    recommendationsString.resize(recommendationsString.size() - 1);
    //Send the proper message to the user.
    return getCommandResultMessage(CommandResult::OK) + "\n\n" + recommendationsString;
};

string GetCommand :: isValidInput(vector<string> input) const
{
        // Check if the input less than 3 elements, if so return false.
    if (input.size() < 3)
    {
        return getCommandResultMessage(CommandResult::BAD_REQUEST);
    }
    // Remove the first element ('get') from the vector.
    input = vector<string>(input.begin() + 1, input.end());
    // Check if all the elements are integers, if not return 'BAD_REQUEST'.
    if (!isIntegers(input))
    {
        return getCommandResultMessage(CommandResult::BAD_REQUEST);
    }
	int userId = stoi(input[0]);
	//check if the user exist.
	if (m_io_object->isUserExist(userId) == IOResult::NOT_FOUND)
	{
		return getCommandResultMessage(CommandResult::NOT_FOUND);
	}
	//
    //if the input is valid return true.
    // If the input is valid return 'OK'.
    return getCommandResultMessage(CommandResult::OK);
}

vector<pair<int,int>> GetCommand :: getRecommendedMovies(int userId, int movieId)
{
    //Get the user's movies ,all movies and all users.
    vector<int> userMovies = m_io_object->getUserMovies(userId);
    vector<int> users = m_io_object->getAllUsers();

    //Make list of movies that the user has not seen (without the movie that we want to recommend).
    vector<int> notSeenMovies = getNotSeenMovies(userMovies, movieId);

    //Create a map that contains the similarity between the user and other users.
    map<int, int> similarityMap = getSimilarityMap(userMovies, users, movieId, userId);

    //Get the recommended movies for the user.
    vector<pair<int,int>> recommendedMovies = getFinalRecommendation(notSeenMovies, similarityMap, movieId);

    return recommendedMovies;
}

vector<int> GetCommand :: getNotSeenMovies(vector<int> userMovies, int movieId)
{
    //Make list of movies that the user has not seen
    vector<int> notSeenMovies;
    vector<int> allMovies = m_io_object->getAllMovies();
    bool seen = false;
    // Go through all the movies and check if the user has seen the movie.
    for (int i = 0; i < allMovies.size(); i++)
    {
        for (int j = 0; j < userMovies.size(); j++)
        {
            if (allMovies[i] == userMovies[j])
            {
                seen = true;
                break;
            }
        }
    // If the user has not seen the movie, and its not the selected movie, add it to the notSeenMovies vector.
    if(!seen && allMovies[i] != movieId)
        {
            notSeenMovies.push_back(allMovies[i]);
        }
        seen = false;
    }
    // Return the list of movies that the user has not seen.
    return notSeenMovies;
};

map<int, int> GetCommand :: getSimilarityMap(vector<int> userMovies, vector<int> users, int movieId, int userId)
{
    //Create a map that contains the similarity between the user and other users.
    map<int, int> similarityMap;
    //Run in a loop from 0 to the size of the users vector to get the similarity between the user and the other users.
    for (int i = 0; i < users.size(); i++)
    {
        //check if the user is not the same user.
        if (users[i] != userId)
        {
            //check the number of common movies between the user and the other user (if 0 dont store the user).
            int commonMovies = 0;
            //get the other user's movies and store them in a vector.
            vector<int> otherUsersMovies = m_io_object->getUserMovies(users[i]);
            for (int j = 0 ; j < userMovies.size(); j++)
            {
                for (int k = 0; k < otherUsersMovies.size(); k++)
                {
                    //check if the other user has seen the movie.
                    if (userMovies[j] == otherUsersMovies[k])
                    {
                        commonMovies++;
                    }
                }
            }
            //store the other user and the number of common movies in the map.
            if (commonMovies != 0)
            {
                similarityMap[users[i]] = commonMovies;
            }
        }
    }
    return similarityMap;
}

vector<pair<int,int>> GetCommand :: getFinalRecommendation(vector<int> notSeenMovies, map<int, int> similarityMap, int movieId)
{
    //We will go through all the movies and check how many users from the map have seen the movie
    //for match we will add the similarity to the movie in the recommendedMovies map
    map<int, int> recommendedMovies;
    for (int i = 0; i < notSeenMovies.size(); i++)
    {
        //loop with for each on all other users with similarity
        for (auto const& x : similarityMap)
        {
            int flag = 0;
            //check if the other user has seen the movie.
            vector<int> otherUserMovies = m_io_object->getUserMovies(x.first);
            for (int j = 0 ; j <otherUserMovies.size() ; j++)
            {
                if (movieId == otherUserMovies[j])
                {
                    flag = 1;
                    break;
                }
            }
            if (flag == 1)
            {
                for (int j = 0; j < otherUserMovies.size(); j++)
                {
                    if(notSeenMovies[i] == otherUserMovies[j])
                    {
                        //add the similarity to the movie in the recommendedMovies map.
                        recommendedMovies[notSeenMovies[i]] += x.second;
                        break;
                    }
                }
            }
        }
    }
    //get the ten most recommended movies (if there are less than ten movies return all the movies).
    return getSortedRecommendedMovies(recommendedMovies);

    
}

vector<pair<int,int>> GetCommand :: getSortedRecommendedMovies(map<int, int> recommendMap)
{
    /*sort from the highest similarity to the lowest, if there are two or movies with the same similarity 
    take first the movie with the lowest id.*/

    //convert the map to a vector of pairs.
    vector<pair<int, int>> sortedRecommendations(recommendMap.begin(), recommendMap.end());
    //use lambda function to sort the vector.
    sort(sortedRecommendations.begin(), sortedRecommendations.end(),[](const pair<int, int>& movie1, 
    const pair<int, int>& movie2) 
    {
        //if the similarity is the same take the movie with the lowest id.
        if (movie1.second == movie2.second) 
        {
            return movie1.first < movie2.first;
        }
        //sort from the highest similarity to the lowest.
        return movie1.second > movie2.second;
        });
    return sortedRecommendations;
}

bool GetCommand :: isRecommendEmpty(const vector<pair<int,int>>& recommendedMovies) const
{
    //check if the recommendation is empty.
    if (recommendedMovies.size() == 0)
    {
        return true;
    }
    return false;
}

