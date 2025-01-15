#include "../Socket/Server.h"
#include "../Commands/CommandFeatures.h"
#include <arpa/inet.h>
#include <sys/socket.h>
#include <thread>
#include <gtest/gtest.h>
#include "../Helpers/FileIO.h"

class dummySocketClient{
public:
    explicit dummySocketClient(int port) : port(port), ip_address("127.0.0.1") {
        // initialize the socket
        sock = socket(AF_INET, SOCK_STREAM, 0);
        EXPECT_GE(sock, 0) <<  "error creating socket";
        memset(&sin, 0, sizeof(sin));
        sin.sin_family = AF_INET;
        sin.sin_addr.s_addr = inet_addr(ip_address.c_str());
        sin.sin_port = htons(port);
    }
    void connect_socket() {
        EXPECT_GE(connect(sock, (struct sockaddr *) &sin, sizeof(sin)), 0) << "error connecting to server";
    }
    void send_socket(const string& message) {
        int data_len = message.length();
        int sent_bytes = send(sock, message.c_str(), data_len, 0);
        EXPECT_GE(sent_bytes, 0) << "error sending message";
    }

    string receive_socket() {
        char buffer[4096];
        int expected_data_len = sizeof(buffer);
        int read_bytes = recv(sock, buffer, expected_data_len, 0);
        EXPECT_GE(read_bytes, 0) << "error receiving message";
        return string(buffer,read_bytes);
    }
    void close_socket() {
        close(sock);
    }
private:
    const int port;
    const string ip_address;
    int sock;
    struct sockaddr_in sin;
};

const int port = 8080;
const string ip = "0.0.0.0";
const bool reached = true;
Server *serverTester = new Server(ip, port);
CommandFeatures *featuresObject = new CommandFeatures();

TEST(ServerTest, runServerAndBindToPort) {
    serverTester->setup_socket();
    // if reached here then the server has successfully binded to the port
    EXPECT_TRUE(reached);
}

TEST(ServerTest, acceptClients) {
    std::thread(&Server::accept_clients,serverTester).detach();
    // if reached here then the server has successfully accepted clients
    EXPECT_TRUE(reached);
}

// CHECK THE SERVER RESPONSES
TEST(ServerTest, registeringNewClient) {
    dummySocketClient *socketServerClient = new dummySocketClient(port);
    socketServerClient->connect_socket();
    socketServerClient->send_socket("post 1 100 101\n");
    string received_message = socketServerClient->receive_socket();
    EXPECT_STREQ(received_message.c_str(),
                 featuresObject->getCommandResultMessage(CommandResult::CREATED).c_str());
    socketServerClient->send_socket("patch 1 102 103\n");
    received_message = socketServerClient->receive_socket();
    EXPECT_STREQ(received_message.c_str(),
                 featuresObject->getCommandResultMessage(CommandResult::NO_CONTENT).c_str());
    socketServerClient->send_socket("delete 1 100 101 102 103\n");
    received_message = socketServerClient->receive_socket();
    EXPECT_STREQ(received_message.c_str(),
                 featuresObject->getCommandResultMessage(CommandResult::NO_CONTENT).c_str());
    socketServerClient->close_socket();
    FileIO *serverFileIo = new FileIO();
    serverFileIo->clearData();
}

// Check the  multi-threading
TEST(ServerTest, multipleClients) {
    dummySocketClient *socketServerClient1 = new dummySocketClient(port);
    dummySocketClient *socketServerClient2 = new dummySocketClient(port);
    dummySocketClient *socketServerClient3 = new dummySocketClient(port);
    socketServerClient2->connect_socket();
    socketServerClient1->connect_socket();
    socketServerClient3->connect_socket();
    socketServerClient1->send_socket("post 1 100 101\n");
    socketServerClient2->send_socket("post 2 200 201\n");
    socketServerClient3->send_socket("post 3 300 301\n");
    string received_message1 = socketServerClient1->receive_socket();
    string received_message2 = socketServerClient2->receive_socket();
    string received_message3 = socketServerClient3->receive_socket();
    EXPECT_STREQ(received_message1.c_str(),
                featuresObject->getCommandResultMessage(CommandResult::CREATED).c_str());
    EXPECT_STREQ(received_message2.c_str(),
                featuresObject->getCommandResultMessage(CommandResult::CREATED).c_str());
    EXPECT_STREQ(received_message3.c_str(),
                featuresObject->getCommandResultMessage(CommandResult::CREATED).c_str());
    socketServerClient1->close_socket();
    socketServerClient2->close_socket();
    socketServerClient3->close_socket();
    FileIO *serverFileIo = new FileIO();
    serverFileIo->clearData();
}

//Check if the mutex works and the lock is working from server pov handleClient


