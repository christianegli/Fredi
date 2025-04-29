
cmake_minimum_required(VERSION 3.22)
project(voiceloop)

set(CMAKE_CXX_STANDARD 17)
add_definitions(-DGGML_USE_ACCELERATE=0)

# pull in llama.cpp, whisper.cpp sources (clone as submodules or copy)
add_subdirectory(llama.cpp EXCLUDE_FROM_ALL)
add_subdirectory(whisper.cpp EXCLUDE_FROM_ALL)

add_library(voiceloop SHARED
    voiceloop_jni.cpp
)

target_include_directories(voiceloop PRIVATE
    llama.cpp
    whisper.cpp
)

target_link_libraries(voiceloop
    llama
    whisper
    android
    log
)
