cmake_minimum_required(VERSION 3.10)

project(NETFLIX)

set(CMAKE_CXX_STANDARD 11)
# Collect all .cpp files from current directory
file(GLOB CURRENT_SOURCES "*.cpp")
file(GLOB COMMANDS_SOURCES "./Commands/*.cpp")
file(GLOB HELPERS_SOURCES "./Helpers/*.cpp")
file(GLOB INTERFACES_SOURCES "./Interfaces/*.cpp")
file(GLOB SOCKET_SOURCES "./Socket/*.cpp")

add_executable(Netflix main.cpp ${CURRENT_SOURCES} ${COMMANDS_SOURCES} ${HELPERS_SOURCES} ${INTERFACES_SOURCES} ${SOCKET_SOURCES})