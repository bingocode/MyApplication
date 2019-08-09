//
// Created by 曾斌 on 2019-05-27.
//
#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_admin_fisrtdemo_jnitest_NDKTools_getStringFromNDK(
        JNIEnv *env, jclass type) {
    std::string hello = "(*^__^*) 嘻嘻……~Hello from C++ 隔壁老李头";
    return env->NewStringUTF(hello.c_str());
}

