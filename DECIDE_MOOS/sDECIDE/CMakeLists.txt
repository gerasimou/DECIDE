#--------------------------------------------------------
# The CMakeLists.txt for:                        RQVMOOS
# Author(s):                         		 Simos     
#--------------------------------------------------------

# FILE(GLOB SRC *.cpp)

SET(SRC
  DECIDE.cpp
  DECIDE_Info.cpp
  main.cpp
)

SET (CMAKE_VERBOSE_MAKEFILE, 1)

#INCLUDE_DIRECTORIES(NodeMessage)
#ADD_SUBDIRECTORY(NodeMessage)

ADD_SUBDIRECTORY(Client)
ADD_SUBDIRECTORY(MergeSort)

ADD_EXECUTABLE(sDECIDE ${SRC})

TARGET_LINK_LIBRARIES(sDECIDE
   ${MOOS_LIBRARIES}
   mbutil
   m
   pthread
   ClientLib
   MergeSortLib)
