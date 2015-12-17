cmake_minimum_required(VERSION 3.3)
project(SimpleIDE)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    cmm/Library.binary
    cmm/Library.elf
    cmm/MulticoreAddedCpp.binary
    cmm/RobotWifi.binary
    cmm/RobotWifi.elf
    cmm/RobotWifi2.binary
    cmm/RobotWifi2.elf
    cmm/RobotWifi3.binary
    cmm/RobotWifi3.elf
    cmm/ScanToInt.binary
    cmm/ScanToInt.elf
    cmm/Wifi.binary
    cmm/Wifi.elf
    cmm/WiFiCode.binary
    cmm/WiFiCode.elf
    "Display/cmm/Hello LCD.binary"
    "Display/cmm/Hello LCD.elf"
    "Display/Hello LCD.c"
    "Display/Hello LCD.side"
    Display/LiqCrystalDisplay.cpp
    Display/LiqCrystalDisplay.side
    "lmm/Multicore Example2.binary"
    "lmm/Multicore Example2.elf"
    lmm/MulticoreAddedCpp.binary
    lmm/RobotWifi2.binary
    lmm/RobotWifi2.elf
    QTI/cmm/QTI_Test.binary
    QTI/cmm/QTI_Test.elf
    "QTI/QTI Example Code for ActivityBot/cmm/Test QTIs.binary"
    "QTI/QTI Example Code for ActivityBot/cmm/Test QTIs.elf"
    "QTI/QTI Example Code for ActivityBot/Line Follow with QTIs.c"
    "QTI/QTI Example Code for ActivityBot/Line Follow with QTIs.side"
    "QTI/QTI Example Code for ActivityBot/Test QTIs.c"
    "QTI/QTI Example Code for ActivityBot/Test QTIs.side"
    QTI/28108-QTI-Line-Follower-C-Code.zip
    QTI/QTI_Test.c
    QTI/QTI_Test.side
    RobotWifi/cmm/RobotWifi2.binary
    RobotWifi/cmm/RobotWifi2.elf
    RobotWifi/cmm/RobotWifiC.binary
    RobotWifi/cmm/RobotWifiC.elf
    RobotWifi/cmm/RobotWifiC4.binary
    RobotWifi/cmm/RobotWifiC4.elf
    "RobotWifi/RobotWifi copy.cpp"
    RobotWifi/RobotWifi.cpp
    RobotWifi/RobotWifi.h
    RobotWifi/RobotWifi2.c
    RobotWifi/RobotWifi2.h
    RobotWifi/RobotWifi2.side
    RobotWifi/RobotWifi3.cpp
    RobotWifi/RobotWifi3.h
    RobotWifi/RobotWifi3.side
    RobotWifi/RobotWifiC.c
    RobotWifi/RobotWifiC.h
    RobotWifi/RobotWifiC.side
    RobotWifi/RobotWifiC4.c
    RobotWifi/RobotWifiC4.h
    RobotWifi/RobotWifiC4.side
    RobotWifi/tags
    Scan/ScanToInt.c
    Scan/ScanToInt.side
    Wifi/Wifi.c
    Wifi/Wifi.side
    Wifi/WiFiCode.c
    Wifi/WiFiCode.side
    WorkExperience/cmm/WorkExperience.binary
    WorkExperience/cmm/WorkExperience.elf
    WorkExperience/cmm/WorkExperienceLib.o
    "WorkExperience/fixed simos's program final (1).c"
    WorkExperience/tags
    WorkExperience/WorkExperience.c
    WorkExperience/WorkExperience.side
    WorkExperience/WorkExperienceFixed.c
    WorkExperience/WorkExperienceLib.c
    WorkExperience/WorkExperienceLib.h
    xmmc/MulticoreAddedCpp.elf
    Library.c
    Library.side
    "Multicore Example2.c"
    "Multicore Example2.h"
    "Multicore Example2.side"
    MulticoreAddedCpp.cpp
    MulticoreAddedCpp.side
    tags)

add_executable(SimpleIDE ${SOURCE_FILES})