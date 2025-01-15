// Server.cpp
#include "Server.h"


void Server::start() {
    // Create the socket and accept clients
    setup_socket();
    // Accept clients
    accept_clients();
}

void Server::setup_socket() {
    // Create the socket
    if ((server_fd = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        perror("Socket failed");
        exit(EXIT_FAILURE);
    }
    
    if (setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR | SO_REUSEPORT, &opt, sizeof(opt))) {
        perror("setsockopt failed");
        exit(EXIT_FAILURE);
    }
    // Set the address and port
    address.sin_family = AF_INET;
    address.sin_port = htons(port);

    // Convert the IP address to binary form
    if (inet_pton(AF_INET, ip.c_str(), &address.sin_addr) <= 0) {
        std::cerr << "Invalid IP address format!" << std::endl;
        close(server_fd);
        exit(EXIT_FAILURE);
    }

    // Bind the socket to the address and port
    if (bind(server_fd, (struct sockaddr *)&address, sizeof(address)) < 0) {
        perror("Bind failed");
        exit(EXIT_FAILURE);
    }
    
    // Listen for incoming connections
    if (listen(server_fd, 3) < 0) {
        perror("Listen failed");
        exit(EXIT_FAILURE);
    }
}

void Server::accept_clients() {
    //cteate a APP instance to run the commands from the client in the thread
    App* app = new App();
    // Initialize the app
    app->init();
	
	//initialize thread pool
	ThreadPool threadPool(MAX_CLIENTS, app);

    //create a loop to accept the clients
    while (true) {
        int client_socket;
        // Accept the client
        if ((client_socket = accept(server_fd, (struct sockaddr *)&address, (socklen_t *)&addrlen)) < 0) {
            perror("Accept failed");
            continue;
        }

        // Add the client to the thread pool
		threadPool.addTask(client_socket);
    }
}

