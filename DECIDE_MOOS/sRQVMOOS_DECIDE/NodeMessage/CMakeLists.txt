#cmake_minimum_required(VERSION 2.8.6)

#FIND_PACKAGE(JNI REQUIRED)

#SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -I/usr/lib/jvm/java-7-openjdk-amd64/include -I/usr/lib/jvm/java-7-openjdk-amd64/include/linux -L/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/amd64/server")

#SET(CMAKE_EXE_LINKER_FLAGS "-ljvm")

# add the binary tree directory to the search path for include files
#include_directories(${CMAKE_CURRENT_SOURCE_DIR})
#include_directories(${JNI_INCLUDE_DIRS})


#message("JNI:- " ${JNI_LIBRARIES})
#message("Source Dir: - " ${PROJECT_SOURCE_DIR})

#target_link_libraries (PRISMLibrar ${JNI_LIBRARIES})


#Need to create a library for the files used by the main application; otherwise the compiler cannot find the implementation of the functions
add_library(NodeMsgLib SHARED NodeMessage.cpp)
target_link_libraries (NodeMsgLib)




