//
// Created by 曾斌 on 2019-05-30.
//
#include <jni.h>
#include "_android.h"
#include <stdio.h>
#include <stdlib.h>

jint addNumber(JNIEnv *env, jclass clazz, jint a, jint b);

jint subNumber(JNIEnv *env, jclass clazz, jint a, jint b);

jint mulNumber(JNIEnv *env, jclass clazz, jint a, jint b);

jint divNumber(JNIEnv *env, jclass clazz, jint a, jint b);

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {

    JNIEnv *env = NULL;
    jint result = -1;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return result;
    }

    const JNINativeMethod method[] = {
            {"add", "(II)I", (void *) addNumber},
            {"sub", "(II)I", (void *) subNumber},
            {"mul", "(II)I", (void *) mulNumber},
            {"div", "(II)I", (void *) divNumber}
    };
    jclass jClassName = env->FindClass("com/example/admin/fisrtdemo/jnitest/JNIToola");

    jint ret = env->RegisterNatives(jClassName, method, 4);

    if (ret != JNI_OK) {
        return -1;
    }

    return JNI_VERSION_1_6;
}


jint addNumber(JNIEnv *env, jclass clazz, jint a, jint b) {
    return a + b;
}

jint subNumber(JNIEnv *env, jclass clazz, jint a, jint b) {
    return a - b;
}

jint mulNumber(JNIEnv *env, jclass clazz, jint a, jint b) {
    return a * b;
}

jint divNumber(JNIEnv *env, jclass clazz, jint a, jint b) {
    return a / b;
}

