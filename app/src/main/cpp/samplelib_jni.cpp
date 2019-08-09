//
// Created by 曾斌 on 2019-05-28.
//

#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include "_android.h"
#define _JNILOG_TAG		"samplelib_jni"

#ifdef __cplusplus
extern "C" {
#endif

static const char *className = "com/example/admin/fisrtdemo/jnitest/JniDemo1";

static jstring sayHello(JNIEnv *env, jobject, jlong handle) {
    LOGI("native: say hello ###");
    std::string hello = "动态注册：嘻嘻……~Hello from C++ 隔壁老李头";
    return env->NewStringUTF(hello.c_str());
}

static JNINativeMethod gJni_Methods_table[] = {
        {"sayHello", "(J)Ljava/lang/String;", (jstring)sayHello},
};

static int jniRegisterNativeMethods(JNIEnv* env, const char* className,
                                    const JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;

    LOGI("JNI","Registering %s natives\n", className);
    clazz = (env)->FindClass( className);
    if (clazz == NULL) {
        LOGE("JNI","Native registration unable to find class '%s'\n", className);
        return -1;
    }

    int result = 0;
    if ((env)->RegisterNatives(clazz, gJni_Methods_table, numMethods) < 0) {
        LOGE("JNI","RegisterNatives failed for '%s'\n", className);
        result = -1;
    }

    (env)->DeleteLocalRef(clazz);
    return result;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved){
    LOGI("JNI", "enter jni_onload");

    JNIEnv* env = NULL;
    jint result = -1;

    if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }

    jniRegisterNativeMethods(env, className, gJni_Methods_table, sizeof(gJni_Methods_table) / sizeof(JNINativeMethod));

    return JNI_VERSION_1_4;
}

#ifdef __cplusplus
}
#endif