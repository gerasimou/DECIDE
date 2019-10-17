#--------------------------------------------------------
# The CMakeLists.txt for:                       sSonarSensor
# Author(s):                              Simos
#--------------------------------------------------------

# FILE(GLOB SRC *.cpp)

SET(SRC
  SonarSensor.cpp
  SonarSensor_Info.cpp
  main.cpp
)



ADD_EXECUTABLE(sSonarSensor ${SRC})

TARGET_LINK_LIBRARIES(sSonarSensor
   ${MOOS_LIBRARIES}
   mbutil
   m
   pthread)

