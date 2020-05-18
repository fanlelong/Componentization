
cmake_minimum_required(VERSION 3.4.1)
file(GLOB SOUR src/main/cpp/*.cpp)#定义一个SOUR的全局变量
add_library( # Sets the name of the library.
        ffmpeg-first
        SHARED
        ${SOUR}

        src/main/cpp/native-lib.cpp)

include_directories(src/main/cpp/include)#指明头文件
include_directories(src/main/cpp/hafuman)#指明头文件
set(my_lib_path ${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI})#指明编译类型 对my_lib_path设置值
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${my_lib_path}")#CMAKE_CXX_FLAGS表示要传入的编译路径
find_library( # Sets the name of the path variable.
        log-lib
        log)


target_link_libraries( # Specifies the target library.
        ffmpeg-first
        ${log-lib}
        android
        z
        jnigraphics
        OpenSLES
        jpeg
        turbojpeg
        )