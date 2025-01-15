// Server.h
#ifndef SERVER_H
#define SERVER_H

using namespace std;

#include <vector>
#include <thread>
#include <mutex>
#include <netinet/in.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include "../App.h"
#include "ThreadPool.h"
#include <iostream>
#include <cstring>
#include <unistd.h>

class Server {
public:
    void start();
    // Constructor
    explicit Server(string ip, int port) : ip(ip) ,port(port), opt(1), addrlen(sizeof(address)), server_fd(-1) {}
    //this function will create the socket and bind it to the port
    void setup_socket();
    //this function will accept the clients
    void accept_clients();

private:
    // The port of the server
    int port;
    // The server socket file descriptor
    int server_fd;
    // The address of the server
    struct sockaddr_in address;
    // The option of the server
    int opt;
    // The length of the address
    int addrlen;
    string ip;
    mutex clients_mutex;
    // The vector of the threads of the clients
    vector<std::thread> client_threads;

};

#endif // SERVER_H





