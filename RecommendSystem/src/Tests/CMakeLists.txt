cmake_minimum_required(VERSION 3.10)

project(MyTest)

set(CMAKE_CXX_STANDARD 11)

include(FetchContent)
FetchContent_Declare(
  googletest
  URL https://github.com/google/googletest/archive/refs/tags/release-1.11.0.zip
)

set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
FetchContent_MakeAvailable(googletest)

# Collect all .cpp files from current directory
file(GLOB CURRENT_SOURCES "*.cpp")
file(GLOB COMMANDS_SOURCES "../Commands/*.cpp")
file(GLOB HELPERS_SOURCES "../Helpers/*.cpp")
file(GLOB INTERFACES_SOURCES "../Interfaces/*.cpp")
file(GLOB SOCKET_SOURCES "../Socket/*.cpp")
set(APP_SOURCES "../App.cpp")

add_executable(runTests test.cpp ${CURRENT_SOURCES} ${COMMANDS_SOURCES} ${HELPERS_SOURCES} ${INTERFACES_SOURCES} ${SOCKET_SOURCES} ${APP_SOURCES})

target_compile_definitions(runTests PRIVATE TESTS)

target_link_libraries(runTests gtest_main)