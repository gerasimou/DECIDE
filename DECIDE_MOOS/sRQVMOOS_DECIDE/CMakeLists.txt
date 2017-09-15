#--------------------------------------------------------
# The CMakeLists.txt for:                        RQVMOOS
# Author(s):                         		 Simos     
#--------------------------------------------------------

# FILE(GLOB SRC *.cpp)

SET(SRC
  RQVMOOS_DECIDE.cpp
  RQVMOOS_DECIDE_Info.cpp
  main.cpp
)

SET (CMAKE_VERBOSE_MAKEFILE, 1)

#INCLUDE_DIRECTORIES(NodeMessage)
#ADD_SUBDIRECTORY(NodeMessage)

ADD_SUBDIRECTORY(Client)
ADD_SUBDIRECTORY(MergeSort)

ADD_EXECUTABLE(sRQVMOOS_DECIDE ${SRC})

TARGET_LINK_LIBRARIES(sRQVMOOS_DECIDE
   ${MOOS_LIBRARIES}
   mbutil
   m
   pthread
   ClientLib
   MergeSortLib)
