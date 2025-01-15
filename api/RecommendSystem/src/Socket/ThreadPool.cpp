#include "ThreadPool.h"

// Initialize the mutex
mutex ThreadPool::queueMutex;
mutex ThreadPool::appMutex;


void ThreadPool::workerFunction() {
    while (true) {
        int clientSocket;
        {
		// Lock the mutex and wait until a task is available
        unique_lock<mutex> lock(queueMutex);
			// Wait until a task is available
            condition.wait(lock, [this]() { return stop || !tasks.empty(); });
			// If the ThreadPool is stopped and the queue is empty, return
            if (stop && tasks.empty())
                return;
			// Get the next task from the queue
            clientSocket = tasks.front();
			// Remove the task from the queue
            tasks.pop(); 
        }

        // Process the client outside the critical section
        handle_client(clientSocket);

        // Close the client socket after processing
        close(clientSocket);
    }
}

void ThreadPool::addTask(int clientSocket) {
    {
        std::unique_lock<std::mutex> lock(queueMutex);
        tasks.push(clientSocket);
    }
    condition.notify_one();
}

// Handle the client
void ThreadPool::handle_client(int clientSocket) {
	char buffer[BUFFER_SIZE] = {0};

	
	// create a loop to read the commands from the client
    size_t maxStringSize = std::string().max_size();
    while (true) {
		try{
			// Read the command from the client
			// create buffer of size 1024
			// resize it each time we read from the
			int currentSize = 0;
			vector<char> buffer(BUFFER_SIZE);
			int bytes_read = read(clientSocket, buffer.data() + currentSize, BUFFER_SIZE);
			while (bytes_read > 0 && buffer[currentSize + bytes_read - 1] != '\n') {
				// resize buffer to match all data
				currentSize += bytes_read;
				buffer.resize(currentSize + BUFFER_SIZE);
				bytes_read = read(clientSocket, buffer.data() + currentSize, BUFFER_SIZE);
			}
			// resize buffer to match all data
			currentSize += bytes_read;
			// Run the command and lock the global mutex until the command return the response
			// everything excluding the new line character
			string command;
			if (buffer.size() > maxStringSize || currentSize == 0) {
				// will make command to not work which will throw 404 if the command is bigger than the max size
				command = "Unknown command";
			}
			else {
				// everything excluding the new line character
				command = string(buffer.data(), currentSize - 1);
			}

			// run the command and lock the global mutex until the command return the response

			std::unique_lock<std::mutex> lock(appMutex);
			string response = app->run(command);
			// Unlock the global mutex
			lock.unlock();
			// Send the response back to the client
			send(clientSocket, response.c_str(), response.length(), 0);
		}
		// Catch any exceptions that occur and close the client socket
		catch(exception& e) { 
			// Close the client socket
			close(clientSocket);
			break;
		}
	}
}