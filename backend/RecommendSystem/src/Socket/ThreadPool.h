// ClientHandler.h
#ifndef ThreadPool_H
#define ThreadPool_H

using namespace std;

#include <iostream>
#include <thread>
#include <mutex>
#include <atomic>
#include <vector>
#include <queue>
#include <condition_variable>
#include <string>
#include <cstring>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include "../App.h"

#define BUFFER_SIZE 1024
#define MAX_CLIENTS 100

class ThreadPool 
{
public:
	// Constructor for the ThreadPool
    ThreadPool(size_t numThreads, App* app) : stop(false), app(app) {
		// Initialize the mutex
		for (size_t i = 0; i < numThreads; ++i) {
			workers.emplace_back([this]() { workerFunction(); });
		}
	}
	// Destructor for the ThreadPool
    ~ThreadPool() {
		{
			unique_lock<mutex> lock(queueMutex);
			stop = true;
		}
	// Notify all threads to stop
		condition.notify_all();
		for (thread &worker : workers) {
			if (worker.joinable()) {
				worker.join();
			}
		}
	}

	// Add a task to the ThreadPool
	void addTask(int clientSocket);


private:
    App* app;
    vector<thread> workers;
    queue<int> tasks; // Queue of client sockets
    static mutex queueMutex;
	static mutex appMutex;
    condition_variable condition;
    atomic<bool> stop;

	// Worker function for the ThreadPool
	void workerFunction();
	// Handle the client
	void handle_client(int clientSocket);
};

#endif // CLIENTHANDLER_H
