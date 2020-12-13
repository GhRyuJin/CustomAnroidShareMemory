
cmake_minimum_required(VERSION 3.4.1)

add_library(
        mysharememory-lib

        SHARED

        mysharememory.cpp)

add_library(
        android_shm-lib

        SHARED

        android_shm.cpp
)
find_library(
        log-lib

        log)
include_directories(src/main/cpp/)


target_link_libraries(
        android_shm-lib
)

target_link_libraries(
        mysharememory-lib
        android_shm-lib
        ${log-lib}
)