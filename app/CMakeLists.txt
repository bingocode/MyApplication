cmake_minimum_required(VERSION 3.4.1)

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/native-lib.cpp
        )
add_library( # Sets the name of the library.
        native-lib2

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/samplelib_jni.cpp
        )

add_library( # Sets the name of the library.
        caculate_jni

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/caculate_jni.cpp
        )

find_library( # Defines the name of the path variable that stores the
        # location of the NDK library.
        log-lib

        # Specifies the name of the NDK library that
        # CMake needs to locate.
        log)

target_link_libraries( # Specifies the target library.
        native-lib

        # Links the log library to the target library.
        ${log-lib}
        )
target_link_libraries( # Specifies the target library.
        native-lib2

        # Links the log library to the target library.
        ${log-lib}
        )
