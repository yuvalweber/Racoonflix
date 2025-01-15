#include "App.h"
#include "Socket/Server.h"
#include <iostream>

using namespace std;    

int main(int argc, char *argv[]) {
    //Initialize the server
    int port = 8080;
    string ip = "0.0.0.0";
    try {
        // if the user provides a port number, use it
        if (argc == 3) {
            ip = argv[1];
            port = stoi(argv[2]);
        }   
        Server server(ip,port);
        server.start();
    } catch (const std::exception &e) {
        std::cerr << "Exception: " << e.what() << std::endl;
    }
    
    // Will never reach this point
    return 0;
}
